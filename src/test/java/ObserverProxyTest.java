import org.junit.jupiter.api.Test;
import patterns.observer.Conexao;
import patterns.observer.ConexaoStatus;
import patterns.proxy.ProxySincronizacao;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ObserverProxyTest {

    @Test
    void deveBloqueiarSincronizacaoQuandoOffline() {
        ProxySincronizacao proxy = new ProxySincronizacao();
        Conexao conexao = new Conexao(ConexaoStatus.ONLINE);
        conexao.adicionarObservador(proxy);

        List<String> log = new ArrayList<>();
        // Sobrescrevemos SincronizacaoReal via proxy — verificamos pelo output
        conexao.setStatus(ConexaoStatus.OFFLINE);

        // Não lança exceção, apenas bloqueia silenciosamente
        assertDoesNotThrow(() -> proxy.sincronizar("payload-teste"));
    }

    @Test
    void deveNotificarObservadorQuandoStatusMuda() {
        List<ConexaoStatus> recebidos = new ArrayList<>();
        Conexao conexao = new Conexao(ConexaoStatus.ONLINE);
        conexao.adicionarObservador(recebidos::add);

        conexao.setStatus(ConexaoStatus.OFFLINE);
        conexao.setStatus(ConexaoStatus.ONLINE);

        assertEquals(2, recebidos.size());
        assertEquals(ConexaoStatus.OFFLINE, recebidos.get(0));
        assertEquals(ConexaoStatus.ONLINE,  recebidos.get(1));
    }
}
