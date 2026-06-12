package notification;

/*
 Contrato para qualquer canal de notificação do sistema.
*/

public interface INotificacao {
    void enviar(String payload);
}
