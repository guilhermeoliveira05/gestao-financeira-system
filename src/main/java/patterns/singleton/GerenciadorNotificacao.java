package patterns.singleton;

import notification.INotificacao;
import notification.NotificacaoAPI;
import notification.NotificacaoEmail;

import java.util.ArrayList;
import java.util.List;

/*
 PADRÃO SINGLETON
 - Garante uma única instância do gerenciador de notificações em todo o sistema.
 - Thread-safe com double-checked locking.
*/

public class GerenciadorNotificacao {

    private static volatile GerenciadorNotificacao instancia;
    private final List<INotificacao> canais = new ArrayList<>();

    private GerenciadorNotificacao() {
        canais.add(new NotificacaoEmail());
        canais.add(new NotificacaoAPI());
    }

    public static GerenciadorNotificacao getInstancia() {
        if (instancia == null) {
            synchronized (GerenciadorNotificacao.class) {
                if (instancia == null) {
                    instancia = new GerenciadorNotificacao();
                }
            }
        }
        return instancia;
    }

    public void notificar(String payload) {
        System.out.println("\n[SINGLETON] GerenciadorNotificacao — enviando para " + canais.size() + " canal(is):");
        for (INotificacao canal : canais) {
            canal.enviar(payload);
        }
    }
}
