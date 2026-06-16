import org.junit.jupiter.api.Test;
import patterns.singleton.ConfiguracaoSistema;

import static org.junit.jupiter.api.Assertions.*;

class SingletonTest {

    @Test
    void deveRetornarMesmaInstancia() {
        ConfiguracaoSistema a = ConfiguracaoSistema.getInstancia();
        ConfiguracaoSistema b = ConfiguracaoSistema.getInstancia();
        assertSame(a, b);
    }

    @Test
    void deveSerThreadSafe() throws InterruptedException {
        ConfiguracaoSistema[] resultados = new ConfiguracaoSistema[2];
        Thread t1 = new Thread(() -> resultados[0] = ConfiguracaoSistema.getInstancia());
        Thread t2 = new Thread(() -> resultados[1] = ConfiguracaoSistema.getInstancia());
        t1.start(); t2.start();
        t1.join();  t2.join();
        assertSame(resultados[0], resultados[1]);
    }
}
