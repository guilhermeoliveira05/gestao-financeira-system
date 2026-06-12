package patterns.command;

import java.util.LinkedList;
import java.util.List;

/*
  PADRÃO COMMAND — Invoker.
  - Acumula comandos e os processa em lote quando solicitado.
*/

public class FilaSincronizacao {

    private final List<IComandoSincronizacao> comandos = new LinkedList<>();

    public void enfileirar(IComandoSincronizacao cmd) {
        comandos.add(cmd);
        System.out.println("[FILA] Comando enfileirado. Total na fila: " + comandos.size());
    }

    public void processarFila() {
        System.out.println("\n[FILA] Processando " + comandos.size() + " comando(s)...");
        for (IComandoSincronizacao cmd : comandos) {
            cmd.executar();
        }
        comandos.clear();
        System.out.println("[FILA] Fila processada e limpa.");
    }

    public int tamanho() { return comandos.size(); }
}
