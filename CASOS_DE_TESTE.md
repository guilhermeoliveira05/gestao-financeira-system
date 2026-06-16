# \# Entregável 3 — Estratégia de Testes

# \## Sistema Financeiro com Padrões GoF

# 

# \---

# 

# \## Parte 1 — 15 Casos de Teste (Cenários)

# 

# Legenda de tipo: \*\*V\*\* = Válido | \*\*A\*\* = Alternativo | \*\*E\*\* = Exceção

# 

# \---

# 

# \### CT-01 · Singleton — Unicidade de instância

# 

# | Campo | Detalhe |

# |---|---|

# | \*\*Tipo\*\* | V |

# | \*\*Padrão\*\* | Singleton |

# | \*\*Classe\*\* | `GerenciadorNotificacao` |

# | \*\*Objetivo\*\* | Garantir que duas chamadas a `getInstancia()` retornam o mesmo objeto. |

# | \*\*Pré-cond.\*\* | Nenhuma instância criada anteriormente. |

# | \*\*Entrada\*\* | Duas chamadas sequenciais a `getInstancia()`. |

# | \*\*Ação\*\* | Comparar referências com `==`. |

# | \*\*Resultado esperado\*\* | `instanciaA == instanciaB` → `true`. |

# 

# \---

# 

# \### CT-02 · Singleton — Segurança em ambiente multi-thread

# 

# | Campo | Detalhe |

# |---|---|

# | \*\*Tipo\*\* | A |

# | \*\*Padrão\*\* | Singleton |

# | \*\*Classe\*\* | `GerenciadorNotificacao` |

# | \*\*Objetivo\*\* | Verificar que duas threads concorrentes recebem a mesma instância. |

# | \*\*Pré-cond.\*\* | Nenhuma instância criada anteriormente. |

# | \*\*Entrada\*\* | Duas threads paralelas chamando `getInstancia()` simultaneamente. |

# | \*\*Ação\*\* | Executar threads, aguardar join, comparar referências. |

# | \*\*Resultado esperado\*\* | Ambas as threads obtêm o mesmo objeto. |

# 

# \---

# 

# \### CT-03 · Singleton — Notificação distribui para todos os canais

# 

# | Campo | Detalhe |

# |---|---|

# | \*\*Tipo\*\* | V |

# | \*\*Padrão\*\* | Singleton |

# | \*\*Classe\*\* | `GerenciadorNotificacao` |

# | \*\*Objetivo\*\* | Confirmar que `notificar()` invoca todos os canais registrados. |

# | \*\*Pré-cond.\*\* | Instância criada com 2 canais (Email + API). |

# | \*\*Entrada\*\* | `notificar("TX-001 processada")`. |

# | \*\*Ação\*\* | Substituir canais por stubs contadores; chamar `notificar`. |

# | \*\*Resultado esperado\*\* | Cada stub registra exatamente 1 chamada a `enviar`. |

# 

# \---

# 

# \### CT-04 · Observer — Notificação ao mudar status para OFFLINE

# 

# | Campo | Detalhe |

# |---|---|

# | \*\*Tipo\*\* | V |

# | \*\*Padrão\*\* | Observer |

# | \*\*Classe\*\* | `Conexao` |

# | \*\*Objetivo\*\* | Confirmar que observadores são notificados quando status muda para OFFLINE. |

# | \*\*Pré-cond.\*\* | `Conexao` iniciada como ONLINE com 1 observador registrado. |

# | \*\*Entrada\*\* | `conexao.setStatus(ConexaoStatus.OFFLINE)`. |

# | \*\*Ação\*\* | Verificar que o stub do observador recebeu `OFFLINE`. |

# | \*\*Resultado esperado\*\* | Stub registra 1 chamada com argumento `OFFLINE`. |

# 

# \---

# 

# \### CT-05 · Observer — Múltiplos observadores todos notificados

# 

# | Campo | Detalhe |

# |---|---|

# | \*\*Tipo\*\* | V |

# | \*\*Padrão\*\* | Observer |

# | \*\*Classe\*\* | `Conexao` |

# | \*\*Objetivo\*\* | Verificar que N observadores registrados recebem a notificação. |

# | \*\*Pré-cond.\*\* | `Conexao` com 3 observadores stub registrados. |

# | \*\*Entrada\*\* | `conexao.setStatus(ConexaoStatus.ONLINE)`. |

# | \*\*Ação\*\* | Contar chamadas em cada stub. |

# | \*\*Resultado esperado\*\* | Cada um dos 3 stubs registra 1 chamada. |

# 

# \---

# 

# \### CT-06 · Observer — Sem observadores registrados não lança exceção

# 

# | Campo | Detalhe |

# |---|---|

# | \*\*Tipo\*\* | E |

# | \*\*Padrão\*\* | Observer |

# | \*\*Classe\*\* | `Conexao` |

# | \*\*Objetivo\*\* | Garantir estabilidade quando não há observadores. |

# | \*\*Pré-cond.\*\* | `Conexao` criada sem registrar observadores. |

# | \*\*Entrada\*\* | `conexao.setStatus(ConexaoStatus.OFFLINE)`. |

# | \*\*Ação\*\* | Verificar ausência de exceção. |

# | \*\*Resultado esperado\*\* | Nenhuma exceção lançada. |

# 

# \---

# 

# \### CT-07 · Proxy — Bloqueia sincronização quando OFFLINE

# 

# | Campo | Detalhe |

# |---|---|

# | \*\*Tipo\*\* | V |

# | \*\*Padrão\*\* | Proxy |

# | \*\*Classe\*\* | `ProxySincronizacao` |

# | \*\*Objetivo\*\* | Confirmar que o proxy impede chamadas ao serviço real quando offline. |

# | \*\*Pré-cond.\*\* | Proxy com status OFFLINE. |

# | \*\*Entrada\*\* | `proxy.sincronizar("payload")`. |

# | \*\*Ação\*\* | Verificar que `SincronizacaoReal` \*\*não\*\* foi chamada. |

# | \*\*Resultado esperado\*\* | Stub de `SincronizacaoReal` com 0 chamadas. |

# 

# \---

# 

# \### CT-08 · Proxy — Delega ao serviço real quando ONLINE

# 

# | Campo | Detalhe |

# |---|---|

# | \*\*Tipo\*\* | V |

# | \*\*Padrão\*\* | Proxy |

# | \*\*Classe\*\* | `ProxySincronizacao` |

# | \*\*Objetivo\*\* | Confirmar que o proxy delega quando online. |

# | \*\*Pré-cond.\*\* | Proxy com status ONLINE. |

# | \*\*Entrada\*\* | `proxy.sincronizar("payload")`. |

# | \*\*Ação\*\* | Verificar que `SincronizacaoReal` foi chamada 1 vez. |

# | \*\*Resultado esperado\*\* | Stub de `SincronizacaoReal` com exatamente 1 chamada. |

# 

# \---

# 

# \### CT-09 · Proxy + Observer — Reage automaticamente à mudança de rede

# 

# | Campo | Detalhe |

# |---|---|

# | \*\*Tipo\*\* | A |

# | \*\*Padrão\*\* | Proxy + Observer |

# | \*\*Classe\*\* | `ProxySincronizacao`, `Conexao` |

# | \*\*Objetivo\*\* | Verificar integração: mudança via Observer atualiza o Proxy. |

# | \*\*Pré-cond.\*\* | Proxy registrado como observador da Conexao (ONLINE). |

# | \*\*Entrada\*\* | `conexao.setStatus(OFFLINE)` → `proxy.sincronizar(...)`. |

# | \*\*Ação\*\* | Checar bloqueio após mudança automática. |

# | \*\*Resultado esperado\*\* | Sincronização bloqueada sem chamada explícita ao proxy. |

# 

# \---

# 

# \### CT-10 · Strategy — Usa ProcessamentoOnline quando rede ONLINE

# 

# | Campo | Detalhe |

# |---|---|

# | \*\*Tipo\*\* | V |

# | \*\*Padrão\*\* | Strategy |

# | \*\*Classe\*\* | `TransacaoService` |

# | \*\*Objetivo\*\* | Confirmar escolha de estratégia correta com rede disponível. |

# | \*\*Pré-cond.\*\* | `Conexao` com status ONLINE. |

# | \*\*Entrada\*\* | `service.processar(new Transacao("TX-001", 100.00))`. |

# | \*\*Ação\*\* | Verificar que stub de `ProcessamentoOnline.processar()` foi chamado. |

# | \*\*Resultado esperado\*\* | `ProcessamentoOnline` invocado 1 vez; status da transação = `PROCESSADA`. |

# 

# \---

# 

# \### CT-11 · Strategy — Usa ProcessamentoOffline quando rede OFFLINE

# 

# | Campo | Detalhe |

# |---|---|

# | \*\*Tipo\*\* | A |

# | \*\*Padrão\*\* | Strategy |

# | \*\*Classe\*\* | `TransacaoService` |

# | \*\*Objetivo\*\* | Confirmar escolha de estratégia alternativa sem rede. |

# | \*\*Pré-cond.\*\* | `Conexao` com status OFFLINE. |

# | \*\*Entrada\*\* | `service.processar(new Transacao("TX-002", 200.00))`. |

# | \*\*Ação\*\* | Verificar status da transação após processamento. |

# | \*\*Resultado esperado\*\* | Status = `PROCESSADA`; `ProcessamentoOffline` invocado 1 vez. |

# 

# \---

# 

# \### CT-12 · Strategy — Transação sem estratégia definida não lança exceção

# 

# | Campo | Detalhe |

# |---|---|

# | \*\*Tipo\*\* | E |

# | \*\*Padrão\*\* | Strategy |

# | \*\*Classe\*\* | `Transacao` |

# | \*\*Objetivo\*\* | Garantir comportamento seguro se `setEstrategia` não for chamado. |

# | \*\*Pré-cond.\*\* | Transação criada sem estratégia. |

# | \*\*Entrada\*\* | `transacao.salvar()`. |

# | \*\*Ação\*\* | Chamar `salvar()` direto; verificar ausência de exceção e status mantido. |

# | \*\*Resultado esperado\*\* | Nenhuma exceção; status permanece `PENDENTE`. |

# 

# \---

# 

# \### CT-13 · Command — Fila acumula comandos corretamente

# 

# | Campo | Detalhe |

# |---|---|

# | \*\*Tipo\*\* | V |

# | \*\*Padrão\*\* | Command |

# | \*\*Classe\*\* | `FilaSincronizacao` |

# | \*\*Objetivo\*\* | Verificar contagem de comandos na fila após enfileiramentos. |

# | \*\*Pré-cond.\*\* | Fila vazia. |

# | \*\*Entrada\*\* | 3 chamadas a `enfileirar(cmd)`. |

# | \*\*Ação\*\* | Checar `fila.tamanho()`. |

# | \*\*Resultado esperado\*\* | `tamanho() == 3`. |

# 

# \---

# 

# \### CT-14 · Command — `processarFila` executa e esvazia a fila

# 

# | Campo | Detalhe |

# |---|---|

# | \*\*Tipo\*\* | V |

# | \*\*Padrão\*\* | Command |

# | \*\*Classe\*\* | `FilaSincronizacao` |

# | \*\*Objetivo\*\* | Confirmar que todos os comandos são executados e fila é zerada. |

# | \*\*Pré-cond.\*\* | Fila com 2 comandos stub. |

# | \*\*Entrada\*\* | `fila.processarFila()`. |

# | \*\*Ação\*\* | Contar execuções nos stubs e verificar `tamanho()` após. |

# | \*\*Resultado esperado\*\* | Cada stub executado 1 vez; `tamanho() == 0`. |

# 

# \---

# 

# \### CT-15 · Command — `processarFila` em fila vazia não lança exceção

# 

# | Campo | Detalhe |

# |---|---|

# | \*\*Tipo\*\* | E |

# | \*\*Padrão\*\* | Command |

# | \*\*Classe\*\* | `FilaSincronizacao` |

# | \*\*Objetivo\*\* | Garantir estabilidade ao processar fila sem comandos. |

# | \*\*Pré-cond.\*\* | Fila criada sem nenhum comando enfileirado. |

# | \*\*Entrada\*\* | `fila.processarFila()`. |

# | \*\*Ação\*\* | Verificar ausência de exceção e tamanho zero. |

# | \*\*Resultado esperado\*\* | Nenhuma exceção; `tamanho() == 0`. |

# 

# \---

# 

# \## Resumo por categoria

# 

# | Tipo | Quantidade | Casos |

# |---|---|---|

# | Válido (V) | 9 | CT-01, CT-03, CT-04, CT-05, CT-07, CT-08, CT-10, CT-13, CT-14 |

# | Alternativo (A) | 3 | CT-02, CT-09, CT-11 |

# | Exceção (E) | 3 | CT-06, CT-12, CT-15 |

# | \*\*Total\*\* | \*\*15\*\* | |

# 

# | Padrão | Casos cobertos |

# |---|---|

# | Singleton | CT-01, CT-02, CT-03 |

# | Observer | CT-04, CT-05, CT-06 |

# | Proxy | CT-07, CT-08, CT-09 |

# | Strategy | CT-10, CT-11, CT-12 |

# | Command | CT-13, CT-14, CT-15 |



