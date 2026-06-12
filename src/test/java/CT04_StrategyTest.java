import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import model.Transacao;
import patterns.strategy.ProcessamentoOnline;
import patterns.strategy.ProcessamentoOffline;
import stubs.EstrategiaStub;

import static org.junit.jupiter.api.Assertions.*;

/*
  Testes do padrão STRATEGY — ProcessamentoOnline / ProcessamentoOffline
  Cobre: CT-10, CT-11, CT-12
 */
@DisplayName("Strategy — Estratégias de Processamento")
class CT04_StrategyTest {

    //  CT-10  Válido 
    @Test
    @DisplayName("CT-10 [V] EstrategiaStub registra corretamente a transação processada")
    void ct10_stubRegistraTransacao() {
        EstrategiaStub stub = new EstrategiaStub();
        Transacao t = new Transacao("TX-010", 150.00, "PENDENTE");

        stub.processar(t);

        assertEquals(1, stub.getChamadas(),
                "processar() deve contabilizar exatamente 1 chamada");
        assertSame(t, stub.getUltimaTransacao(),
                "A transação registrada deve ser a mesma instância passada");
    }

    // CT-11  Alternativo 
    @Test
    @DisplayName("CT-11 [A] ProcessamentoOnline executa sem lançar exceção com transação válida")
    void ct11_onlineNaoLancaExcecao() {
        ProcessamentoOnline online = new ProcessamentoOnline();
        Transacao t = new Transacao("TX-011", 200.00, "APROVADA");

        assertDoesNotThrow(
                () -> online.processar(t),
                "ProcessamentoOnline não deve lançar exceção com transação válida"
        );
    }

    // CT-12  Exceção 
    @Test
    @DisplayName("CT-12 [E] ProcessamentoOffline executa sem lançar exceção com transação válida")
    void ct12_offlineNaoLancaExcecao() {
        ProcessamentoOffline offline = new ProcessamentoOffline();
        Transacao t = new Transacao("TX-012", 75.50, "PENDENTE");

        assertDoesNotThrow(
                () -> offline.processar(t),
                "ProcessamentoOffline não deve lançar exceção com transação válida"
        );
    }
}
