package model;

import patterns.singleton.ConfiguracaoSistema;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/*
  Entidade ContaPagar — presente no diagrama UML.
  Usa ConfiguracaoSistema (Singleton) para obter a URL da API no momento de registrar o pagamento.
 
  Relação no diagrama: ContaPagar ──uses──> ConfiguracaoSistema
*/

public class ContaPagar {

    private final String    id;
    private final String    descricao;
    private       double    valor;
    private       boolean   paga;
    private final LocalDate vencimento;

    public ContaPagar(String id, String descricao, double valor, LocalDate vencimento) {
        this.id         = id;
        this.descricao  = descricao;
        this.valor      = valor;
        this.paga       = false;
        this.vencimento = vencimento;
    }

    /*
     Registra o pagamento usando a URL configurada no Singleton.
    */
   
    public void registrarPagamento() {
        ConfiguracaoSistema cfg = ConfiguracaoSistema.getInstancia();
        System.out.println("[CONTA_PAGAR] Registrando pagamento de '" + descricao
                + "' (R$ " + valor + ") em: " + cfg.getUrlApi());
        this.paga = true;
    }

    /*
     Calcula quantos dias faltam até o vencimento.
     Valor negativo indica que a conta já venceu.
    */
    public long diasParaVencer() {
        return ChronoUnit.DAYS.between(LocalDate.now(), vencimento);
    }

    /*
     Dispara o alerta de vencimento (usado pelo AlertaVencimentoService).
    */
    public void notificarVencimento() {
        System.out.println("[CONTA_PAGAR] Notificando vencimento de '" + descricao
                + "' (vence em " + vencimento + ")");
    }

    public String    getId()         { return id; }
    public String    getDescricao()  { return descricao; }
    public double    getValor()      { return valor; }
    public boolean   isPaga()        { return paga; }
    public LocalDate getVencimento() { return vencimento; }
}