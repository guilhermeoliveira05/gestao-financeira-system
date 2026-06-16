import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import patterns.singleton.ConfiguracaoSistema;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes do padrão SINGLETON — ConfiguracaoSistema
 * Cobre: CT-01, CT-02, CT-03
 */
@DisplayName("Singleton — ConfiguracaoSistema")
class CT01_SingletonTest {

    // ── CT-01 ── Válido ──────────────────────────────────────────────
    @Test
    @DisplayName("CT-01 [V] Duas chamadas a getInstancia() retornam o mesmo objeto")
    void ct01_mesmaInstancia() {
        ConfiguracaoSistema a = ConfiguracaoSistema.getInstancia();
        ConfiguracaoSistema b = ConfiguracaoSistema.getInstancia();
        assertSame(a, b, "getInstancia() deve sempre retornar a mesma referência");
    }

    // ── CT-02 ── Alternativo ─────────────────────────────────────────
    @Test
    @DisplayName("CT-02 [A] Threads concorrentes obtêm a mesma instância (thread-safety)")
    void ct02_threadSafety() throws InterruptedException {
        ConfiguracaoSistema[] resultados = new ConfiguracaoSistema[2];
        Thread t1 = new Thread(() -> resultados[0] = ConfiguracaoSistema.getInstancia());
        Thread t2 = new Thread(() -> resultados[1] = ConfiguracaoSistema.getInstancia());
        t1.start(); t2.start();
        t1.join();  t2.join();
        assertSame(resultados[0], resultados[1], "Ambas as threads devem obter a mesma instância");
    }

    // ── CT-03 ── Válido ──────────────────────────────────────────────
    @Test
    @DisplayName("CT-03 [V] setUrlApi() altera valor e getUrlApi() reflete em qualquer referência")
    void ct03_setUrlApiRefletidaEmTodas() {
        ConfiguracaoSistema cfg1 = ConfiguracaoSistema.getInstancia();
        ConfiguracaoSistema cfg2 = ConfiguracaoSistema.getInstancia();

        String novaUrl = "https://homolog.api.com";
        cfg1.setUrlApi(novaUrl);

        assertEquals(novaUrl, cfg2.getUrlApi(),
            "cfg2 deve refletir a URL alterada via cfg1 — é a mesma instância");
    }
}
