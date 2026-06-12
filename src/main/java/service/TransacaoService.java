package service;

import model.Transacao;
import patterns.command.FilaSincronizacao;
import patterns.command.SincronizarTransacaoCommand;
import patterns.observer.Conexao;
import patterns.observer.ConexaoStatus;
import patterns.proxy.ISincronizacao;
import patterns.strategy.IEstrategiaProcessamento;
import patterns.strategy.ProcessamentoOffline;
import patterns.strategy.ProcessamentoOnline;

/*
 - Regras de negócio para processamento de transações.
 - Orquestra a escolha de estratégia conforme o estado da rede e enfileira comandos de sincronização.
*/

public class TransacaoService {

    private final Conexao conexao;
    private final ISincronizacao sincronizacao;
    private final FilaSincronizacao fila;

    public TransacaoService(Conexao conexao, ISincronizacao sincronizacao, FilaSincronizacao fila) {
        this.conexao = conexao;
        this.sincronizacao = sincronizacao;
        this.fila = fila;
    }

    /*
     Processa a transação escolhendo automaticamente a estratégia com base no status atual da rede.
    */

    public void processar(Transacao transacao) {
        IEstrategiaProcessamento estrategia = conexao.getStatus() == ConexaoStatus.ONLINE
                ? new ProcessamentoOnline()
                : new ProcessamentoOffline();

        transacao.setEstrategia(estrategia);
        transacao.salvar();

        fila.enfileirar(new SincronizarTransacaoCommand(transacao, sincronizacao));
    }

    /*
     Dispara o processamento de todos os comandos acumulados na fila.
    */
   
    public void sincronizarFila() {
        fila.processarFila();
    }
}
