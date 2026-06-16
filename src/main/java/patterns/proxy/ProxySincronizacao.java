package patterns.proxy;

import patterns.observer.ConexaoStatus;
import patterns.observer.IObservadorConexao;

/*
 PADRÃO PROXY
 - Intercepta chamadas de sincronização e verifica o status da rede.
 - Implementa IObservadorConexao para reagir automaticamente às mudanças detectadas pelo Observer (Conexao).
*/

public class ProxySincronizacao implements ISincronizacao, IObservadorConexao {

    private final SincronizacaoReal real = new SincronizacaoReal();
    private ConexaoStatus statusAtual = ConexaoStatus.ONLINE;

    @Override
    public void atualizar(ConexaoStatus status) {
        this.statusAtual = status;
        System.out.println("[PROXY] Status de rede atualizado para: " + status);
    }

    @Override
    public void sincronizar(String payload) {
        if (statusAtual == ConexaoStatus.OFFLINE) {
            System.out.println("[PROXY] Acesso bloqueado — rede OFFLINE. Sincronização adiada para: " + payload);
            return;
        }
        System.out.println("[PROXY] Acesso permitido — delegando sincronização...");
        real.sincronizar(payload);
    }
}
