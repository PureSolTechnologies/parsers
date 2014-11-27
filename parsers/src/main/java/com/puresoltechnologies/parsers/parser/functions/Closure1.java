package com.puresoltechnologies.parsers.parser.functions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.puresoltechnologies.parsers.grammar.Grammar;
import com.puresoltechnologies.parsers.grammar.production.Construction;
import com.puresoltechnologies.parsers.grammar.production.Production;
import com.puresoltechnologies.parsers.grammar.production.ProductionSet;
import com.puresoltechnologies.parsers.grammar.production.Terminal;
import com.puresoltechnologies.parsers.parser.items.LR1Item;
import com.puresoltechnologies.parsers.parser.items.LR1ItemSet;

public class Closure1 implements Serializable {

    private static final long serialVersionUID = 5779840433746397739L;

    /**
     * This field contains all calculated closures to avoid double calculations.
     */
    private final Map<LR1ItemSet, LR1ItemSet> closures = new HashMap<LR1ItemSet, LR1ItemSet>();

    private final ProductionSet productions;
    private final First first;

    public Closure1(Grammar grammar, First first) {
	this.productions = grammar.getProductions();
	this.first = first;
    }

    public LR1ItemSet calc(LR1Item item) {
	return calc(new LR1ItemSet(item));
    }

    /**
     * This is the closure method for a set of items. This method is descibed in
     * the Dragon Book 4.6.2.
     * 
     * <pre>
     * 	SetOfItems CLOSURE(I) {
     * 		repeat
     * 			for (jedes Item [ A --&gt; alpha . B beta, a] in I)
     * 				for (jede Produktion B --&gt; gamma in G')
     * 					for ( jedes Terminal b in FIRST(beta a) )
     * 						fuege [ B --&gt; . gamma, b ] zur Menge I hinzu;
     * 		until es werden keine weiteren Items mehr zu I hinzugefuegt;
     * 	}
     * </pre>
     * 
     * @param initialItemSet
     *            is the {@link LR1ItemSet} for initial state.
     * @return A complete set of items is returned containing the parameter
     *         items and all calculated extensions.
     */
    public LR1ItemSet calc(LR1ItemSet initialItemSet) {
	LR1ItemSet result = closures.get(initialItemSet);
	if (result == null) {
	    return calculate(initialItemSet);
	}
	return result;
    }

    private LR1ItemSet calculate(LR1ItemSet initialItemSet) {
	LR1ItemSet itemSet = new LR1ItemSet(initialItemSet);
	int currentPosition = 0;
	int newPosition = 0;
	int currentSize = 1;
	do {
	    newPosition = itemSet.getSize();
	    currentSize = itemSet.getSize();
	    List<LR1Item> lr1Items = new ArrayList<LR1Item>(
		    itemSet.getAllItems());
	    for (int position = currentPosition; position < currentSize; position++) {
		LR1Item item = lr1Items.get(position);
		if (!item.hasNext()) {
		    continue;
		}
		Construction nextConstruction = item.getNext();
		if (nextConstruction.isTerminal()) {
		    continue;
		}
		Production helperProduction = getRemainingProduction(item,
			item.getLookahead());
		for (Production grammarProduction : productions
			.get(nextConstruction.getName())) {
		    Set<Terminal> firstResult = first.get(helperProduction);
		    for (Terminal terminal : firstResult) {
			LR1Item newItem = new LR1Item(grammarProduction, 0,
				terminal);
			itemSet.addNonKernelItem(newItem);
		    }
		}
	    }
	    currentPosition = newPosition;
	} while (currentSize != itemSet.getSize());
	closures.put(initialItemSet, itemSet);
	return itemSet;
    }

    private Production getRemainingProduction(LR1Item item,
	    Construction lookAhead) {
	Production production = new Production(item.getProduction().getName());
	for (int id = item.getPosition() + 1; id < item.getProduction()
		.getConstructions().size(); id++) {
	    production.addConstruction(item.getProduction().getConstructions()
		    .get(id));
	}
	production.addConstruction(lookAhead);
	return production;
    }
}
