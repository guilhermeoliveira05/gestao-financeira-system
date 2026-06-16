import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import patterns.observer.Conexao;
import patterns.observer.ConexaoStatus;
import stubs.ObservadorStub;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes do padrão OBSERVER — Conexao
 * Cobre: CT-04, CT-05, CT-06
 */
@DisplayName("Observer — Conexao")
class CT02_ObserverTest {

    private Conexao conexao;

    @BeforeEach
    void setUp() {
        conexao = new Conexao(ConexaoStatus.ONLINE);
    }

    // ── CT-04 ── Válido ──────────────────────────────────────────────
    @Test
    @DisplayName("CT-04 [V] Observador é notificado quando status muda para OFFLINE")
    void ct04_notificaObservadorOffline() {
        ObservadorStub stub = new ObservadorStub();
        conexao.adicionarObservador(stub);

        conexao.setStatus(ConexaoStatus.OFFLINE);

        assertEquals(1, stub.getChamadas(),      "Deve haver exatamente 1 notificação");
        assertEquals(ConexaoStatus.OFFLINE, stub.getUltimo(), "Status recebido deve ser OFFLINE");
    }

    // ── CT-05 ── Válido ──────────────────────────────────────────────
    @Test
    @DisplayName("CT-05 [V] Todos os observadores registrados são notificados")
    void ct05_multiplosObservadoresNotificados() {
        ObservadorStub stub1 = new ObservadorStub();
        ObservadorStub stub2 = new ObservadorStub();
        ObservadorStub stub3 = new ObservadorStub();

        conexao.adicionarObservador(stub1);
        conexao.adicionarObservador(stub2);
        conexao.adicionarObservador(stub3);

        conexao.setStatus(ConexaoStatus.ONLINE);

        assertAll("Todos os stubs devem ser notificados",
            () -> assertEquals(1, stub1.getChamadas(), "Stub 1 deve receber 1 notificação"),
            () -> assertEquals(1, stub2.getChamadas(), "Stub 2 deve receber 1 notificação"),
            () -> assertEquals(1, stub3.getChamadas(), "Stub 3 deve receber 1 notificação")
        );
    }

    // ── CT-06 ── Exceção ─────────────────────────────────────────────
    @Test
    @DisplayName("CT-06 [E] Mudar status sem observadores registrados não lança exceção")
    void ct06_semObservadoresNaoExplode() {
        // Nenhum observador registrado — não deve lançar NullPointerException
        assertDoesNotThrow(
            () -> conexao.setStatus(ConexaoStatus.OFFLINE),
            "setStatus() sem observadores não deve lançar exceção"
        );
        assertEquals(ConexaoStatus.OFFLINE, conexao.getStatus());
    }
}
