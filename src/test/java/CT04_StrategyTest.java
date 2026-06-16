import model.Transacao;
import model.TransacaoStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import stubs.EstrategiaStub;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes do padrão STRATEGY — IEstrategiaProcessamento + Transacao
 * Cobre: CT-10, CT-11, CT-12
 */
@DisplayName("Strategy — IEstrategiaProcessamento")
class CT04_StrategyTest {

    // ── CT-10 ── Válido ──────────────────────────────────────────────
    @Test
    @DisplayName("CT-10 [V] ProcessamentoOnline é invocado e transação fica PROCESSADA")
    void ct10_processamentoOnline() {
        EstrategiaStub stub = new EstrategiaStub();
        Transacao t = new Transacao("TX-010", new BigDecimal("1500.00"));
        t.setEstrategia(stub);

        t.salvar();

        assertAll(
            () -> assertEquals(1, stub.getChamadas(),
                "ProcessamentoOnline deve ser chamado 1 vez"),
            () -> assertEquals(TransacaoStatus.PROCESSADA, t.getStatus(),
                "Status deve ser PROCESSADA após salvar()"),
            () -> assertSame(t, stub.getUltimaTransacao(),
                "Estratégia deve receber a própria transação")
        );
    }

    // ── CT-11 ── Alternativo ─────────────────────────────────────────
    @Test
    @DisplayName("CT-11 [A] ProcessamentoOffline é invocado e transação fica PROCESSADA")
    void ct11_processamentoOffline() {
        EstrategiaStub stub = new EstrategiaStub();
        Transacao t = new Transacao("TX-011", new BigDecimal("320.50"));
        t.setEstrategia(stub);   // simula atribuição da estratégia offline

        t.salvar();

        assertAll(
            () -> assertEquals(1, stub.getChamadas(),
                "ProcessamentoOffline deve ser chamado 1 vez"),
            () -> assertEquals(TransacaoStatus.PROCESSADA, t.getStatus(),
                "Transação offline também deve ficar PROCESSADA")
        );
    }

    // ── CT-12 ── Exceção ─────────────────────────────────────────────
    @Test
    @DisplayName("CT-12 [E] salvar() sem estratégia definida não lança exceção e mantém status PENDENTE")
    void ct12_semEstrategiaNaoExplode() {
        Transacao t = new Transacao("TX-012", new BigDecimal("50.00"));
        // Nenhuma estratégia definida

        assertDoesNotThrow(t::salvar,
            "salvar() sem estratégia não deve lançar exceção");

        assertEquals(TransacaoStatus.PENDENTE, t.getStatus(),
            "Status deve permanecer PENDENTE se não houver estratégia");
    }
}
