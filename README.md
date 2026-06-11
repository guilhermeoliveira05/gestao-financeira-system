# 📊 Sistema de Transcrição de Fluxo e Regras de Negócio

> Projeto desenvolvido para a disciplina de Engenharia de Software — Partes 1 e 2.

---

## 📌 Sobre o Projeto

Sistema de **gestão financeira com suporte a modo offline**, capaz de:

- Salvar dados localmente quando sem conexão à internet
- Notificar o usuário sobre o estado da conectividade em tempo real
- Alertar sobre **contas a pagar** próximas do vencimento via API
- Sincronizar automaticamente os dados ao restabelecer conexão

> O foco do projeto está em **arquitetura, padrões de projeto, testes e qualidade de software** — não em interface gráfica ou banco de dados.

---

## 👥 Integrantes

| Nome | GitHub |
|------|--------|
| Integrante 1 | [Guilherme Augusto](https://github.com/guilhermeoliveira05) |
| Integrante 2 | [Roberto Neto](https://github.com/neto067) |
| Integrante 3 | [@usuario3](https://github.com/) |
| Integrante 4 | [@usuario4](https://github.com/) |

---

## 📁 Estrutura do Repositório

```
📦 projeto-engenharia-software
 ┣ 📂 docs/
 ┃ ┣ 📄 documentacao_parte1.docx       # Documentação da Parte 1
 ┃ ┣ 📄 documento_arquitetural.docx    # Padrões GoF aplicados
 ┃ ┣ 📄 casos_de_teste.docx            # 15 casos de teste estruturados
 ┃ ┗ 📂 diagramas/
 ┃   ┣ 🖼️ diagrama_classes_v1.png      # Versão original
 ┃   ┣ 🖼️ diagrama_classes_v2.png      # Versão evoluída com padrões GoF
 ┃   ┣ 🖼️ diagrama_casos_uso.png
 ┃   ┗ 🖼️ diagrama_sequencia.png
 ┣ 📂 src/
 ┃ ┣ 📂 main/java/
 ┃ ┃ ┣ 📂 model/                       # Entidades do domínio
 ┃ ┃ ┣ 📂 service/                     # Regras de negócio
 ┃ ┃ ┣ 📂 patterns/                    # Implementação dos padrões GoF
 ┃ ┃ ┗ 📂 notification/               # Integração com API de notificações
 ┃ ┗ 📂 test/java/                     # Testes unitários JUnit
 ┗ 📄 README.md
```

---

## 🧩 Parte 1 — Entregáveis

- ✅ Descrição do Problema
- ✅ Planejamento do Projeto (PMBOK)
- ✅ Diagrama de Casos de Uso
- ✅ Diagrama de Classes (versão inicial)
- ✅ Diagrama de Sequência
- ✅ Matriz de Requisitos (Setor | Requisito | Atributo | Domínio | Tempo | Banco)

---

## 🚀 Parte 2 — Entregáveis

### 1. Evolução do Diagrama de Classes

Versão evoluída com a aplicação de **5 Padrões de Projeto GoF**:

| # | Padrão | Categoria | Onde foi aplicado |
|---|--------|-----------|-------------------|
| 1 | Factory Method | Criacional | Criação de tipos de notificação (API, local, e-mail) |
| 2 | Proxy | Estrutural | Controle de acesso ao serviço de sincronização online |
| 3 | Observer | Comportamental | Monitoramento do estado de conexão (online/offline) |
| 4 | Strategy | Comportamental | Estratégia de salvamento (online vs offline) |
| 5 | Command | Comportamental | Fila de operações pendentes para sincronização |

---

### 2. Documento Arquitetural

Para cada padrão aplicado, o documento descreve:
- ✔️ Problema identificado no sistema
- ✔️ Solução proposta pelo padrão
- ✔️ Justificativa técnica
- ✔️ Impacto arquitetural
- ✔️ Vantagens e limitações
- ✔️ Trecho do diagrama de classes evidenciando o padrão

📄 Disponível em: `docs/documento_arquitetural.docx`

---

### 3. Estratégia de Testes

#### Casos de Teste (15 estruturados)

Contemplam cenários:
- ✅ **Válidos** — fluxo esperado funcionando corretamente
- ⚠️ **Alternativos** — comportamentos fora do fluxo principal
- ❌ **De exceção** — falhas, dados inválidos e indisponibilidade de serviços

#### Testes Unitários JUnit (15 testes)

- Utilizando **stubs** para simulação de dependências
- Cobertura das principais regras de negócio
- **DESAFIO (nota extra):** uso de **Mockito** para simulação de comportamentos dinâmicos

📄 Disponível em: `docs/casos_de_teste.docx`  
💻 Código em: `src/test/java/`

---

### 4. Estratégia de Versionamento (Git)

#### Branches adotadas

```
main          → versão estável / entregável
develop       → integração contínua do grupo
feature/xxx   → cada integrante desenvolve sua parte
```

**Exemplo de fluxo:**
```
feature/diagrama-classes → develop → main
feature/testes-junit     → develop → main
feature/documento-arq    → develop → main
feature/javadoc          → develop → main
```

#### Padrão de Commits (Conventional Commits)

```
feat:     nova funcionalidade
fix:      correção de bug
docs:     alteração em documentação
test:     adição ou correção de testes
refactor: refatoração de código
chore:    tarefas auxiliares (config, dependências)
```

**Exemplos reais:**
```bash
git commit -m "feat: implementa padrão Observer para monitorar conexão"
git commit -m "test: adiciona casos de teste para salvamento offline"
git commit -m "docs: atualiza diagrama de classes com padrão Proxy"
git commit -m "refactor: aplica Factory Method na criação de notificações"
```

---

### 5. Documentação Técnica (JavaDoc)

Todos os protótipos documentados com JavaDoc incluindo:
- Descrição das classes
- Descrição dos métodos
- Parâmetros (`@param`)
- Retornos (`@return`)
- Exceções (`@throws`)
- Observações relevantes (`@see`, `@since`)

---

## ✅ Definition of Done

Uma funcionalidade é considerada **concluída** quando atende todos os critérios:

- [ ] Modelagem atualizada no diagrama de classes
- [ ] Caso de teste correspondente elaborado
- [ ] Documentação JavaDoc escrita
- [ ] Evidência da aplicação do padrão GoF registrada

---

## 🔧 Tecnologias Utilizadas

- **Java** — linguagem principal dos protótipos
- **JUnit 5** — testes unitários
- **Mockito** — simulação de comportamentos (desafio)
- **Git / GitHub** — versionamento
- **draw.io / PlantUML** — diagramas UML
- **JavaDoc** — documentação técnica

---

## 📋 Regras de Negócio Principais

### Salvamento sem Internet
1. Usuário tenta salvar um dado
2. Sistema detecta ausência de conexão
3. Dado é persistido **localmente** (planilha/arquivo)
4. Operação é adicionada à **fila de sincronização**
5. Usuário recebe alerta: *"Sem internet — dados salvos localmente"*
6. Ao reconectar, fila é processada automaticamente

### Alerta de Contas a Pagar
1. Rotina agendada verifica contas com vencimento em até **3 dias**
2. Para cada conta pendente, dispara notificação via **API externa**
3. Se API indisponível, registra na fila para reenvio posterior

---



---


