package com.puresoltechnologies.parsers.parser.lr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.parsers.grammar.Grammar;
import com.puresoltechnologies.parsers.grammar.GrammarException;
import com.puresoltechnologies.parsers.grammar.production.DummyTerminal;
import com.puresoltechnologies.parsers.grammar.production.FinishTerminal;
import com.puresoltechnologies.parsers.grammar.production.Terminal;
import com.puresoltechnologies.parsers.parser.functions.Closure1;
import com.puresoltechnologies.parsers.parser.items.LR0Item;
import com.puresoltechnologies.parsers.parser.items.LR0ItemSet;
import com.puresoltechnologies.parsers.parser.items.LR1Item;
import com.puresoltechnologies.parsers.parser.items.LR1ItemSet;

public class LALR1ItemSetCollection implements Serializable {

    private static final long serialVersionUID = 6813332818983681476L;

    private static final Logger logger = LoggerFactory
	    .getLogger(LALR1ItemSetCollection.class);

    private final Terminal DUMMY_TERMINAL = DummyTerminal.getInstance();

    private final List<LR1ItemSet> itemSetCollection = new ArrayList<LR1ItemSet>();

    private final Grammar grammar;
    private final Closure1 closure1;
    private final LR0ItemSetCollection lr0ItemSetCollection;
    private final LR0StateTransitions lr0Transitions;

    public LALR1ItemSetCollection(Grammar grammar,
	    LR0ItemSetCollection lr0ItemSetCollection,
	    LR0StateTransitions lr0Transitions, Closure1 closure1)
	    throws GrammarException {
	super();
	this.grammar = grammar;
	this.lr0ItemSetCollection = lr0ItemSetCollection;
	this.lr0Transitions = lr0Transitions;
	this.closure1 = closure1;
	calculate();
    }

    private void calculate() throws GrammarException {
	logger.trace("Calculate LR0 kernel items...");
	calculateLR0KernelItemSets();
	logger.trace("Calculate look aheads for " + itemSetCollection.size()
		+ " items...");
	calculateLookahead();
	logger.trace("Build closure items...");
	addClosure();
	logger.trace("Print look ahead table...");
	printLookaheadTable();
    }

    /**
     * This method takes all item sets from LR0StateTransitionGraph and makes
     * them to a LR1ItemSet containing only kernel items.
     */
    private void calculateLR0KernelItemSets() {
	for (int stateId = 0; stateId < lr0ItemSetCollection.getStateNumber(); stateId++) {
	    LR0ItemSet lr0ItemSet = lr0ItemSetCollection.getItemSet(stateId);
	    Set<LR1Item> lr1Items = new LinkedHashSet<LR1Item>();
	    for (LR0Item lr0Item : lr0ItemSet.getKernelItems()) {
		LR1Item lr1Item = new LR1Item(lr0Item.getProduction(),
			lr0Item.getPosition(), DUMMY_TERMINAL);
		lr1Items.add(lr1Item);
	    }
	    itemSetCollection.add(new LR1ItemSet(lr1Items));
	}
    }

    private void calculateLookahead() {
	boolean changed;
	int run = 0;
	do {
	    run++;
	    logger.trace("Calculate lookahead. Run: " + run);
	    changed = lookaheadCalculationIteration(run);
	} while (changed);
    }

    private boolean lookaheadCalculationIteration(int run) {
	Map<Integer, List<LR1Item>> newItems = new HashMap<Integer, List<LR1Item>>();
	for (int stateId = 0; stateId < itemSetCollection.size(); stateId++) {
	    newItems.put(stateId, new ArrayList<LR1Item>());
	    LR1ItemSet lr1ItemSet = itemSetCollection.get(stateId);
	    for (LR1Item lr1Item : lr1ItemSet.getKernelItems()) {
		LR1ItemSet closure = closure1.calc(lr1Item);
		for (LR1Item closureItem : closure.getAllItems()) {
		    if (closureItem.getLookahead().equals(DUMMY_TERMINAL)) {
			continue;
		    }
		    // Spontaneously generated...
		    LR1Item newItem = new LR1Item(closureItem.getProduction(),
			    closureItem.getPosition() + 1,
			    closureItem.getLookahead());
		    newItems.get(stateId).add(newItem);
		}
	    }
	}
	if (run == 1) {
	    newItems.get(0).add(
		    new LR1Item(grammar.getProductions().get(0), 0,
			    FinishTerminal.getInstance()));
	}
	return addNewItems(newItems);
    }

    private boolean addNewItems(Map<Integer, List<LR1Item>> newItems) {
	boolean changed = false;
	for (Entry<Integer, List<LR1Item>> entry : newItems.entrySet()) {
	    Integer key = entry.getKey();
	    Runtime runtime = Runtime.getRuntime();
	    long free = runtime.freeMemory() / 1024 / 1024;
	    long total = runtime.totalMemory() / 1024 / 1024;
	    long max = runtime.maxMemory() / 1024 / 1024;
	    if (logger.isTraceEnabled()) {
		logger.trace("state: " + key + " / " + itemSetCollection.size()
			+ " (free mem: " + free + "MB; total mem: " + total
			+ "MB; max mem: " + max + "MB)");
	    }
	    for (LR1Item newItem : entry.getValue()) {
		if (addNewLookahead(key, newItem)) {
		    changed = true;
		}
	    }
	}
	return changed;
    }

    private boolean addNewLookahead(int stateId, LR1Item newItem) {
	boolean added = false;
	if (addNewLookahead(newItem, stateId)) {
	    added = true;
	}
	for (int setId : lr0Transitions.getTransitions(stateId).values()) {
	    if (addNewLookahead(newItem, setId)) {
		added = true;
	    }
	}
	return added;
    }

    private boolean addNewLookahead(LR1Item newItem, int targetId) {
	LR1ItemSet lr1ItemSet = itemSetCollection.get(targetId);
	boolean found = false;
	for (LR1Item lr1Item : lr1ItemSet.getKernelItems()) {
	    if ((lr1Item.getPosition() == newItem.getPosition())
		    && lr1Item.getProduction().equals(newItem.getProduction())) {
		found = true;
		break;
	    }
	}
	if (found) {
	    if (lr1ItemSet.addKernelItem(newItem)) {
		lr1ItemSet.removeItem(new LR1Item(newItem.getProduction(),
			newItem.getPosition(), DUMMY_TERMINAL));
		return true;
	    }
	}
	return false;
    }

    private void addClosure() {
	for (LR1ItemSet lr1ItemSet : itemSetCollection) {
	    lr1ItemSet.addNonKernelItems(closure1.calc(lr1ItemSet)
		    .getNonKernelItems());
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
	for (int stateId = 0; stateId < itemSetCollection.size(); stateId++) {
	    LR1ItemSet lr1ItemSet = itemSetCollection.get(stateId);
	    if (lr1ItemSet.equals(targetSet)) {
		return stateId;
	    }
	}
	throw new GrammarException("Target set '" + targetSet
		+ "' was not found!");
    }

    public int getStateIdForPartialItem(LR1ItemSet targetSet)
	    throws GrammarException {
	for (int stateId = 0; stateId < itemSetCollection.size(); stateId++) {
	    LR1ItemSet lr1ItemSet = itemSetCollection.get(stateId);
	    boolean all = true;
	    for (LR1Item lr1Item : targetSet.getKernelItems()) {
		if (!lr1ItemSet.containsItem(lr1Item)) {
		    all = false;
		    break;
		}
	    }
	    if (all) {
		return stateId;
	    }
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
	    LR1ItemSet itemSet = itemSetCollection.get(stateId);
	    buffer.append(itemSet.toString());
	    buffer.append("\n");
	}
	return buffer.toString();
    }

    private void printLookaheadTable() {
	for (int setId = 0; setId < itemSetCollection.size(); setId++) {
	    System.out.print(setId);
	    LR1ItemSet lr1ItemSet = itemSetCollection.get(setId);
	    for (LR1Item lr1Item : lr1ItemSet.getAllItems()) {
		System.out.println("\t" + ((LR0Item) lr1Item).toString());
	    }
	}
    }
}
