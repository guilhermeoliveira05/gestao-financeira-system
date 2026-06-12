package stubs;

import patterns.observer.ConexaoStatus;
import patterns.observer.IObservadorConexao;

import java.util.ArrayList;
import java.util.List;

/*
  STUB — IObservadorConexao
  Captura todos os status recebidos para asserção nos testes do Observer.
 */
public class ObservadorStub implements IObservadorConexao {

    private final List<ConexaoStatus> statusRecebidos = new ArrayList<>();

    @Override
    public void atualizar(ConexaoStatus status) {
        statusRecebidos.add(status);
    }

    public List<ConexaoStatus> getStatusRecebidos() { return statusRecebidos; }
    public int getChamadas()                         { return statusRecebidos.size(); }
    public ConexaoStatus getUltimo()                 { return statusRecebidos.isEmpty() ? null : statusRecebidos.get(statusRecebidos.size() - 1); }
}
