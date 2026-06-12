package stubs;

import patterns.command.IComandoSincronizacao;

/*
  STUB — IComandoSincronizacao
  Registra execuções do comando sem efeito colateral.
  Usado para testar FilaSincronizacao de forma isolada.
 */
public class ComandoStub implements IComandoSincronizacao {

    private int execucoes = 0;

    @Override
    public void executar() {
        execucoes++;
    }

    public int getExecucoes() { return execucoes; }
}
