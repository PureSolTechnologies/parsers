package com.puresoltechnologies.parsers.parser.functions;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import com.puresoltechnologies.parsers.grammar.production.Construction;
import com.puresoltechnologies.parsers.parser.items.LR0Item;
import com.puresoltechnologies.parsers.parser.items.LR0ItemSet;

public class Goto0 implements Serializable {

    private static final long serialVersionUID = 1L;

    private final Map<LR0ItemSet, Map<Construction, LR0ItemSet>> gotos = new HashMap<LR0ItemSet, Map<Construction, LR0ItemSet>>();

    private final Closure0 closure;

    public Goto0(Closure0 closure) {
	this.closure = closure;
    }

    /**
     * This is the closure method for a set of items. This method is descibed in
     * the Dragon Book 4.6.2.
     * 
     * @param itemSet
     *            is an {@link LR0ItemSet} to be used for calculation.
     * @param construction
     *            is the construction to be used.
     * @return A complete set of items is returned containing the parameter
     *         items and all calculated extensions.
     */
    public LR0ItemSet calc(LR0ItemSet itemSet, Construction construction) {
	Map<Construction, LR0ItemSet> map = gotos.get(itemSet);
	if (map == null) {
	    return calculate(itemSet, construction);
	}
	LR0ItemSet result = map.get(construction);
	if (result == null) {
	    return calculate(itemSet, construction);
	}
	return result;
    }

    private LR0ItemSet calculate(LR0ItemSet itemSet, Construction x) {
	Set<LR0Item> items = new LinkedHashSet<LR0Item>();
	for (LR0Item item : itemSet.getNextItems(x)) {
	    items.add(new LR0Item(item.getProduction(), item.getPosition() + 1));
	}
	;
	if (!gotos.containsKey(itemSet)) {
	    gotos.put(itemSet, new HashMap<Construction, LR0ItemSet>());
	}
	LR0ItemSet result = closure.calc(new LR0ItemSet(items));
	gotos.get(itemSet).put(x, result);
	return result;
    }
}
