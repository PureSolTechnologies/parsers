package com.puresoltechnologies.parsers.parser.lr;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.parsers.grammar.Grammar;
import com.puresoltechnologies.parsers.grammar.GrammarException;
import com.puresoltechnologies.parsers.grammar.production.FinishTerminal;
import com.puresoltechnologies.parsers.grammar.production.NonTerminal;
import com.puresoltechnologies.parsers.grammar.production.Production;
import com.puresoltechnologies.parsers.grammar.production.Terminal;
import com.puresoltechnologies.parsers.grammar.token.Visibility;
import com.puresoltechnologies.parsers.lexer.Token;
import com.puresoltechnologies.parsers.lexer.TokenStream;
import com.puresoltechnologies.parsers.parser.AbstractParser;
import com.puresoltechnologies.parsers.parser.Parser;
import com.puresoltechnologies.parsers.parser.ParserException;
import com.puresoltechnologies.parsers.parser.ParseTreeNode;
import com.puresoltechnologies.parsers.parser.parsetable.ActionType;
import com.puresoltechnologies.parsers.parser.parsetable.ParserAction;
import com.puresoltechnologies.parsers.parser.parsetable.ParserActionSet;
import com.puresoltechnologies.parsers.parser.parsetable.ParserTable;

/**
 * This class is a complete implementation of a LR parser. Only the parser table
 * is missing. The implementation of this needs to be done in the inherited
 * class in the abstract method.
 * 
 * This parser creates a action stack which is a collection all processes
 * necassary to create a parser tree. The acutal creation of a parser tree is
 * performed in an external converter.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public abstract class AbstractLRParser extends AbstractParser {

    private static final long serialVersionUID = 9173136242276185400L;

    private final static Logger logger = LoggerFactory
	    .getLogger(AbstractLRParser.class);

    /**
     * This flag specifies whether the parser is allowed to use back tracking or
     * not. If it is set to true, back tracking is allowed.
     */
    private final boolean backtrackEnabled;

    /**
     * This field stores the maximum size of the backtrack buffer. If this value
     * is set to zero, the backtrack buffer has no maximum size.
     */
    private final int backtrackDepth;

    /**
     * This filed is used to store the timeout value. The parser throws an
     * exception after the number of seconds stored in this field. If this value
     * is zero, timeout checking is disabled.
     */
    private final int timeout;

    /**
     * This is the startTime of the parsing process.
     */
    private long startTime;

    /**
     * This stack keeps the back tracking information for back tracking.
     */
    private final Stack<BacktrackLocation> backtrackStack = new Stack<BacktrackLocation>();

    /**
     * This field contains the parser table to be used.
     */
    private final ParserTable parserTable;

    /**
     * This stack is for storing the states of the parser for shift and
     * reduction.
     */
    private Stack<Integer> stateStack = new Stack<Integer>();

    /**
     * This field contains the maximum position within the token stream which
     * was reached during parsing. This maximum position can be used after an
     * parser exception to retrieve to most possible position of the parsing
     * issue. The issue may arise due to an illegal syntax construct in the
     * source token stream or a wrongly or not defined grammar construct.
     */
    private int maxPosition = 0;

    /**
     * This stack stores the AST fragments during parsing in parallel to the
     * states in stateStack.
     */
    private final List<ParserAction> actionStack = new ArrayList<ParserAction>();

    /**
     * This field contains a histogramm of all parser error during the parser
     * process. This field can be used for grammar debugging to find ambiguous
     * grammar constructs. The time behavior can be optimized with this
     * informaiton, too.
     */
    private final ParserErrors parserErrors = new ParserErrors();

    /**
     * This is the current position in the stream.
     */
    private int streamPosition = 0;

    /**
     * This is the counter of the steps and gives the current step number during
     * parsing.
     */
    private int stepCounter = 0;

    public AbstractLRParser(Grammar grammar) throws GrammarException {
	super(grammar);
	parserTable = calculateParserTable();
	backtrackEnabled = Boolean.valueOf((String) grammar.getOptions().get(
		"parser.backtracking"));
	int backtrackDepth = 0;
	try {
	    backtrackDepth = Integer.parseInt((String) grammar.getOptions()
		    .get("parser.backtracking.depth"));
	} catch (NumberFormatException e) {
	}
	this.backtrackDepth = backtrackDepth;
	int timeout = 0;
	try {
	    timeout = Integer.valueOf((String) grammar.getOptions().get(
		    "parser.timeout"));
	} catch (NumberFormatException e) {
	}
	this.timeout = timeout;
    }

    /**
     * This method is abstract is to be implemented by inheriting classes to
     * provide the correct parser table.
     * 
     * @return The correct parser table is to be returned.
     * @throws GrammarException
     *             is thrown in case of grammar issues.
     */
    protected abstract ParserTable calculateParserTable()
	    throws GrammarException;

    protected ParserTable getParserTable() {
	return parserTable;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final ParseTreeNode parse(TokenStream tokenStream)
	    throws ParserException {
	startTime = System.currentTimeMillis();
	setTokenStream(tokenStream);
	reset();
	return parse();
    }

    /**
     * This method is called just before running the parser to reset all
     * internal values and to clean all stacks.
     */
    private final void reset() {
	backtrackStack.clear();
	actionStack.clear();
	stateStack.clear();
	parserErrors.clear();
	streamPosition = 0;
	stepCounter = 0;
	stateStack.push(0);
	maxPosition = 0;
	shiftIgnoredTokens();
    }

    /**
     * This method treats all ignored tokens during a shift. The ignored tokens
     * are just skipped by moving the stream position variable forward.
     */
    private final void shiftIgnoredTokens() {
	if (streamPosition == getTokenStream().size()) {
	    return;
	}
	Token token = getTokenStream().get(streamPosition);
	while (token.getVisibility() == Visibility.IGNORED) {
	    streamPosition++;
	    if (streamPosition == getTokenStream().size()) {
		break;
	    }
	    token = getTokenStream().get(streamPosition);
	}
    }

    /**
     * This method does the actual parsing.
     * 
     * @return The result AST is returned.
     * @throws ParserException
     */
    private final ParseTreeNode parse() throws ParserException {
	try {
	    createActionStack();
	    return LRTokenStreamConverter.convert(getTokenStream(),
		    getGrammar(), actionStack);
	} catch (GrammarException e) {
	    logger.error(e.getMessage(), e);
	    throw new ParserException(e.getMessage());
	}
    }

    /**
     * This method is the actual parsing. The parser creates an action stack
     * which can be later convertered with a {@link LRTokenStreamConverter} into
     * a {@link ParseTreeNode}.
     * 
     * @throws ParserException
     * @throws GrammarException
     */
    private void createActionStack() throws ParserException, GrammarException {
	boolean accepted = false;
	do {
	    checkTimeout();
	    stepCounter++;
	    if (logger.isTraceEnabled()) {
		logger.trace(toString());
	    }
	    if (streamPosition > maxPosition) {
		maxPosition = streamPosition;
	    }
	    final ParserActionSet actionSet;
	    final Token token;
	    if (streamPosition < getTokenStream().size()) {
		token = getTokenStream().get(streamPosition);
		actionSet = parserTable.getActionSet(stateStack.peek(),
			new Terminal(token.getName(), token.getText()));
	    } else {
		token = null;
		actionSet = parserTable.getActionSet(stateStack.peek(),
			FinishTerminal.getInstance());
	    }
	    if (logger.isTraceEnabled()) {
		logger.trace(actionSet.toString());
	    }
	    final ParserAction action = getAction(actionSet);
	    switch (action.getAction()) {
	    case SHIFT:
		shift(action);
		break;
	    case REDUCE:
		reduce(action);
		break;
	    case ACCEPT:
		accept(action);
		accepted = true;
		break;
	    case ERROR:
		error();
		break;
	    default:
		throw new ParserException("Invalid action '" + action
			+ "'for parser near '"
			+ getTokenStream().getCodeSample(maxPosition) + "'!");
	    }
	} while (!accepted);
    }

    /**
     * This method checks for the timeout. If the time ran out, an
     * ParserException is thrown and the parser process is finished.
     * 
     * @throws ParserException
     *             is thrown if the time ran out.
     */
    private void checkTimeout() throws ParserException {
	if (timeout > 0) {
	    if (System.currentTimeMillis() - startTime > timeout * 1000) {
		throw new ParserException("Timeout after " + timeout
			+ " seconds near '"
			+ getTokenStream().getCodeSample(maxPosition) + "'!");
	    }
	}
    }

    /**
     * This mehtod returns the currently to be processed action. This method
     * also takes care for ambiguous grammars and backtracking.
     * 
     * @param actionSet
     * @return
     * @throws GrammarException
     */
    private final ParserAction getAction(ParserActionSet actionSet)
	    throws GrammarException {
	if (actionSet.getActionNumber() == 1) {
	    /*
	     * action is not ambiguous, therefore, the action is easily
	     * returned...
	     */
	    return actionSet.getAction();
	}
	if (!backtrackEnabled) {
	    /*
	     * Backtracking is disabled and action set is ambiguous. Throw
	     * exception due to illegal state...
	     */
	    logger.trace("Action set '" + actionSet
		    + "' is ambiguous and back tracking is disabled!");
	    throw new GrammarException("Grammar is ambiguous!");
	}
	if ((!backtrackStack.isEmpty())
		&& (backtrackStack.peek().getStepCounter() == stepCounter)) {
	    /*
	     * We are currently at a step with stored backtracking information,
	     * so we just returned from a backtrack. Trying next alternative...
	     */
	    if (logger.isTraceEnabled()) {
		logger.trace("Action set '"
			+ actionSet
			+ "' is ambiguous and back tracking was performed already. Trying new alternative...");
	    }
	    BacktrackLocation location = backtrackStack.pop();
	    int stepAhead = 1;
	    if (location.getLastAlternative() + stepAhead >= actionSet
		    .getActionNumber()) {
		logger.trace("No alternative left. Abort.");
		return new ParserAction(ActionType.ERROR, -1);
	    }
	    addBacktrackLocation(location.getLastAlternative() + stepAhead);
	    return actionSet.getAction(location.getLastAlternative()
		    + stepAhead);
	}
	/*
	 * We have a new ambiguous state. We store backtracking information and
	 * try first alternative...
	 */
	if (logger.isTraceEnabled()) {
	    logger.trace("Action set '"
		    + actionSet
		    + "' is ambiguous. Installing back tracking location in stack...");
	}
	addBacktrackLocation(0);
	return actionSet.getAction(0);
    }

    /**
     * This method adds the backtracking information for the current step.
     * 
     * @param usedAlternative
     *            is the number of the alternative which is the next to be
     *            tried.
     */
    @SuppressWarnings("unchecked")
    private final void addBacktrackLocation(int usedAlternative) {
	backtrackStack.push(new BacktrackLocation((Stack<Integer>) stateStack
		.clone(), actionStack.size(), streamPosition, stepCounter,
		usedAlternative));
	if (backtrackDepth > 0) {
	    while (backtrackStack.size() > backtrackDepth) {
		backtrackStack.remove(0);
	    }
	}
    }

    /**
     * This method is called for shift actions.
     * 
     * @param newState
     *            is the new state id after shift.
     * @param token
     *            is the current token in token stream.
     * @throws ParserException
     */
    private final void shift(ParserAction action) {
	stateStack.push(action.getParameter());
	actionStack.add(action);
	streamPosition++;
	shiftIgnoredTokens();
    }

    /**
     * This method is called for reduce actions.
     * 
     * @param grammarRuleId
     *            is the rule id which is to be applied for reduction.
     * @throws ParserException
     *             is thrown if the rule can not successfully be applied.
     */
    private final void reduce(ParserAction action) throws ParserException {
	try {
	    Production production = getGrammar().getProduction(
		    action.getParameter());
	    for (int i = 0; i < production.getConstructions().size(); i++) {
		/*
		 * The for loop is run as many times as the production contains
		 * constructions which are added up for an AST node.
		 */
		stateStack.pop();
	    }
	    actionStack.add(action);
	    ParserAction gotoAction = parserTable.getAction(stateStack.peek(),
		    new NonTerminal(production.getName()));
	    if (gotoAction.getAction() == ActionType.ERROR) {
		error();
		return;
	    }
	    stateStack.push(gotoAction.getParameter());
	} catch (GrammarException e) {
	    logger.error(e.getMessage(), e);
	    throw new ParserException(e.getMessage());
	}
    }

    /**
     * This method is called for accept action and checks for the acceptance
     * state for the parser to finish the parsing process.
     * 
     * The containing check in this method is a check to be secure. Accept is
     * only true if the tree stack only contains one more element (the result
     * tree) and the state stack only contains the current state and the zero
     * state for the start element.
     * 
     * @throws ParserException
     */
    private final void accept(ParserAction action) throws ParserException {
	actionStack.add(action);
    }

    /**
     * This method is called for an error action.
     * 
     * @param token
     * @throws ParserException
     */
    private final void error() throws ParserException {
	Integer currentState = stateStack.peek();
	parserErrors.addError(currentState);
	if (backtrackEnabled && !backtrackStack.isEmpty()) {
	    trackBack();
	    return;
	}
	if (!backtrackEnabled) {
	    logger.trace("No valid action available and back tracking is disabled. Aborting near '"
		    + getTokenStream().getCodeSample(maxPosition) + "'...");
	} else {
	    logger.trace("No valid action available and back tracking stack is empty. Aborting near '"
		    + getTokenStream().getCodeSample(maxPosition) + "'...");
	}
	throw new ParserException(
		"Error! Could not parse the token stream near '"
			+ getTokenStream().getCodeSample(maxPosition) + "'!");
    }

    /**
     * This method perform the back tracking by popping all relevant information
     * from the stacks.
     */
    private final void trackBack() {
	logger.trace("No valid action available. Perform back tracking...");
	BacktrackLocation backtrackLocation = backtrackStack.peek();
	streamPosition = backtrackLocation.getStreamPosition();
	stepCounter = backtrackLocation.getStepCounter();
	while (actionStack.size() > backtrackLocation.getActionStackSize()) {
	    actionStack.remove(actionStack.size() - 1);
	}
	stateStack = backtrackLocation.getStateStack();
	stepCounter--;
    }

    @Override
    public String toString() {
	StringBuffer buffer = new StringBuffer();
	buffer.append("(");
	buffer.append(stepCounter);
	buffer.append(")\t| ");
	for (Integer stateId : stateStack) {
	    buffer.append(" ");
	    buffer.append(stateId);
	}
	buffer.append("\t| ");
	TokenStream tokenStream = getTokenStream();
	if (tokenStream != null) {
	    for (int i = streamPosition; i < tokenStream.size(); i++) {
		buffer.append(" ");
		buffer.append(tokenStream.get(i));
		if (i > streamPosition + 5) {
		    buffer.append("[...]");
		    break;
		}
	    }
	    buffer.append("$");
	}
	return buffer.toString();
    }

    @Override
    public Parser clone() {
	try {
	    AbstractLRParser cloned = (AbstractLRParser) super.clone();

	    Field backtrackEnabled = AbstractLRParser.class
		    .getDeclaredField("backtrackEnabled");
	    backtrackEnabled.setAccessible(true);
	    backtrackEnabled.set(cloned, this.backtrackEnabled);
	    backtrackEnabled.setAccessible(false);

	    Field backtrackDepth = AbstractLRParser.class
		    .getDeclaredField("backtrackDepth");
	    backtrackDepth.setAccessible(true);
	    backtrackDepth.set(cloned, this.backtrackDepth);
	    backtrackDepth.setAccessible(false);

	    Field timeout = AbstractLRParser.class.getDeclaredField("timeout");
	    timeout.setAccessible(true);
	    timeout.set(cloned, this.timeout);
	    timeout.setAccessible(false);

	    Field parserTable = AbstractLRParser.class
		    .getDeclaredField("parserTable");
	    parserTable.setAccessible(true);
	    parserTable.set(cloned, this.parserTable);
	    parserTable.setAccessible(false);

	    Field backtrackStack = AbstractLRParser.class
		    .getDeclaredField("backtrackStack");
	    backtrackStack.setAccessible(true);
	    backtrackStack.set(cloned, new Stack<BacktrackLocation>());
	    backtrackStack.setAccessible(false);

	    Field stateStack = AbstractLRParser.class
		    .getDeclaredField("stateStack");
	    stateStack.setAccessible(true);
	    stateStack.set(cloned, new Stack<Integer>());
	    stateStack.setAccessible(false);

	    Field actionStack = AbstractLRParser.class
		    .getDeclaredField("actionStack");
	    actionStack.setAccessible(true);
	    actionStack.set(cloned, new ArrayList<ParserAction>());
	    actionStack.setAccessible(false);

	    Field parserErrors = AbstractLRParser.class
		    .getDeclaredField("parserErrors");
	    parserErrors.setAccessible(true);
	    parserErrors.set(cloned, new ParserErrors());
	    parserErrors.setAccessible(false);

	    cloned.startTime = 0;
	    cloned.maxPosition = 0;
	    cloned.streamPosition = 0;
	    cloned.stepCounter = 0;

	    return cloned;
	} catch (SecurityException e) {
	    throw new RuntimeException(e);
	} catch (NoSuchFieldException e) {
	    throw new RuntimeException(e);
	} catch (IllegalArgumentException e) {
	    throw new RuntimeException(e);
	} catch (IllegalAccessException e) {
	    throw new RuntimeException(e);
	}
    }
}
