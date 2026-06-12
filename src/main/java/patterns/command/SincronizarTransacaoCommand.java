package patterns.command;

import model.Transacao;
import patterns.proxy.ISincronizacao;

/*
 PADRÃO COMMAND — Comando concreto.
 - Encapsula uma Transacao e um receptor ISincronizacao.
 - A FilaSincronizacao (Invoker) não conhece detalhes internos.
 
*/

public class SincronizarTransacaoCommand implements IComandoSincronizacao {

    private final Transacao transacao;
    private final ISincronizacao receptor;

    public SincronizarTransacaoCommand(Transacao transacao, ISincronizacao receptor) {
        this.transacao = transacao;
        this.receptor = receptor;
    }

    @Override
    public void executar() {
        System.out.println("[COMMAND] Executando sincronização da transação: " + transacao.getId());
        receptor.sincronizar("Transacao{id=" + transacao.getId()
                + ", valor=" + transacao.getValor()
                + ", status=" + transacao.getStatus() + "}");
    }
}
