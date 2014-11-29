package com.puresoltechnologies.parsers.parser.parsetable;

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
import com.puresoltechnologies.parsers.grammar.production.Construction;
import com.puresoltechnologies.parsers.grammar.production.NonTerminal;
import com.puresoltechnologies.parsers.grammar.production.Terminal;

/**
 * This class is the abstract implementation of a parser table. The table based
 * parsers use this tables for parsing texts very efficiently.
 * 
 * The abstract parser table implements several functions for universal parser
 * usage.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public abstract class AbstractParserTable implements ParserTable {

    private static final long serialVersionUID = 4097931723838051724L;

    private static final Logger logger = LoggerFactory
	    .getLogger(AbstractParserTable.class);

    private final List<Map<Construction, ParserActionSet>> table = new ArrayList<Map<Construction, ParserActionSet>>();
    private final Set<Terminal> actionTerminals = new LinkedHashSet<Terminal>();
    private final Set<NonTerminal> gotoNonTerminals = new LinkedHashSet<NonTerminal>();
    private final Grammar grammar;

    /**
     * This is the constructor for initializing the abstract parser table.
     * 
     * @param grammar
     *            is the grammar to be used for parser table creation.
     * @throws GrammarException
     *             is thrown in case of a grammar issue.
     */
    public AbstractParserTable(Grammar grammar) throws GrammarException {
	super();
	this.grammar = grammar;
	logger.trace("Calculate parser table...");
	calculate();
	logger.trace("done.");
    }

    /**
     * This method is overridden in inheriting classes to calculate the actual
     * table.
     * 
     * @throws GrammarException
     *             is thrown in case of grammar issues.
     */
    protected abstract void calculate() throws GrammarException;

    /**
     * This method is used to add a new action terminal.
     * 
     * @param construction
     *            is a {@link Terminal} to be added.
     */
    protected void addActionTerminal(Terminal construction) {
	actionTerminals.add(construction);
    }

    /**
     * This method adds a new goto for a non-terminal.
     * 
     * @param construction
     *            is the {@link NonTerminal} to be added.
     */
    protected void addGotoNonTerminal(NonTerminal construction) {
	gotoNonTerminals.add(construction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Set<Terminal> getActionTerminals() {
	return actionTerminals;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Set<NonTerminal> getGotoNonTerminals() {
	return gotoNonTerminals;
    }

    protected final void addAction(int stateId, Construction construction,
	    ParserAction action) {
	while (table.size() <= stateId) {
	    table.add(new HashMap<Construction, ParserActionSet>());
	}
	ParserActionSet actionSet = table.get(stateId).get(construction);
	if (actionSet == null) {
	    actionSet = new ParserActionSet();
	    table.get(stateId).put(construction, actionSet);
	}
	actionSet.addAction(action);
    }

    protected final Grammar getGrammar() {
	return grammar;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Map<Construction, ParserActionSet> getPossibleActions(
	    int currentState) throws GrammarException {
	return table.get(currentState);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ParserAction getAction(int currentState,
	    Construction construction) throws GrammarException {
	if (construction == null) {
	    return new ParserAction(ActionType.ERROR, -1);
	}
	Map<Construction, ParserActionSet> actions = table.get(currentState);
	if (actions == null) {
	    return new ParserAction(ActionType.ERROR, -1);
	}
	ParserActionSet action = actions.get(construction);
	if (action == null) {
	    return new ParserAction(ActionType.ERROR, -1);
	}
	return action.getAction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ParserActionSet getActionSet(int currentState,
	    Construction construction) {
	if (construction == null) {
	    return ParserActionSet.getErrorSet();
	}
	Map<Construction, ParserActionSet> actions = table.get(currentState);
	if (actions == null) {
	    return ParserActionSet.getErrorSet();
	}
	ParserActionSet set = new ParserActionSet();
	addExactActions(construction, set, actions);
	addNonExcactActions(construction, set, actions);
	return set;
    }

    /**
     * This method adds all actions which are concrete, what means that name and
     * content match exactly.
     * 
     * This method is needed for grammars which ignore the case of letters. A
     * simple Map.get() is not working due to the fact, that the equals method
     * can not be easily created for case-ignorance. It could easily violate the
     * contract for equals and hashCode.
     * 
     * @param construction
     * @param set
     * @param actions
     */
    private void addExactActions(Construction construction,
	    ParserActionSet set, Map<Construction, ParserActionSet> actions) {
	for (Entry<Construction, ParserActionSet> entry : actions.entrySet()) {
	    Construction c = entry.getKey();
	    if (!c.getName().equals(construction.getName())) {
		// if name is not equal, it does obviously not fit...
		continue;
	    }
	    if (c.isNonTerminal() != construction.isNonTerminal()) {
		// if the type is different, they do not fit either...
		continue;
	    }
	    if (construction.isNonTerminal()) {
		/*
		 * if both are non-terminals, the names are the only criterion
		 * for an exact match
		 */
		set.addActions(entry.getValue());
		continue;
	    }
	    Terminal terminal = (Terminal) construction;
	    if (terminal.getText() == null) {
		continue;
	    }
	    Terminal t = (Terminal) c;
	    if (grammar.isIgnoreCase()) {
		if (terminal.getText().equalsIgnoreCase(t.getText())) {
		    set.addActions(entry.getValue());
		}
	    } else {
		if (terminal.getText().equals(t.getText())) {
		    set.addActions(entry.getValue());
		}
	    }
	}
    }

    /**
     * The method is needed for grammars where no keywords are available like
     * Fortran.
     * 
     * E.G.: If 'IF' is a keyword for the IF-construct and 'IF' also allowed to
     * be a variable name, we need to add the actions for terminal
     * NAME_LITERAL/'IF" and for NAME_LITERAL/*.
     */
    private void addNonExcactActions(Construction construction,
	    ParserActionSet set, Map<Construction, ParserActionSet> actions) {
	set.addActions(actions.get(new Terminal(construction.getName(), null)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final int getStateCount() {
	return table.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String toString() {
	StringBuffer buffer = new StringBuffer();
	buffer.append("=============\n");
	buffer.append("Parsing Table\n");
	buffer.append("=============\n");
	buffer.append("\n");
	buffer.append(toColumn("STATE:|"));
	boolean first = true;
	for (int i = 0; i < actionTerminals.size(); i++) {
	    if (first) {
		buffer.append(toColumn("ACTION:"));
		first = false;
	    } else {
		buffer.append(toColumn(" "));
	    }
	}
	buffer.append(toColumn("|"));
	first = true;
	for (int i = 0; i < gotoNonTerminals.size(); i++) {
	    if (first) {
		buffer.append(toColumn("GOTO:"));
		first = false;
	    } else {
		buffer.append(toColumn(" "));
	    }
	}
	buffer.append("\n");
	buffer.append(toColumn("|"));
	for (Construction construction : actionTerminals) {
	    buffer.append(toColumn(construction.toShortString()));
	}
	buffer.append(toColumn("|"));
	for (Construction construction : gotoNonTerminals) {
	    buffer.append(toColumn(construction.getName()));
	}
	buffer.append("\n");
	buffer.append(toColumn("------|"));
	for (int i = 0; i < actionTerminals.size(); i++) {
	    buffer.append(toColumn("-------"));
	}
	buffer.append(toColumn("------|"));
	for (int i = 0; i < gotoNonTerminals.size(); i++) {
	    buffer.append(toColumn("-------"));
	}
	buffer.append("\n");
	for (int state = 0; state < table.size(); state++) {
	    buffer.append(toColumn(String.valueOf(state) + " |"));
	    for (Construction construction : actionTerminals) {
		buffer.append(toColumn(getActionSet(state, construction)
			.toString()));
	    }
	    buffer.append(toColumn("|"));
	    for (Construction construction : gotoNonTerminals) {
		buffer.append(toColumn(getActionSet(state, construction)
			.toString()));
	    }
	    buffer.append("\n");
	}
	return buffer.toString();
    }

    private String toColumn(String s) {
	StringBuffer result = new StringBuffer();
	while (result.length() + s.length() < 7) {
	    result.append(' ');
	}
	result.append(' ');
	result.append(s);
	return result.toString();
    }
}
