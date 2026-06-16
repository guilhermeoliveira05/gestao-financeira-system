package service;

import model.ContaPagar;
import patterns.command.FilaSincronizacao;
import patterns.command.IComandoSincronizacao;
import patterns.proxy.ISincronizacao;

import java.util.List;

/*
  Regra de negócio: Alerta de Contas a Pagar.
 
  Rotina que verifica contas com vencimento em até 3 dias e dispara notificação via API externa. 
  Se o proxy estiver offline, o comando é enfileirado para reenvio posterior (Command).
*/

public class AlertaVencimentoService {

    private static final int DIAS_LIMITE = 3;

    private final ISincronizacao  sincronizacao;
    private final FilaSincronizacao fila;

    public AlertaVencimentoService(ISincronizacao sincronizacao, FilaSincronizacao fila) {
        this.sincronizacao = sincronizacao;
        this.fila          = fila;
    }

    /*
      Verifica cada conta e, se vence em até DIAS_LIMITE dias, tenta notificar via API. Caso o proxy bloqueie (offline), enfileira o comando para reenvio.
    */
   
    public void verificarContas(List<ContaPagar> contas) {
        System.out.println("  [ALERTA-SERVICE] Verificando " + contas.size() + " conta(s)...");

        for (ContaPagar conta : contas) {
            if (conta.isPaga()) continue;

            long dias = conta.diasParaVencer();

            if (dias < 0) {
                System.out.printf("  [ALERTA-SERVICE] !! VENCIDA: '%s' venceu há %d dia(s)%n",
                        conta.getDescricao(), Math.abs(dias));
                continue;
            }

            if (dias <= DIAS_LIMITE) {
                System.out.printf("  [ALERTA-SERVICE] Conta '%s' vence em %d dia(s) — disparando alerta...%n",
                        conta.getDescricao(), dias);

                // Tenta notificar; se proxy offline → command vai para fila
                IComandoSincronizacao cmd = () -> {
                    conta.notificarVencimento();
                    sincronizacao.sincronizar(
                        "ALERTA|" + conta.getId() + "|" + conta.getDescricao() + "|" + conta.getVencimento()
                    );
                };

                // Executa direto (o proxy decide se passa ou enfileira)
                try {
                    conta.notificarVencimento();
                    sincronizacao.sincronizar(
                        "ALERTA|" + conta.getId() + "|" + conta.getDescricao()
                    );
                } catch (Exception e) {
                    System.out.println("  [ALERTA-SERVICE] API indisponível — enfileirando para reenvio.");
                    fila.enfileirar(cmd);
                }
            } else {
                System.out.printf("  [ALERTA-SERVICE] OK: '%s' vence em %d dia(s) — sem urgência.%n",
                        conta.getDescricao(), dias);
            }
        }
    }
}
