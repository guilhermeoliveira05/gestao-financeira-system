package patterns.strategy;

import model.Transacao;

/*
 PADRÃO STRATEGY — Estratégia concreta para rede disponível.
 - Envia a transação diretamente ao servidor remoto.
*/

public class ProcessamentoOnline implements IEstrategiaProcessamento {
    @Override
    public void processar(Transacao t) {
        System.out.println("[STRATEGY:ONLINE] Processando transação " + t.getId()
                + " no servidor remoto. Valor: R$ " + t.getValor());
    }
}
