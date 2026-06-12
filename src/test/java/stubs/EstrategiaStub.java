package stubs;

import model.Transacao;
import patterns.strategy.IEstrategiaProcessamento;

/*
  STUB — IEstrategiaProcessamento
  Registra chamadas a processar() sem lógica real.
  Permite verificar qual estratégia foi selecionada pelo TransacaoService.
 */
public class EstrategiaStub implements IEstrategiaProcessamento {

    private int chamadas = 0;
    private Transacao ultimaTransacao;

    @Override
    public void processar(Transacao t) {
        chamadas++;
        ultimaTransacao = t;
    }

    public int getChamadas()              { return chamadas; }
    public Transacao getUltimaTransacao() { return ultimaTransacao; }
}
