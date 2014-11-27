package com.puresoltechnologies.parsers.parser.lr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.puresoltechnologies.parsers.grammar.Grammar;
import com.puresoltechnologies.parsers.grammar.GrammarException;
import com.puresoltechnologies.parsers.grammar.production.Construction;
import com.puresoltechnologies.parsers.parser.functions.Closure0;
import com.puresoltechnologies.parsers.parser.functions.Goto0;
import com.puresoltechnologies.parsers.parser.items.LR0Item;
import com.puresoltechnologies.parsers.parser.items.LR0ItemSet;

public class LR0ItemSetCollection implements Serializable {

    private static final long serialVersionUID = -5320832167468349031L;

    private final List<LR0ItemSet> itemSetCollection = new ArrayList<LR0ItemSet>();

    private final Grammar grammar;
    private final Closure0 closure0;
    private final Goto0 goto0;

    public LR0ItemSetCollection(Grammar grammar, Closure0 closure0, Goto0 goto0)
	    throws GrammarException {
	super();
	this.grammar = grammar;
	this.closure0 = closure0;
	this.goto0 = goto0;
	calculate();
    }

    /**
     * From Dragon Book:
     * 
     * <pre>
     *   void items(G') {
     *   	C = {CLOSURE({[S0 --> .S]})};
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
	addState(closure0.calc(new LR0Item(grammar.getProductions().get(0), 0)));
	int currentSize;
	do {
	    currentSize = itemSetCollection.size();
	    int currentItemSetCount = itemSetCollection.size();
	    for (int stateId = 0; stateId < currentItemSetCount; stateId++) {
		LR0ItemSet itemSet = itemSetCollection.get(stateId);
		for (Construction grammarSymbol : itemSet
			.getAllGrammarSymbols()) {
		    LR0ItemSet gotoSet = goto0.calc(itemSet, grammarSymbol);
		    if (gotoSet.getSize() > 0) {
			addState(gotoSet);
		    }
		}
	    }
	} while (currentSize < itemSetCollection.size());
    }

    private void addState(LR0ItemSet itemSet) {
	if (!itemSetCollection.contains(itemSet)) {
	    itemSetCollection.add(itemSet);
	}
    }

    public LR0ItemSet getItemSet(int stateId) {
	return (LR0ItemSet) itemSetCollection.toArray()[stateId];
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
    public int getStateId(LR0ItemSet targetSet) throws GrammarException {
	int id = itemSetCollection.indexOf(targetSet);
	if (id >= 0) {
	    return id;
	}
	throw new GrammarException("Target set '" + targetSet
		+ "' was not found!");
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
	for (int stateId = 0; stateId < itemSetCollection.size(); stateId++) {
	    buffer.append("===========\n");
	    buffer.append("State " + stateId + ":\n");
	    buffer.append("===========\n");
	    LR0ItemSet itemSet = (LR0ItemSet) itemSetCollection.toArray()[stateId];
	    buffer.append(itemSet.toString());
	    buffer.append("\n");
	}
	return buffer.toString();
    }
}
