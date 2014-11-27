package com.puresoltechnologies.parsers.parser.functions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.puresoltechnologies.parsers.grammar.production.Construction;
import com.puresoltechnologies.parsers.parser.items.LR1Item;
import com.puresoltechnologies.parsers.parser.items.LR1ItemSet;

public class Goto1 implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<LR1ItemSet, Map<Construction, LR1ItemSet>> gotos = new HashMap<LR1ItemSet, Map<Construction, LR1ItemSet>>();

    private final Closure1 closure;

    public Goto1(Closure1 closure) {
	this.closure = closure;
    }

    /**
     * This is the closure method for a set of items. This method is described
     * in the Dragon Book 4.6.2.
     * 
     * <pre>
     * SetOfItems GOTO(I, X) {
     * 		initialisiere J als die leere Menge;
     * 		for ( jedes Item [ A --&gt; alpha . X beta, a ] in I )
     * 			fuege Item [A --&gt; alpha X . beta, a] zur Menge J hinzu'
     * 		return closure(J);
     * }
     * </pre>
     * 
     * @param itemSet
     *            is the {@link LR1ItemSet} for which the calculation need to
     *            take place.
     * @param construction
     *            is the {@link Construction} to be used.
     * @return A complete set of items is returned containing the parameter
     *         items and all calculated extensions.
     */
    public LR1ItemSet calc(LR1ItemSet itemSet, Construction construction) {
	Map<Construction, LR1ItemSet> map = gotos.get(itemSet);
	if (map == null) {
	    return calculate(itemSet, construction);
	}
	LR1ItemSet result = map.get(construction);
	if (result == null) {
	    return calculate(itemSet, construction);
	}
	return result;
    }

    private LR1ItemSet calculate(LR1ItemSet itemSet, Construction x) {
	Set<LR1Item> items = new LinkedHashSet<LR1Item>();
	for (LR1Item item : itemSet.getNextItems(x)) {
	    LR1Item newItem = new LR1Item(item.getProduction(),
		    item.getPosition() + 1, item.getLookahead());
	    items.add(newItem);
	}
	if (!gotos.containsKey(itemSet)) {
	    gotos.put(itemSet, new HashMap<Construction, LR1ItemSet>());
	}
	LR1ItemSet result = closure.calc(new LR1ItemSet(items));
	gotos.get(itemSet).put(x, result);
	return result;
    }
}
