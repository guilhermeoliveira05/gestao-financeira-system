package patterns.observer;

/*
 PADRÃO OBSERVER — Interface do Observador.
*/

public interface IObservadorConexao {
    void atualizar(ConexaoStatus status);
}
