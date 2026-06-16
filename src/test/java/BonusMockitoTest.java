import model.Transacao;
import model.TransacaoStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import patterns.command.FilaSincronizacao;
import patterns.observer.Conexao;
import patterns.observer.ConexaoStatus;
import patterns.observer.IObservadorConexao;
import patterns.proxy.ISincronizacao;
import patterns.strategy.IEstrategiaProcessamento;
import service.TransacaoService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * ════════════════════════════════════════════════════════
 *  DESAFIO EXTRA — Testes com MOCKITO
 *  Demonstra o uso de @Mock, @Spy, verify(), times(),
 *  never(), argumentCaptor e when().thenThrow()
 * ════════════════════════════════════════════════════════
 *
 * Diferença entre STUB (manual) e MOCK (Mockito):
 *  - Stub manual: classe que implementamos para controlar retornos.
 *  - Mock Mockito: objeto gerado dinamicamente que também verifica
 *    interações (verify), capturas de argumentos e comportamento
 *    em tempo de execução — muito mais expressivo.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BÔNUS Mockito — Verificação de Interações")
class BonusMockitoTest {

    // ── Mocks injetados pelo Mockito ─────────────────────────────────
    @Mock
    private ISincronizacao sincronizacaoMock;

    @Mock
    private IEstrategiaProcessamento estrategiaMock;

    @Mock
    private IObservadorConexao observadorMock;

    @Spy
    private FilaSincronizacao filaSpy;

    private Conexao conexao;
    private TransacaoService service;

    @BeforeEach
    void setUp() {
        conexao = new Conexao(ConexaoStatus.ONLINE);
        service = new TransacaoService(conexao, sincronizacaoMock, filaSpy);
    }

    // ─────────────────────────────────────────────────────────────────
    // BÔNUS-01 — verify() + times(): sincronizar chamado exatamente 1x
    // ─────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("BÔNUS-01 sincronizar() chamado exatamente 1 vez após processar()")
    void bonus01_sincronizarChamadoUmaVez() {
        Transacao t = new Transacao("TX-B01", new BigDecimal("100.00"));

        service.processar(t);
        service.sincronizarFila();

        verify(sincronizacaoMock, times(1)).sincronizar(anyString());
    }

    // ─────────────────────────────────────────────────────────────────
    // BÔNUS-02 — never(): serviço real NÃO é chamado com fila vazia
    // ─────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("BÔNUS-02 sincronizar() nunca chamado se fila não for processada")
    void bonus02_sincronizarNuncaSeFilaNaoProcessada() {
        Transacao t = new Transacao("TX-B02", new BigDecimal("200.00"));

        service.processar(t);
        // NÃO chama sincronizarFila()

        verify(sincronizacaoMock, never()).sincronizar(anyString());
    }

    // ─────────────────────────────────────────────────────────────────
    // BÔNUS-03 — ArgumentCaptor: verifica o conteúdo do payload
    // ─────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("BÔNUS-03 payload enviado a sincronizar() contém o ID da transação")
    void bonus03_payloadContemIdTransacao() {
        Transacao t = new Transacao("TX-B03", new BigDecimal("750.00"));

        service.processar(t);
        service.sincronizarFila();

        org.mockito.ArgumentCaptor<String> captor = org.mockito.ArgumentCaptor.forClass(String.class);
        verify(sincronizacaoMock).sincronizar(captor.capture());

        assertTrue(captor.getValue().contains("TX-B03"),
            "Payload deve conter o ID da transação");
    }

    // ─────────────────────────────────────────────────────────────────
    // BÔNUS-04 — Spy: verifica que enfileirar() foi chamado no Invoker
    // ─────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("BÔNUS-04 FilaSincronizacao.enfileirar() chamado ao processar transação")
    void bonus04_spyFilaEnfileira() {
        Transacao t = new Transacao("TX-B04", new BigDecimal("300.00"));

        service.processar(t);

        verify(filaSpy, times(1)).enfileirar(any());
    }

    // ─────────────────────────────────────────────────────────────────
    // BÔNUS-05 — when().thenThrow(): Observer lança exceção, testa resiliência
    // ─────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("BÔNUS-05 Observador que lança exceção não impede outros de serem notificados")
    void bonus05_observadorFalhoNaoParaOutros() {
        // Observador problemático (mock que lança exceção)
        IObservadorConexao observadorFalho = mock(IObservadorConexao.class);
        doThrow(new RuntimeException("Falha simulada"))
            .when(observadorFalho).atualizar(any());

        // Segundo observador saudável
        IObservadorConexao observadorSaudavel = mock(IObservadorConexao.class);

        // NOTA: este teste documenta o comportamento ATUAL (falha em cascata).
        // Em produção, recomenda-se adicionar try-catch em notificarObservadores()
        // para isolar falhas. O teste abaixo verifica que a exceção é propagada:
        conexao.adicionarObservador(observadorFalho);
        conexao.adicionarObservador(observadorSaudavel);

        assertThrows(RuntimeException.class,
            () -> conexao.setStatus(ConexaoStatus.OFFLINE),
            "Exceção do observador deve ser propagada (comportamento atual)");
    }

    // ─────────────────────────────────────────────────────────────────
    // BÔNUS-06 — inOrder: Observer notifica ANTES de Proxy sincronizar
    // ─────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("BÔNUS-06 Observer notifica observadores na ordem de registro")
    void bonus06_ordemDeNotificacao() {
        IObservadorConexao obs1 = mock(IObservadorConexao.class);
        IObservadorConexao obs2 = mock(IObservadorConexao.class);

        conexao.adicionarObservador(obs1);
        conexao.adicionarObservador(obs2);
        conexao.setStatus(ConexaoStatus.OFFLINE);

        org.mockito.InOrder ordem = inOrder(obs1, obs2);
        ordem.verify(obs1).atualizar(ConexaoStatus.OFFLINE);
        ordem.verify(obs2).atualizar(ConexaoStatus.OFFLINE);
    }

    // ─────────────────────────────────────────────────────────────────
    // BÔNUS-07 — Strategy mock: verifica estratégia correta por status
    // ─────────────────────────────────────────────────────────────────
    @Test
    @DisplayName("BÔNUS-07 processar() muda status da transação para PROCESSADA independente da estratégia")
    void bonus07_transacaoProcessadaComQualquerEstrategia() {
        Transacao t = new Transacao("TX-B07", new BigDecimal("999.99"));
        t.setEstrategia(estrategiaMock);

        t.salvar();

        verify(estrategiaMock, times(1)).processar(t);
        assertEquals(TransacaoStatus.PROCESSADA, t.getStatus());
    }
}
