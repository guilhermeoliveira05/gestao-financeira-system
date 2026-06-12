package stubs;

import notification.INotificacao;

/*
  STUB — INotificacao
  Registra quantas vezes foi chamado e o último payload recebido.
  Substitui NotificacaoEmail / NotificacaoAPI nos testes sem efeitos colaterais.
 */
public class NotificacaoStub implements INotificacao {

    private int chamadas = 0;
    private String ultimoPayload;

    @Override
    public void enviar(String payload) {
        chamadas++;
        ultimoPayload = payload;
    }

    public int getChamadas()        { return chamadas; }
    public String getUltimoPayload(){ return ultimoPayload; }
    public void reset()             { chamadas = 0; ultimoPayload = null; }
}
