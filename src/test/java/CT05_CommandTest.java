import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import patterns.command.FilaSincronizacao;
import stubs.ComandoStub;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes do padrão COMMAND — FilaSincronizacao (Invoker)
 * Cobre: CT-13, CT-14, CT-15
 */
@DisplayName("Command — FilaSincronizacao")
class CT05_CommandTest {

    private FilaSincronizacao fila;

    @BeforeEach
    void setUp() {
        fila = new FilaSincronizacao();
    }

    // ── CT-13 ── Válido ──────────────────────────────────────────────
    @Test
    @DisplayName("CT-13 [V] Fila acumula o número correto de comandos")
    void ct13_acumulaComandos() {
        ComandoStub c1 = new ComandoStub();
        ComandoStub c2 = new ComandoStub();
        ComandoStub c3 = new ComandoStub();

        fila.enfileirar(c1);
        fila.enfileirar(c2);
        fila.enfileirar(c3);

        assertEquals(3, fila.tamanho(), "Fila deve conter exatamente 3 comandos");
    }

    // ── CT-14 ── Válido ──────────────────────────────────────────────
    @Test
    @DisplayName("CT-14 [V] processarFila() executa todos os comandos e esvazia a fila")
    void ct14_processaEEsvazia() {
        ComandoStub c1 = new ComandoStub();
        ComandoStub c2 = new ComandoStub();

        fila.enfileirar(c1);
        fila.enfileirar(c2);
        fila.processarFila();

        assertAll(
            () -> assertEquals(1, c1.getExecucoes(), "Comando 1 deve ser executado 1 vez"),
            () -> assertEquals(1, c2.getExecucoes(), "Comando 2 deve ser executado 1 vez"),
            () -> assertEquals(0, fila.tamanho(),    "Fila deve estar vazia após processamento")
        );
    }

    // ── CT-15 ── Exceção ─────────────────────────────────────────────
    @Test
    @DisplayName("CT-15 [E] processarFila() em fila vazia não lança exceção")
    void ct15_filaVaziaNaoExplode() {
        assertDoesNotThrow(fila::processarFila,
            "processarFila() em fila vazia não deve lançar exceção");

        assertEquals(0, fila.tamanho(), "Tamanho deve continuar 0");
    }
}
