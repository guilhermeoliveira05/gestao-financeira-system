import model.Transacao;
import patterns.command.FilaSincronizacao;
import patterns.observer.Conexao;
import patterns.observer.ConexaoStatus;
import patterns.proxy.ProxySincronizacao;
import patterns.singleton.GerenciadorNotificacao;
import service.TransacaoService;

import java.math.BigDecimal;

/*
 Ponto de entrada do Sistema Financeiro.
 Demonstra a integração dos 5 padrões GoF em um único fluxo.
*/

public class Main {

    public static void main(String[] args) {


        System.out.println("SISTEMA FINANCEIRO — PADRÕES GoF em JAVA");


        // ── 1. SINGLETON ─────────────────────────────────────────────
        System.out.println("[1] SINGLETON: Gerenciador de Notificações");
        GerenciadorNotificacao gn1 = GerenciadorNotificacao.getInstancia();
        GerenciadorNotificacao gn2 = GerenciadorNotificacao.getInstancia();
        System.out.println("    Mesma instância? " + (gn1 == gn2));
        System.out.println("───────────────────────────────────────────────\n");


        // ── 2. OBSERVER + PROXY ───────────────────────────────────────
        System.out.println("[2] PROXY + [3] OBSERVER: Rede e Acesso");
        ProxySincronizacao proxy = new ProxySincronizacao();
        Conexao conexao = new Conexao(ConexaoStatus.ONLINE);
        conexao.adicionarObservador(proxy);
        System.out.println("    Proxy registrado como observador da Conexao.");
        System.out.println("───────────────────────────────────────────────\n");

        // ── SERVICE orquestra Strategy + Command ──────────────────────
        FilaSincronizacao fila = new FilaSincronizacao();
        TransacaoService service = new TransacaoService(conexao, proxy, fila);

        // ── STRATEGY (rede ONLINE) ─────────────────────────────────────
        System.out.println("[4] STRATEGY: Transação com rede ONLINE");
        Transacao t1 = new Transacao("TX-001", new BigDecimal("1500.00"));
        service.processar(t1);
        System.out.println("───────────────────────────────────────────────\n");

        // ── OBSERVER: simula queda de rede ────────────────────────────
        System.out.println("Simulando queda de rede...");
        conexao.setStatus(ConexaoStatus.OFFLINE);
        System.out.println("───────────────────────────────────────────────\n");

        // ── STRATEGY (rede OFFLINE) ────────────────────────────────────
        System.out.println("[4] STRATEGY: Transação com rede OFFLINE");
        Transacao t2 = new Transacao("TX-002", new BigDecimal("320.50"));
        service.processar(t2);
        System.out.println("───────────────────────────────────────────────\n");

        // ── OBSERVER: rede restaurada ──────────────────────────────────
        System.out.println("Rede restaurada...");
        conexao.setStatus(ConexaoStatus.ONLINE);
        System.out.println("───────────────────────────────────────────────\n");

        // ── COMMAND: processa fila pendente ───────────────────────────
        System.out.println("[5] COMMAND: Processando fila de sincronização");
        service.sincronizarFila();
        System.out.println("───────────────────────────────────────────────\n");


        System.out.println("             FIM DA DEMONSTRAÇÃO              ");
    }
}
