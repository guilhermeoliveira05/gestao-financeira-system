package patterns.observer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
  PADRÃO OBSERVER — Sujeito (Subject).
  - Monitora o status da rede e notifica todos os observadores registrados sempre que houver mudança.
*/

public class Conexao {

    private ConexaoStatus status;
    private LocalDateTime ultimaVerificacao;
    private final List<IObservadorConexao> observadores = new ArrayList<>();

    public Conexao(ConexaoStatus statusInicial) {
        this.status = statusInicial;
        this.ultimaVerificacao = LocalDateTime.now();
    }

    public void adicionarObservador(IObservadorConexao obs) {
        observadores.add(obs);
    }

    public void setStatus(ConexaoStatus novoStatus) {
        this.status = novoStatus;
        this.ultimaVerificacao = LocalDateTime.now();
        notificarObservadores();
    }

    private void notificarObservadores() {
        System.out.println("\n[OBSERVER] Conexão mudou para: " + status
                + " — notificando " + observadores.size() + " observador(es).");
        for (IObservadorConexao obs : observadores) {
            obs.atualizar(status);
        }
    }

    public ConexaoStatus getStatus()             { return status; }
    public LocalDateTime getUltimaVerificacao()  { return ultimaVerificacao; }
}
