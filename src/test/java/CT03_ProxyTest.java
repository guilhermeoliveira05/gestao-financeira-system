import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import patterns.proxy.ProxySincronizacao;
import stubs.SincronizacaoStub;

import static org.junit.jupiter.api.Assertions.*;

/*
  Testes do padrão PROXY — ProxySincronizacao
  Cobre: CT-07, CT-08, CT-09
 */
@DisplayName("Proxy — ProxySincronizacao")
class CT03_ProxyTest {

    private SincronizacaoStub stub;
    private ProxySincronizacao proxy;

    @BeforeEach
    void setUp() {
        stub  = new SincronizacaoStub();
        proxy = new ProxySincronizacao(stub);
    }

    //  CT-07  Válido 
    @Test
    @DisplayName("CT-07 [V] sincronizar() delega a chamada ao serviço real")
    void ct07_delegaAoServico() {
        proxy.sincronizar("TX-001");

        assertEquals(1, stub.getChamadas(),
                "O proxy deve delegar exatamente 1 chamada ao receptor");
        assertEquals("TX-001", stub.getUltimoPayload(),
                "O payload deve ser repassado intacto ao receptor");
    }

    // CT-08  Alternativo 
    @Test
    @DisplayName("CT-08 [A] Múltiplas chamadas acumulam corretamente no receptor")
    void ct08_multiplasChamadas() {
        proxy.sincronizar("TX-001");
        proxy.sincronizar("TX-002");
        proxy.sincronizar("TX-003");

        assertEquals(3, stub.getChamadas(),
                "Três chamadas ao proxy devem gerar três delegações ao receptor");
        assertEquals("TX-003", stub.getUltimoPayload(),
                "O último payload registrado deve ser o da terceira chamada");
    }

    //  CT-09  Exceção 
    @Test
    @DisplayName("CT-09 [E] sincronizar() com payload nulo não lança exceção no proxy")
    void ct09_payloadNulo() {
        assertDoesNotThrow(
                () -> proxy.sincronizar(null),
                "O proxy não deve lançar exceção ao receber payload nulo"
        );
        assertEquals(1, stub.getChamadas(),
                "A delegação deve ocorrer mesmo com payload nulo");
    }
}
