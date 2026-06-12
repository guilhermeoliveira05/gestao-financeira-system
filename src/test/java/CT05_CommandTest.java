import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import model.Transacao;
import patterns.command.FilaSincronizacao;
import patterns.command.SincronizarTransacaoCommand;
import stubs.ComandoStub;
import stubs.SincronizacaoStub;

import static org.junit.jupiter.api.Assertions.*;

/*
  Testes do padrão COMMAND — SincronizarTransacaoCommand + FilaSincronizacao
  Cobre: CT-13, CT-14, CT-15
 */
@DisplayName("Command — SincronizarTransacaoCommand e FilaSincronizacao")
class CT05_CommandTest {

    private SincronizacaoStub sincronizacaoStub;
    private FilaSincronizacao fila;

    @BeforeEach
    void setUp() {
        sincronizacaoStub = new SincronizacaoStub();
        fila = new FilaSincronizacao();
    }

    // CT-13  Válido 
    @Test
    @DisplayName("CT-13 [V] executar() aciona o receptor com o payload correto da transação")
    void ct13_commandAcionaReceptor() {
        Transacao t = new Transacao("TX-013", 300.00, "PENDENTE");
        SincronizarTransacaoCommand cmd = new SincronizarTransacaoCommand(t, sincronizacaoStub);

        cmd.executar();

        assertEquals(1, sincronizacaoStub.getChamadas(),
                "executar() deve acionar o receptor exatamente 1 vez");
        assertTrue(sincronizacaoStub.getUltimoPayload().contains("TX-013"),
                "O payload enviado ao receptor deve conter o id da transação");
    }

    // CT-14  Alternativo 
    @Test
    @DisplayName("CT-14 [A] FilaSincronizacao executa todos os comandos enfileirados na ordem")
    void ct14_filaExecutaTodosComandos() {
        ComandoStub cmd1 = new ComandoStub();
        ComandoStub cmd2 = new ComandoStub();
        ComandoStub cmd3 = new ComandoStub();

        fila.adicionarComando(cmd1);
        fila.adicionarComando(cmd2);
        fila.adicionarComando(cmd3);
        fila.executarTodos();

        assertAll("Todos os comandos da fila devem ser executados",
                () -> assertEquals(1, cmd1.getExecucoes(), "cmd1 deve ser executado 1 vez"),
                () -> assertEquals(1, cmd2.getExecucoes(), "cmd2 deve ser executado 1 vez"),
                () -> assertEquals(1, cmd3.getExecucoes(), "cmd3 deve ser executado 1 vez")
        );
    }

    //  CT-15  Exceção 
    @Test
    @DisplayName("CT-15 [E] executarTodos() com fila vazia não lança exceção")
    void ct15_filaVaziaNaoLancaExcecao() {
        assertDoesNotThrow(
                () -> fila.executarTodos(),
                "executarTodos() com fila vazia não deve lançar exceção"
        );
    }
}
