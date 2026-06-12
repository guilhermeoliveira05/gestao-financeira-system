package patterns.strategy;

import model.Transacao;

/*
 PADRÃO STRATEGY — Estratégia concreta para rede indisponível.
 - Armazena a transação localmente para sincronização posterior.
*/

public class ProcessamentoOffline implements IEstrategiaProcessamento {
    @Override
    public void processar(Transacao t) {
        System.out.println("[STRATEGY:OFFLINE] Armazenando transação " + t.getId()
                + " localmente para sincronização posterior. Valor: R$ " + t.getValor());
    }
}
