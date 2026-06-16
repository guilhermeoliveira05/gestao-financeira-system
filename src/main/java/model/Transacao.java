package model;

import patterns.singleton.ConfiguracaoSistema;
import patterns.strategy.IEstrategiaProcessamento;

import java.math.BigDecimal;

/*
  Entidade central do domínio financeiro.
  Utiliza o padrão Strategy para definir como será processada.
  Ao salvar, consulta ConfiguracaoSistema (Singleton) para obter a URL da API onde o resultado será registrado.
*/

public class Transacao {

    private final String id;
    private final BigDecimal valor;
    private TransacaoStatus status;
    private IEstrategiaProcessamento estrategia;

    public Transacao(String id, BigDecimal valor) {
        this.id     = id;
        this.valor  = valor;
        this.status = TransacaoStatus.PENDENTE;
    }

    public void setEstrategia(IEstrategiaProcessamento estrategia) {
        this.estrategia = estrategia;
    }

    public void salvar() {
        if (estrategia == null) {
            System.out.println("[TRANSACAO] Nenhuma estratégia definida para " + id);
            return;
        }
        estrategia.processar(this);
        this.status = TransacaoStatus.PROCESSADA;

        // Consulta o Singleton para obter endpoint de registro
        String url = ConfiguracaoSistema.getInstancia().getUrlApi();
        System.out.println("[TRANSACAO] " + id + " registrada em: " + url
                + " | Valor: R$ " + valor);
    }

    public String          getId()     { return id; }
    public BigDecimal      getValor()  { return valor; }
    public TransacaoStatus getStatus() { return status; }
}
