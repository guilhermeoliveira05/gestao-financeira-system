import model.ContaPagar;
import model.Transacao;
import patterns.command.FilaSincronizacao;
import patterns.observer.Conexao;
import patterns.observer.ConexaoStatus;
import patterns.proxy.ProxySincronizacao;
import patterns.singleton.ConfiguracaoSistema;
import service.AlertaVencimentoService;
import service.TransacaoService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

/*

 *  SISTEMA DE GESTÃO FINANCEIRA- Simulação de uso real

 
 *  Regras de negócio simuladas (conforme README):
 
 *  RN-01  Salvamento sem internet
 *        - Usuário tenta salvar dado
 *        - Sistema detecta ausência de conexão
 *        - Dado é persistido localmente
 *        - Operação vai para a fila de sincronização
 *        - Alerta: "Sem internet- dados salvos localmente"
 *        - Ao reconectar, fila é processada automaticamente
 
 *  RN-02  Alerta de Contas a Pagar
 *        - Rotina verifica contas com vencimento em até 3 dias
 *        - Dispara notificação via API externa
 *        - Se API indisponível, registra na fila para reenvio
 
 *  Padrões GoF acionados:
 *   [1] Singleton - ConfiguracaoSistema (URL da API, porta)
 *   [2] Proxy     - ProxySincronizacao  (bloqueia quando offline)
 *   [3] Observer  - Conexao             (detecta queda / retorno)
 *   [4] Strategy  - Online vs Offline   (como salvar a transação)
 *   [5] Command   - FilaSincronizacao   (fila de reenvio pendente)
 */

public class Main {

    static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    static void cabecalho(String txt) {
        System.out.printf(txt);
    }

    static void passo(String numero, String descricao) {
        System.out.printf("%n  - PASSO %s - %s%n", numero, descricao);
        System.out.println("  " + "-".repeat(58));
    }

    static void info(String msg)   { System.out.println("  │  " + msg); }
    static void aviso(String msg)  { System.out.println("  │  (aviso)  " + msg); }
    static void ok(String msg)     { System.out.println("  │  (ok)  " + msg); }
    static void pausa(int ms) throws InterruptedException { Thread.sleep(ms); }

    // ─────────────────────────────────────────────────────────────────
    public static void main(String[] args) throws InterruptedException {

        cabecalho("SISTEMA DE GESTÃO FINANCEIRA -  Simulação Completa");
        System.out.println("  Disciplina: Engenharia de Software");
        System.out.println("  Grupo: Guilherme | Roberto | Rafael | Vitor");

        // ═════════════════════════════════════════════════════════════
        // INICIALIZAÇÃO DO SISTEMA
        // Padrão: [1] SINGLETON- ConfiguracaoSistema
        
        // O sistema carrega as configurações UMA ÚNICA VEZ na inicialização. Qualquer módulo que precisar da URL da API
        // ou da porta usa sempre a mesma instância- nunca duplica.
        // ═════════════════════════════════════════════════════════════
        cabecalho("[1] SINGLETON- Inicialização das configurações");

        passo("1.1", "Carregando ConfiguracaoSistema");
        ConfiguracaoSistema cfg = ConfiguracaoSistema.getInstancia();
        ok("Configuração criada: " + cfg);

        passo("1.2", "Módulos buscam configuração independentemente");
        ConfiguracaoSistema cfgAlerta   = ConfiguracaoSistema.getInstancia();
        ConfiguracaoSistema cfgPagamento = ConfiguracaoSistema.getInstancia();
        ok("Módulo Alerta    usa mesma instância? " + (cfg == cfgAlerta));
        ok("Módulo Pagamento usa mesma instância? " + (cfg == cfgPagamento));
        info("→ Singleton garante consistência: qualquer alteração reflete em todos.");

        passo("1.3", "Equipe de infra atualiza endpoint para produção");
        cfg.setUrlApi("https://api.gestaofinanceira.com.br");
        ok("Módulo Alerta enxerga nova URL? " + cfgAlerta.getUrlApi());
        pausa(400);

        // ═════════════════════════════════════════════════════════════
        // CONFIGURAÇÃO DO MONITOR DE REDE
        // Padrão: [3] OBSERVER- Conexao monitora rede em tempo real
        //         [2] PROXY   - Protege o serviço real de sync
        //
        // O Proxy fica na frente do serviço de sincronização.
        // O Observer (Conexao) notifica o Proxy automaticamente
        // sempre que a rede muda- sem acoplamento direto.
        // ═════════════════════════════════════════════════════════════
        cabecalho("[2+3] PROXY + OBSERVER- Monitor de conectividade");

        passo("2.1", "Registrando Proxy como observador da rede");
        ProxySincronizacao proxy = new ProxySincronizacao();
        Conexao conexao = new Conexao(ConexaoStatus.ONLINE);
        conexao.adicionarObservador(proxy);
        ok("Monitor ativo. Status inicial: " + conexao.getStatus());
        info("→ Observer notificará o Proxy a cada mudança de rede.");
        pausa(400);

        // ═════════════════════════════════════════════════════════════
        // RN-02- ALERTA DE CONTAS A PAGAR
        //
        // Rotina agendada verifica contas com vencimento ≤ 3 dias
        // e dispara notificação via API (Singleton fornece URL).
        // Padrões: [1] Singleton (URL), [5] Command (fila de reenvio)
        // ═════════════════════════════════════════════════════════════
        cabecalho("RN-02- Rotina de Alerta de Contas a Pagar");

        LocalDate hoje = LocalDate.now();
        List<ContaPagar> contas = Arrays.asList(
            new ContaPagar("CP-001", "Aluguel escritório",  3500.00, hoje.plusDays(1)),
            new ContaPagar("CP-002", "Licença JetBrains",    850.00, hoje.plusDays(2)),
            new ContaPagar("CP-003", "Servidor AWS",        1200.00, hoje.plusDays(3)),
            new ContaPagar("CP-004", "Conta de energia",     420.00, hoje.plusDays(10)),
            new ContaPagar("CP-005", "Internet corporativa", 299.00, hoje.minusDays(1)) // vencida
        );

        passo("3.1", "Listando contas cadastradas no sistema");
        System.out.printf("  %-6s  %-28s  %10s  %12s%n", "ID", "Descrição", "Valor", "Vencimento");
        System.out.println("  " + "-".repeat(58));
        for (ContaPagar c : contas) {
            long dias = c.diasParaVencer();
            String status = c.isPaga() ? "PAGA"
                          : dias < 0   ? "VENCIDA"
                          : dias <= 3  ? " URGENTE"
                          : "OK";
            System.out.printf("  %-6s  %-28s  R$%8.2f  %s  [%s]%n",
                    c.getId(), c.getDescricao(), c.getValor(),
                    c.getVencimento().format(FMT), status);
        }

        passo("3.2", "Executando rotina de alerta (rede ONLINE)");
        FilaSincronizacao fila = new FilaSincronizacao();
        AlertaVencimentoService alertaService = new AlertaVencimentoService(proxy, fila);
        alertaService.verificarContas(contas);
        pausa(400);

        // ═════════════════════════════════════════════════════════════
        // RN-01- SALVAMENTO SEM INTERNET
        // Sequência exata do README:
        //   1. Usuário tenta salvar
        //   2. Sistema detecta ausência de conexão  ← Observer
        //   3. Dado é persistido localmente          ← Strategy Offline
        //   4. Operação vai para a fila              ← Command
        //   5. Usuário recebe alerta
        //   6. Ao reconectar, fila processada        ← Observer + Command
        // ═════════════════════════════════════════════════════════════
        cabecalho("RN-01- Salvamento de Transações com Suporte Offline");

        TransacaoService transacaoService = new TransacaoService(conexao, proxy, fila);

        // ── Transações com rede OK ────────────────────────────────
        passo("4.1", "Usuário registra transações (rede ONLINE)");
        Transacao tx1 = new Transacao("TX-001", new BigDecimal("2850.00"));
        info("Usuário cadastrando: Recebimento cliente Alpha- R$ 2.850,00");
        transacaoService.processar(tx1);
        ok("Transação salva e sincronizada diretamente. Status: " + tx1.getStatus());
        pausa(300);

        Transacao tx2 = new Transacao("TX-002", new BigDecimal("430.00"));
        info("Usuário cadastrando: Despesa combustível- R$ 430,00");
        transacaoService.processar(tx2);
        ok("Transação salva e sincronizada diretamente. Status: " + tx2.getStatus());
        pausa(400);

        // ── Queda de rede detectada pelo Observer ─────────────────
        passo("4.2", "Sistema detecta queda de conexão");
        aviso("Sinal de rede perdido- gateway bancário inacessível.");
        conexao.setStatus(ConexaoStatus.OFFLINE);
        // O Observer notifica o Proxy automaticamente ↑
        aviso("Observer notificou o Proxy: sincronização em tempo real BLOQUEADA.");
        pausa(400);

        // ── Transações durante queda- Strategy Offline + Command ─
        passo("4.3", "Usuário continua cadastrando (rede OFFLINE)");

        Transacao tx3 = new Transacao("TX-003", new BigDecimal("1150.00"));
        info("Usuário cadastrando: NF Fornecedor Beta- R$ 1.150,00");
        transacaoService.processar(tx3);
        aviso("Sem internet- dados salvos localmente. [Strategy: Offline]");
        aviso("Operação adicionada à fila de sincronização. [Command]");
        pausa(300);

        Transacao tx4 = new Transacao("TX-004", new BigDecimal("78.50"));
        info("Usuário cadastrando: Estacionamento- R$ 78,50");
        transacaoService.processar(tx4);
        aviso("Sem internet- dados salvos localmente. [Strategy: Offline]");
        aviso("Operação adicionada à fila de sincronização. [Command]");
        pausa(300);

        Transacao tx5 = new Transacao("TX-005", new BigDecimal("9400.00"));
        info("Usuário cadastrando: Pagamento cliente Gamma- R$ 9.400,00");
        transacaoService.processar(tx5);
        aviso("Sem internet- dados salvos localmente. [Strategy: Offline]");
        aviso("Operação adicionada à fila de sincronização. [Command]");
        info("Fila de sincronização acumulada: " + fila.tamanho() + " operação(ões).");
        pausa(400);

        // ── Reconexão detectada pelo Observer ─────────────────────
        passo("4.4", "Conexão restabelecida- Observer detecta e avisa Proxy");
        ok("Sinal de rede restaurado.");
        conexao.setStatus(ConexaoStatus.ONLINE);
        ok("Observer notificou o Proxy: sincronização LIBERADA.");
        pausa(400);

        // ── Command processa fila automaticamente ─────────────────
        passo("4.5", "Fila processada automaticamente ao reconectar [Command]");
        info("Sincronizando " + fila.tamanho() + " operação(ões) pendente(s)...");
        transacaoService.sincronizarFila();
        ok("Todas as operações offline foram enviadas ao servidor.");
        pausa(400);

        // ═════════════════════════════════════════════════════════════
        // RELATÓRIO FINAL
        // ═════════════════════════════════════════════════════════════
        cabecalho("RELATÓRIO FINAL- Fechamento da sessão");

        passo("5.1", "Status das transações");
        System.out.printf("  %-8s  %-38s  %12s  %s%n", "ID", "Descrição", "Valor", "Status");
        System.out.println("  " + "─".repeat(68));
        Object[][] txs = {
            {"TX-001", "Recebimento cliente Alpha",  "R$  2.850,00", tx1.getStatus()},
            {"TX-002", "Despesa combustível",        "R$    430,00", tx2.getStatus()},
            {"TX-003", "NF Fornecedor Beta",         "R$  1.150,00", tx3.getStatus()},
            {"TX-004", "Estacionamento",             "R$     78,50", tx4.getStatus()},
            {"TX-005", "Pagamento cliente Gamma",    "R$  9.400,00", tx5.getStatus()},
        };
        for (Object[] row : txs)
            System.out.printf("  %-8s  %-38s  %-12s  [%s]%n", row[0], row[1], row[2], row[3]);

        passo("5.2", "Padrões GoF acionados nesta sessão");
        info("[1] Singleton - ConfiguracaoSistema: URL única compartilhada entre módulos");
        info("[2] Proxy     - ProxySincronizacao: bloqueou sync durante queda de rede");
        info("[3] Observer  - Conexao: detectou queda (4.2) e retorno (4.4) automaticamente");
        info("[4] Strategy  - Online para TX-001/002; Offline para TX-003/004/005");
        info("[5] Command   - FilaSincronizacao: 3 operações enfileiradas e reenviadas");
        info("");
        info("Regras de negócio exercidas:");
        info("  RN-01  Salvamento sem internet- OK (TX-003, TX-004, TX-005)");
        info("  RN-02  Alerta contas a pagar  - OK (CP-001, CP-002, CP-003 notificadas)");
        info("");
        info("API utilizada: " + ConfiguracaoSistema.getInstancia().getUrlApi());
        info("Fila restante: " + fila.tamanho() + " item(s).");

        System.out.println("Sessão encerrada com sucesso.");
        
    }
}
