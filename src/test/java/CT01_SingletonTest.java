import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import patterns.singleton.GerenciadorNotificacao;

import static org.junit.jupiter.api.Assertions.*;

/*
  Testes do padrão SINGLETON — GerenciadorNotificacao
  Cobre: CT-01, CT-02, CT-03
 */
@DisplayName("Singleton — GerenciadorNotificacao")
class CT01_SingletonTest {

    // CT-01  Válido 
    @Test
    @DisplayName("CT-01 [V] Duas chamadas a getInstancia() retornam o mesmo objeto")
    void ct01_mesmInstancia() {
        GerenciadorNotificacao a = GerenciadorNotificacao.getInstancia();
        GerenciadorNotificacao b = GerenciadorNotificacao.getInstancia();

        assertSame(a, b, "getInstancia() deve sempre retornar a mesma referência");
    }

    //  CT-02  Alternativo 
    @Test
    @DisplayName("CT-02 [A] Threads concorrentes obtêm a mesma instância (thread-safety)")
    void ct02_threadSafety() throws InterruptedException {
        GerenciadorNotificacao[] resultados = new GerenciadorNotificacao[2];

        Thread t1 = new Thread(() -> resultados[0] = GerenciadorNotificacao.getInstancia());
        Thread t2 = new Thread(() -> resultados[1] = GerenciadorNotificacao.getInstancia());

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        assertNotNull(resultados[0], "Thread 1 não deve retornar null");
        assertNotNull(resultados[1], "Thread 2 não deve retornar null");
        assertSame(resultados[0], resultados[1], "Ambas as threads devem obter a mesma instância");
    }

    // CT-03  Válido 
    @Test
    @DisplayName("CT-03 [V] notificar() não lança exceção com payload válido")
    void ct03_notificarSemExcecao() {
        GerenciadorNotificacao gn = GerenciadorNotificacao.getInstancia();
        // Verifica que o método executa sem lançar nenhuma exceção
        assertDoesNotThrow(
            () -> gn.notificar("TX-001 processada"),
            "notificar() não deve lançar exceção com payload válido"
        );
    }
}
