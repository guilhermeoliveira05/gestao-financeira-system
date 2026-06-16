import model.Transacao;
import model.TransacaoStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import patterns.command.FilaSincronizacao;
import patterns.observer.Conexao;
import patterns.observer.ConexaoStatus;
import patterns.proxy.ProxySincronizacao;
import service.TransacaoService;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class TransacaoServiceTest {

    private Conexao conexao;
    private ProxySincronizacao proxy;
    private FilaSincronizacao fila;
    private TransacaoService service;

    @BeforeEach
    void setUp() {
        conexao = new Conexao(ConexaoStatus.ONLINE);
        proxy   = new ProxySincronizacao();
        fila    = new FilaSincronizacao();
        conexao.adicionarObservador(proxy);
        service = new TransacaoService(conexao, proxy, fila);
    }

    @Test
    void deveProcessarTransacaoOnlineComSucesso() {
        Transacao t = new Transacao("TX-001", new BigDecimal("100.00"));
        service.processar(t);
        assertEquals(TransacaoStatus.PROCESSADA, t.getStatus());
    }

    @Test
    void deveProcessarTransacaoOfflineComSucesso() {
        conexao.setStatus(ConexaoStatus.OFFLINE);
        Transacao t = new Transacao("TX-002", new BigDecimal("200.00"));
        service.processar(t);
        assertEquals(TransacaoStatus.PROCESSADA, t.getStatus());
    }

    @Test
    void deveEnfileirarComandoAposProcessar() {
        Transacao t = new Transacao("TX-003", new BigDecimal("50.00"));
        service.processar(t);
        assertEquals(1, fila.tamanho());
    }

    @Test
    void deveEsvaziarFilaAposSincronizacao() {
        Transacao t = new Transacao("TX-004", new BigDecimal("75.00"));
        service.processar(t);
        service.sincronizarFila();
        assertEquals(0, fila.tamanho());
    }
}
