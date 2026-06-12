package notification;

public class NotificacaoAPI implements INotificacao {
    @Override
    public void enviar(String payload) {
        System.out.println("[API] Notificação enviada: " + payload);
    }
}
