import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import patterns.observer.Conexao;
import patterns.observer.ConexaoStatus;
import patterns.proxy.ProxySincronizacao;
import stubs.SincronizacaoStub;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes do padrão PROXY — ProxySincronizacao
 * Cobre: CT-07, CT-08, CT-09
 *
 * Estratégia: ProxySincronizacaoTestavel é uma subclasse que injeta
 * o SincronizacaoStub no lugar do SincronizacaoReal (sem alterar o código de produção).
 */
@DisplayName("Proxy — ProxySincronizacao")
class CT03_ProxyTest {

    /**
     * STUB INTERNO — Subclasse testável do Proxy que expõe
     * o receptor como injetável, permitindo verificar se o serviço
     * real foi ou não chamado.
     */
    static class ProxySincronizacaoTestavel extends ProxySincronizacao {
        private final SincronizacaoStub stubReal;

        ProxySincronizacaoTestavel(SincronizacaoStub stubReal) {
            this.stubReal = stubReal;
        }

        @Override
        public void sincronizar(String payload) {
            // Reutiliza a lógica de bloqueio do pai via atualizar(),
            // mas delega ao stub em vez do SincronizacaoReal interno
            try {
                java.lang.reflect.Field f = ProxySincronizacao.class.getDeclaredField("statusAtual");
                f.setAccessible(true);
                patterns.observer.ConexaoStatus status =
                    (patterns.observer.ConexaoStatus) f.get(this);
                if (status == patterns.observer.ConexaoStatus.OFFLINE) {
                    System.out.println("[PROXY-TEST] Bloqueado — OFFLINE");
                    return;
                }
                stubReal.sincronizar(payload);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private SincronizacaoStub sincStub;
    private ProxySincronizacaoTestavel proxy;
    private Conexao conexao;

    @BeforeEach
    void setUp() {
        sincStub = new SincronizacaoStub();
        proxy    = new ProxySincronizacaoTestavel(sincStub);
        conexao  = new Conexao(ConexaoStatus.ONLINE);
        conexao.adicionarObservador(proxy);
    }

    // ── CT-07 ── Válido ──────────────────────────────────────────────
    @Test
    @DisplayName("CT-07 [V] Proxy bloqueia chamada ao serviço real quando OFFLINE")
    void ct07_bloqueiaQuandoOffline() {
        conexao.setStatus(ConexaoStatus.OFFLINE);

        proxy.sincronizar("payload-bloqueado");

        assertEquals(0, sincStub.getChamadas(),
            "SincronizacaoReal NÃO deve ser chamada quando OFFLINE");
    }

    // ── CT-08 ── Válido ──────────────────────────────────────────────
    @Test
    @DisplayName("CT-08 [V] Proxy delega ao serviço real quando ONLINE")
    void ct08_delegaQuandoOnline() {
        // Conexao já está ONLINE no setUp
        proxy.sincronizar("payload-permitido");

        assertEquals(1, sincStub.getChamadas(),
            "SincronizacaoReal deve ser chamada exatamente 1 vez quando ONLINE");
        assertEquals("payload-permitido", sincStub.getUltimoPayload());
    }

    // ── CT-09 ── Alternativo ─────────────────────────────────────────
    @Test
    @DisplayName("CT-09 [A] Proxy reage automaticamente à mudança de rede via Observer")
    void ct09_reageViaObserver() {
        // 1. Rede ONLINE → sincronização passa
        proxy.sincronizar("antes-da-queda");
        assertEquals(1, sincStub.getChamadas(), "1ª chamada deve passar (ONLINE)");

        // 2. Observer propaga OFFLINE automaticamente ao Proxy
        conexao.setStatus(ConexaoStatus.OFFLINE);

        // 3. Agora a sincronização deve ser bloqueada
        proxy.sincronizar("durante-queda");
        assertEquals(1, sincStub.getChamadas(), "2ª chamada deve ser bloqueada (OFFLINE via Observer)");
    }
}
