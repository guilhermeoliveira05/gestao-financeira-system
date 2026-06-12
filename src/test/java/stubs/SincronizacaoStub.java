package stubs;

import patterns.proxy.ISincronizacao;

/*
  STUB — ISincronizacao
  Registra chamadas a sincronizar() sem acessar nenhum servidor real.
  Usado nos testes do Proxy e do Command.
 */
public class SincronizacaoStub implements ISincronizacao {

    private int chamadas = 0;
    private String ultimoPayload;

    @Override
    public void sincronizar(String payload) {
        chamadas++;
        ultimoPayload = payload;
    }

    public int getChamadas()        { return chamadas; }
    public String getUltimoPayload(){ return ultimoPayload; }
    public void reset()             { chamadas = 0; ultimoPayload = null; }
}
