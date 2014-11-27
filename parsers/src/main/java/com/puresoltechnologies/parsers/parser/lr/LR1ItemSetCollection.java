package com.puresoltechnologies.parsers.parser.lr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.parsers.grammar.Grammar;
import com.puresoltechnologies.parsers.grammar.GrammarException;
import com.puresoltechnologies.parsers.grammar.production.Construction;
import com.puresoltechnologies.parsers.grammar.production.FinishTerminal;
import com.puresoltechnologies.parsers.parser.functions.Closure1;
import com.puresoltechnologies.parsers.parser.functions.Goto1;
import com.puresoltechnologies.parsers.parser.items.LR1Item;
import com.puresoltechnologies.parsers.parser.items.LR1ItemSet;

public class LR1ItemSetCollection implements Serializable {

    private static final long serialVersionUID = -1330346621768260912L;

    private final static Logger logger = LoggerFactory
	    .getLogger(LR1ItemSetCollection.class);

    private final Map<LR1ItemSet, Integer> set2int = new HashMap<LR1ItemSet, Integer>();
    private final List<LR1ItemSet> itemSetCollection = new ArrayList<LR1ItemSet>();

    private final Grammar grammar;
    private final Closure1 closure1;
    private final Goto1 goto1;

    public LR1ItemSetCollection(Grammar grammar, Closure1 closure1, Goto1 goto1)
	    throws GrammarException {
	super();
	this.grammar = grammar;
	this.closure1 = closure1;
	this.goto1 = goto1;
	calculate();
    }

    /**
     * From Dragon Book:
     * 
     * <pre>
     *   void items(G') {
     *   	C = {CLOSURE({[S' --> .S, $]})};
     *   	repeat
     *   		for ( jede Item-Menge I in C )
     *  			for ( jedes Grammatiksymbol X )
     *  				if ( GOTO( I, X) ist nicht leer und nicht in C )
     *  					fuege GOTO(I, X) zu C hinzu;
     *   	until es werden keine neuen Item-Mengen mehr in einer Runde zu C hinzugefuegt.
     *   }
     * </pre>
     * 
     * This method was extended to save also all transitions found and to handle
     * ambiguous grammars.
     * 
     * @throws GrammarException
     */
    private void calculate() throws GrammarException {
	addState(closure1.calc(new LR1Item(grammar.getProductions().get(0), 0,
		FinishTerminal.getInstance())));
	int run = 0;
	int nextStartPosition = 0;
	int currentStartPosition = 0;
	do {
	    run++;
	    currentStartPosition = nextStartPosition;
	    nextStartPosition = itemSetCollection.size();
	    int currentItemSetCount = itemSetCollection.size();
	    for (int stateId = currentStartPosition; stateId < currentItemSetCount; stateId++) {
		logger.trace("state: " + stateId + "/"
			+ itemSetCollection.size() + " run: " + run);
		LR1ItemSet itemSet = itemSetCollection.get(stateId);
		for (Construction grammarSymbol : itemSet
			.getAllGrammarSymbols()) {
		    LR1ItemSet gotoSet = goto1.calc(itemSet, grammarSymbol);
		    if (gotoSet.getSize() > 0) {
			addState(gotoSet);
		    }
		}
	    }
	} while (nextStartPosition < itemSetCollection.size());
    }

    private void addState(LR1ItemSet itemSet) {
	if (!itemSetCollection.contains(itemSet)) {
	    set2int.put(itemSet, itemSetCollection.size());
	    itemSetCollection.add(itemSet);
	}
    }

    public LR1ItemSet getItemSet(int stateId) {
	return itemSetCollection.get(stateId);
    }

    /**
     * This method looks for the state id for a given item set.
     * 
     * @param targetSet
     *            is the target set to look for.
     * @return The id is returned as integer.
     * @throws GrammarException
     *             is thrown in case the grammar does not contain the target
     *             set.
     */
    public int getStateId(LR1ItemSet targetSet) throws GrammarException {
	Integer id = set2int.get(targetSet);
	if (id == null) {
	    throw new GrammarException("Target set '" + targetSet
		    + "' was not found!");
	}
	return id;
    }

    public int getStateNumber() {
	return itemSetCollection.size();
    }

    public Grammar getGrammar() {
	return grammar;
    }

    @Override
    public String toString() {
	StringBuffer buffer = new StringBuffer();
	// for (int stateId = 0; stateId < itemSetCollection.size(); stateId++)
	// {
	// buffer.append("===========\n");
	// buffer.append("State " + stateId + ":\n");
	// buffer.append("===========\n");
	// LR1ItemSet itemSet = itemSetCollection.get(stateId);
	// buffer.append(itemSet.toString());
	// buffer.append("\n");
	// }
	return buffer.toString();
    }
}
