package notification;

public class NotificacaoEmail implements INotificacao {
    @Override
    public void enviar(String payload) {
        System.out.println("[EMAIL] Notificação enviada: " + payload);
    }
}
