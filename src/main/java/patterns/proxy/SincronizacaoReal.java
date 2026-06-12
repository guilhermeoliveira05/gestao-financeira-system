package patterns.proxy;

public class SincronizacaoReal implements ISincronizacao {
    @Override
    public void sincronizar(String payload) {
        System.out.println("[REAL] Sincronizando dados com servidor: " + payload);
    }
}
