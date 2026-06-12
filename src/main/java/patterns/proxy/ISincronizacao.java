package patterns.proxy;

/*
 PADRÃO PROXY — Interface comum entre o Proxy e o serviço real.
*/

public interface ISincronizacao {
    void sincronizar(String payload);
}
