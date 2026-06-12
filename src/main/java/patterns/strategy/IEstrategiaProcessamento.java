package patterns.strategy;

import model.Transacao;

/*
 PADRÃO STRATEGY — Interface da estratégia.
*/

public interface IEstrategiaProcessamento {
    void processar(Transacao t);
}
