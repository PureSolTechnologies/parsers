package com.puresoltechnologies.parsers.parser.packrat;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.commons.trees.TreeException;
import com.puresoltechnologies.commons.trees.TreeVisitor;
import com.puresoltechnologies.commons.trees.TreeWalker;
import com.puresoltechnologies.commons.trees.WalkingAction;
import com.puresoltechnologies.commons.types.StringUtils;
import com.puresoltechnologies.parsers.grammar.Grammar;
import com.puresoltechnologies.parsers.grammar.production.Construction;
import com.puresoltechnologies.parsers.grammar.production.Production;
import com.puresoltechnologies.parsers.grammar.production.Terminal;
import com.puresoltechnologies.parsers.grammar.token.TokenDefinition;
import com.puresoltechnologies.parsers.grammar.token.TokenDefinitionSet;
import com.puresoltechnologies.parsers.grammar.token.Visibility;
import com.puresoltechnologies.parsers.lexer.Token;
import com.puresoltechnologies.parsers.lexer.TokenMetaData;
import com.puresoltechnologies.parsers.parser.ParserException;
import com.puresoltechnologies.parsers.parser.ParserTree;
import com.puresoltechnologies.parsers.source.SourceCode;
import com.puresoltechnologies.parsers.source.SourceCodeLine;
import com.puresoltechnologies.parsers.source.SourceCodeLocation;
import com.puresoltechnologies.parsers.source.StringWithLocation;

/**
 * This is a complete implementation of a packrat parser.
 * 
 * <b>This class is not thread safe!</b>
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class PackratParser implements Serializable {

	private static final long serialVersionUID = -2004344389320369178L;

	private static final Logger logger = LoggerFactory
			.getLogger(PackratParser.class);

	/**
	 * This is the memo for this parser.
	 */
	private final PackratMemo memo = new PackratMemo();

	/**
	 * This field contains a list of all token definitions which are to ignored
	 * or hidden. This is used by the parser to move all parts away which is at
	 * the moment of parsing not of any interest.
	 */
	private final Set<TokenDefinition> hiddenAndIgnoredTokens;

	/**
	 * This is the rule invocation stack which contains all rules which are
	 * currently processed. These rules are nested. The data is kept in
	 * left-recursive data elements to put data in here if needed for recursion
	 * detection and seed growing. This field contains only one element, but the
	 * next elements is referenced.
	 */
	private RuleInvocation ruleInvocationStack = null;

	/**
	 * This is a list of all heads on all positions which are currently grown.
	 */
	private final Map<Integer, Head> heads = new HashMap<Integer, Head>();

	/**
	 * This flag specified whether ignored or hidden tokens are put leading to
	 * the following instructions or trailing after the latest instructions
	 * processed.
	 */
	private final Boolean ignoredLeading;

	/**
	 * This is the grammer to be used for parsing.
	 */
	private final Grammar grammar;

	/**
	 * This is the text to be parsed.
	 */
	private String text = "";

	/**
	 * This array contains after initialization an array of
	 * {@link SourceCodeLine} references. This array is used to map each
	 * position in the input text to original {@link SourceCodeLine} to get the
	 * correct meta information for creating the tokens.
	 */
	private StringWithLocation textWithSource = null;

	/**
	 * This field contains the original source code which is used to extract
	 * meta information.
	 */
	private SourceCode sourceCode = null;

	/**
	 * This is a field for tracking the maximum position of the parsing process.
	 * When the parsing process aborts, this position is the most likely
	 * position where an error is within the input stream.
	 */
	private int maxPosition = 0;

	public PackratParser(Grammar grammar) {
		super();
		this.grammar = grammar;
		hiddenAndIgnoredTokens = extractHiddenAndIgnoredTokensFromGrammar();
		Properties options = grammar.getOptions();
		ignoredLeading = Boolean.valueOf(options.getProperty(
				"grammar.ignored-leading", "true"));

	}

	/**
	 * This method extracts all token definitions which are to be ignored or
	 * hidden to process them separately and to put them into special locations
	 * into the parser tree.
	 * 
	 * @return
	 */
	private Set<TokenDefinition> extractHiddenAndIgnoredTokensFromGrammar() {
		Set<TokenDefinition> hiddenAndIgnoredTokens = new LinkedHashSet<TokenDefinition>();
		for (TokenDefinition tokenDefinition : grammar.getTokenDefinitions()
				.getDefinitions()) {
			Visibility visibility = tokenDefinition.getVisibility();
			if ((visibility == Visibility.HIDDEN)
					|| (visibility == Visibility.IGNORED)) {
				hiddenAndIgnoredTokens.add(tokenDefinition);
			}
		}
		return hiddenAndIgnoredTokens;
	}

	/**
	 * This is the actual parser start. Work is performed on StringBuffer
	 * object.
	 * 
	 * @param buffer
	 * @return
	 * @throws ParserException
	 */
	public ParserTree parse(SourceCode sourceCode) throws ParserException {
		return parse(sourceCode, "_START_");
	}

	/**
	 * This method resets the whole parser and sets the new values for text and
	 * name to start the parsing process afterwards.
	 * 
	 * @param text
	 * 
	 * @param name
	 */
	private void initialize(SourceCode sourceCode) {
		this.sourceCode = sourceCode;
		textWithSource = new StringWithLocation(sourceCode);
		text = textWithSource.getText();

		memo.clear();
		ruleInvocationStack = null;
		maxPosition = 0;
	}

	/**
	 * This is the actual parser start. After running the parse, a check is
	 * applied to check for full parsing or partial parsing. If partial parsing
	 * is found, an exception is thrown.
	 * 
	 * @param buffer
	 * @return
	 * @throws ParserException
	 */
	public ParserTree parse(SourceCode sourceCode, String production)
			throws ParserException {
		try {
			initialize(sourceCode);
			MemoEntry progress = applyRule(production, 0, 1);
			if (progress.getDeltaPosition() != text.length()) {
				throw new ParserException(getParserErrorMessage());
			}
			Object answer = progress.getAnswer();
			if (answer instanceof Status) {
				Status status = (Status) answer;
				switch (status) {
				case FAILED:
					throw new ParserException(
							"Parser returned status 'FAILED'.");
				default:
					throw new RuntimeException("A status '" + status.toString()
							+ "' is not expected here.");
				}

			}
			ParserTree parserTree = (ParserTree) answer;
			normalizeParents(parserTree);
			return parserTree;
		} catch (TreeException e) {
			throw new ParserException(e);
		}
	}

	/**
	 * This message just generates a parser exception message to be returned
	 * containing the maximum position where the parser could not proceed. This
	 * should be in most cases the position where the error within the text is
	 * located.
	 * 
	 * @return
	 */
	private String getParserErrorMessage() {
		StringBuffer code = new StringBuffer(text);
		code = code.insert(maxPosition, " >><< ");
		String codeString = code.substring(maxPosition - 100 < 0 ? 0
				: maxPosition - 100,
				maxPosition + 100 >= code.length() ? code.length()
						: maxPosition + 100);
		return "Could not parse the input string near '" + codeString + "'!";
	}

	private StringBuilder indentLine() {
		RuleInvocation invokation = ruleInvocationStack;
		StringBuilder builder = new StringBuilder();
		while (invokation != null) {
			builder.append("    ");
			invokation = invokation.getNext();
		}
		return builder;
	}

	private void printMessage(String text, int position, int line) {
		if (logger.isTraceEnabled()) {
			StringBuilder builder = indentLine();
			builder.append(position);
			builder.append(" ");
			builder.append(line);
			builder.append(" ");
			builder.append(text);
			logger.trace(builder.toString());
		}
	}

	/**
	 * This method tries to apply a production at a given position. The
	 * production is given as a name and not as a concrete rule to process all
	 * choices afterwards.
	 * 
	 * @param rule
	 * @param position
	 * @return
	 * @throws TreeException
	 * @throws ParserException
	 */
	private MemoEntry applyRule(String rule, int position, int line)
			throws TreeException, ParserException {
		printMessage("applyRule: " + rule, position, line);
		MemoEntry m = recall(rule, position, line);
		if (m == null) {
			/*
			 * "Create a new LR and push it onto the rule invocation stack."
			 * 
			 * At this point we found a rule which was never processed at this
			 * position. We start completely virgin here...
			 */
			LR lr = new LR(MemoEntry.failed(), rule, null);
			ruleInvocationStack = new RuleInvocation(MemoEntry.failed(), rule,
					null, ruleInvocationStack);
			/*
			 * "Memoize lr, then evaluate R."
			 * 
			 * Put a fail into memoization memory and evaluate the rule
			 * afterwards.
			 */
			m = MemoEntry.create(lr);
			memo.setMemo(rule, position, line, m);
			final MemoEntry ans = eval(rule, position, line);
			/*
			 * "Pop lr off the rule invocation stack."
			 * 
			 * The evaluation of this lr is finished now and we can remove it
			 * from stack. This was needed in cases a left recursion whould be
			 * found within the rule.
			 */
			ruleInvocationStack = ruleInvocationStack.getNext();

			if ((m.getAnswer() instanceof LR)
					&& (((LR) m.getAnswer()).getHead() != null)) {
				/*
				 * If a head was added to lr, we found a recursion during
				 * evaluation. We need to set the seed and process with left
				 * recursion evaluation. For that purpose we grow m with ans as
				 * seed.
				 */
				lr = (LR) m.getAnswer();
				lr.setSeed(ans);
				MemoEntry lrAnswer = lrAnswer(rule, position, line, m);
				printMessage("grow LR for '" + rule + "' (" + lrAnswer + ").",
						position, line);
				return lrAnswer;
			} else {
				/*
				 * We finished an evaluation and did not find a recursion. So
				 * the result (independent of the the state) is stored in memo
				 * and returned.
				 */
				m.set(ans);
				printMessage("applied '" + rule + "' (" + ans.getAnswer()
						+ ").", position, line);
				return ans;
			}
		} else {
			/*
			 * We were here already and with the same production. We either have
			 * a real answer or we found a recursion with or without currently
			 * seed growing...
			 */
			if ((m.getAnswer() instanceof LR)) {
				/*
				 * There is still a LR object in the memo, so we found a
				 * recursion or an in-progress seed grow. We setup the LR seed
				 * grow and return the current seed.
				 */
				setupLR(rule, (LR) m.getAnswer());
				MemoEntry seed = ((LR) m.getAnswer()).getSeed();
				printMessage("Found recursion or grow in process for '" + rule
						+ "' (" + seed + ").", position, line);
				return seed;
			} else {
				/*
				 * We were already here and we have a real result. So we can
				 * just return the answer.
				 */
				printMessage("already processed '" + rule + "' (" + m + ").",
						position, line);
				return m;
			}
		}
	}

	/**
	 * After finding a recursion or a seed grow in process, this method puts all
	 * information in place for seed grow. This might be the start information
	 * for the growth or the current result in the growing, which means the
	 * current result.
	 * 
	 * @param production
	 * @param l
	 * @throws ParserException
	 */
	private void setupLR(String production, final LR l) throws ParserException {
		/*
		 * If the lr object does not contain a head, we found a new recursion
		 * production. Otherwise we already know the production, but we found
		 * another production which is involved in the recursion.
		 */
		if (l.getHead() == null)
			l.setHead(new Head(production));
		/*
		 * Go over all heads and...!?
		 */
		RuleInvocation s = ruleInvocationStack;
		while (!l.getHead().getProduction().equals(s.getProduction())) {
			s.setHead(l.getHead());
			l.getHead().addInvolved(s.getProduction());
			s = s.getNext();
			if (s == null)
				throw new RuntimeException(
						"We should find the head again, when we search the stack.\n"
								+ "We found a recursion and the rule should be there again.");
		}
	}

	/**
	 * This method is an extended getMemo function which also takes into account
	 * the seed growing processes which might be underway.
	 * 
	 * @param production
	 *            is the currently processes production.
	 * @param position
	 *            is the current parser position.
	 * @return A new memo entry is returned containing the result of the
	 *         memoization buffer and seed growing status lookup.
	 * @throws TreeException
	 * @throws ParserException
	 */
	private MemoEntry recall(String production, int position, int line)
			throws TreeException, ParserException {
		/*
		 * Retrieve the current memoized item for the production and the head on
		 * the current position.
		 */
		final MemoEntry m = memo.getMemo(production, position);
		final Head h = heads.get(position);
		/*
		 * "If not growing a seed parse, just return what is stored in the memo
		 * table."
		 */
		if (h == null) {
			return m;
		}
		/*
		 * "Do not evaluate any rule that is not involved in this left recursion."
		 * 
		 * If we have never been at this position (NONE) and the head rule
		 * (production) is not the current production and the involved rules in
		 * the heads also do not fit, then we are on a wrong pass here. We need
		 * to return a failure.
		 */
		if ((m == null) && (!h.getProduction().equals(production))
				&& (!h.getInvolvedSet().contains(production))) {
			return MemoEntry.failed();
		}
		/*
		 * "Allow involved rules to be evaluated, but only once, during a
		 * seed-growing iteration."
		 * 
		 * If we have been here already, we check the eval set for the
		 * containing rule and evaluate it necessary. If the rule is within the
		 * eval set, we need to parse it once.
		 */
		if (h.getEvalSet().contains(production)) {
			h.getEvalSet().remove(production);
			final MemoEntry ans = eval(production, position, line);
			m.set(ans);
		}
		return m;
	}

	private MemoEntry growLR(String production, int position, int line,
			final MemoEntry m, final Head head) throws TreeException,
			ParserException {
		printMessage("Growing: " + production, position, line);
		/*
		 * We need to mark that at position a seed growing takes place with the
		 * head rule.
		 */
		heads.put(position, head);
		while (true) {
			/*
			 * Set all involved production into evaluation status.
			 */
			head.setEvalSet(head.getInvolvedSet());
			/*
			 * Evaluate production.
			 */
			final MemoEntry ans = eval(production, position, line);
			if (ans.getAnswer().equals(Status.FAILED)
					|| (ans.getDeltaPosition() <= m.getDeltaPosition())) {
				break;
			}
			m.set(ans);
		}
		/*
		 * Delete head from head buffer to signal end of seed growing.
		 */
		heads.remove(position);
		printMessage("End of growing: " + production, position, line);
		return m;
	}

	private MemoEntry lrAnswer(String production, int position, int line,
			final MemoEntry m) throws TreeException, ParserException {
		final LR lr = (LR) m.getAnswer();
		final Head h = lr.getHead();
		MemoEntry seed = lr.getSeed();
		if (!h.getProduction().equals(production)) {
			return seed;
		} else {
			m.set(seed);
			if (m.getAnswer().equals(Status.FAILED)) {
				return MemoEntry.failed();
			} else {
				return growLR(production, position, line, m, h);
			}
		}
	}

	/**
	 * This method evaluates the production given by it's name. The different
	 * choices are tried from the first to the last. The first choice matching
	 * is returned.
	 * 
	 * <b><i>Attention:</i></b> There was a test to introduce a parse where the
	 * alternative with the largest progress is returned. <b>This does not
	 * work!</b> During the parsing of a succeeding alternative a lot of states
	 * are changed on the way. By trying another alternative, the parser gets
	 * confused by some inconsistent information.
	 * 
	 * @param productionName
	 *            is the name of the production to be evaluated.
	 * @param position
	 *            is the position within the stream to evaluate the production
	 *            at.
	 * @return A MemoEntry object is returned containing the result.
	 * @throws ParserException
	 * @throws TreeException
	 */
	private MemoEntry eval(String productionName, int position, int line)
			throws ParserException, TreeException {
		MemoEntry maxProgress = MemoEntry.failed();
		for (Production production : grammar.getProductions().get(
				productionName)) {
			MemoEntry progress = parseProduction(production, position, line);
			if (progress.getAnswer() instanceof ParserTree) {
				if ((maxProgress.getAnswer() == Status.FAILED)
						|| (maxProgress.getDeltaPosition() < progress
								.getDeltaPosition())) {
					maxProgress = progress;
				}
			}
		}
		return maxProgress;
	}

	/**
	 * This method performs the actual parsing by reading the production and
	 * applying token definitions and starting other non terminal parsings.
	 * 
	 * @param production
	 *            is the production to be applied.
	 * @param position
	 *            is the position within the stream to evaluate the production
	 *            at.
	 * @return A MemoEntry object is returned containing the result.
	 * @throws TreeException
	 * @throws ParserException
	 */
	private MemoEntry parseProduction(Production production, int position,
			int line) throws TreeException, ParserException {
		ParserTree node = new ParserTree(production);
		MemoEntry progress = MemoEntry.success(0, 0, node);
		for (Construction construction : production.getConstructions()) {
			processIgnoredLeadingTokens(node, position, line, progress);
			if (construction.isNonTerminal()) {
				MemoEntry newProgress = applyRule(construction.getName(),
						position + progress.getDeltaPosition(),
						line + progress.getDeltaLine());
				if (newProgress.getAnswer() instanceof ParserTree) {
					ParserTree child = (ParserTree) newProgress.getAnswer();
					if (child.isNode()) {
						if (child.isStackingAllowed()) {
							node.addChild(child);
						} else {
							if (node.getName().equals(child.getName())) {
								node.addChildren(child.getChildren());
							} else {
								node.addChild(child);
							}
						}
					} else {
						node.addChildren(child.getChildren());
					}
					progress.add(newProgress);
				} else if (newProgress.getAnswer().equals(Status.FAILED))
					return MemoEntry.failed();
			} else {
				MemoEntry newProgress = processTerminal(node,
						(Terminal) construction,
						position + progress.getDeltaPosition(),
						line + progress.getDeltaLine());
				if (newProgress.getAnswer() instanceof ParserTree) {
					progress.add(newProgress);
				} else {
					return MemoEntry.failed();
				}
			}
			processIgnoredTrailingTokens(node, position, line, progress);
		}
		indentLine();
		if (logger.isTraceEnabled()) {
			logger.trace("Parsed: " + production);
		}
		return progress;
	}

	/**
	 * This method processes leading tokens which are either hidden or ignored.
	 * The processing only happens if the configuration allows it.
	 * 
	 * @param node
	 * @param position
	 * @param id
	 * @param line
	 * @param progress
	 * @throws TreeException
	 * @throws ParserException
	 */
	private void processIgnoredLeadingTokens(ParserTree node, int position,
			int line, MemoEntry progress) throws TreeException, ParserException {
		if (ignoredLeading) {
			processIgnoredTokens(node, position, line, progress);
		}
	}

	/**
	 * This method processes trailing tokens which are either hidden or ignored.
	 * The processing only happens if the configuration allows it.
	 * 
	 * @param node
	 * @param position
	 * @param id
	 * @param line
	 * @param progress
	 * @throws TreeException
	 * @throws ParserException
	 */
	private void processIgnoredTrailingTokens(ParserTree node, int position,
			int line, MemoEntry progress) throws TreeException, ParserException {
		if (!ignoredLeading) {
			processIgnoredTokens(node, position, line, progress);
		}
	}

	/**
	 * <p>
	 * This method reads all hidden and ignored tokens from the text and puts
	 * them into the node as children.
	 * </p>
	 * <p>
	 * This is the non-recursive part of the procedure to be called by the
	 * packrat parser. The procedure itself is implemented recursively in
	 * {@link #processIgnoredTrailingTokens(ParserTree, int, int, MemoEntry)}.
	 * </p>
	 * <p>
	 * Attention: This method is package private for testing purposes!
	 * </p>
	 * 
	 * @param node
	 *            is the current node in the {@link ParserTree}
	 * @param position
	 *            is the current parsing position.
	 * @throws TreeException
	 * @throws ParserException
	 */
	private void processIgnoredTokens(ParserTree node, int position, int line,
			MemoEntry progress) throws TreeException, ParserException {
		MemoEntry newProgress = processIgnoredTokens(node,
				position + progress.getDeltaPosition(),
				line + progress.getDeltaLine());
		if ((!newProgress.getAnswer().equals(Status.FAILED)))
			progress.add(newProgress);
	}

	/**
	 * This method processes a single terminal. This method uses
	 * processTokenDefinition to do this. The information to be put into that
	 * method is extracted and prepared here.
	 * 
	 * @param node
	 * @param terminal
	 * @param position
	 * @return
	 * @throws TreeException
	 */
	private MemoEntry processTerminal(ParserTree node, Terminal terminal,
			int position, int line) throws TreeException {
		printMessage("applyTerminal: " + terminal, position, line);
		TokenDefinitionSet tokenDefinitions = grammar.getTokenDefinitions();
		TokenDefinition tokenDefinition = tokenDefinitions
				.getDefinition(terminal.getName());
		MemoEntry result = processTokenDefinition(node, tokenDefinition,
				position, line);
		if (result == null) {
			throw new RuntimeException("There should be a result not null!");
		}
		printMessage(
				"applied Terminal '" + terminal + "' (" + result.getAnswer()
						+ ").", position, line);
		return result;
	}

	/**
	 * <p>
	 * This class reads all hidden and ignored tokens from the text and puts
	 * them into the node as children.
	 * </p>
	 * <p>
	 * This is the recursive part of the procedure.
	 * </p>
	 * <p>
	 * Attention: This method is package private for testing purposes!
	 * </p>
	 * <p>
	 * 
	 * @param node
	 * @param position
	 * @return
	 * @throws TreeException
	 * @throws ParserException
	 */
	MemoEntry processIgnoredTokens(ParserTree node, int position, int line)
			throws TreeException, ParserException {
		MemoEntry progress = MemoEntry.success(0, 0, node);
		MemoEntry newProgress = MemoEntry.success(0, 0, null);
		do {
			for (TokenDefinition tokenDefinition : hiddenAndIgnoredTokens) {
				newProgress = processTokenDefinition(node, tokenDefinition,
						position + progress.getDeltaPosition(),
						line + progress.getDeltaLine());
				if (!newProgress.getAnswer().equals(Status.FAILED)) {
					progress.add(newProgress);
					break;
				}
			}
		} while (newProgress.getDeltaPosition() > 0);
		return progress;
	}

	/**
	 * This method tries to process a single token definition. If this can be
	 * done, true is returned, a new parser tree child is added and all internal
	 * states are updated like position, id and line.
	 * 
	 * @param parserTree
	 * @param tokenDefinition
	 * @return
	 * @throws TreeException
	 */
	private MemoEntry processTokenDefinition(ParserTree node,
			TokenDefinition tokenDefinition, int position, int line)
			throws TreeException {
		Matcher matcher = tokenDefinition.getPattern().matcher(
				text.substring(position));
		if (!matcher.find()) {
			return MemoEntry.failed();
		}
		String match = matcher.group();
		int lineBreakNum = StringUtils.countLineBreaks(match);
		SourceCodeLocation source = sourceCode.getLines().get(line - 1)
				.getSource();
		int lineNumber = textWithSource.getLineNumber(position);
		TokenMetaData metaData = new TokenMetaData(source, lineNumber,
				lineBreakNum + 1, textWithSource.getColumn(position));
		Token token = new Token(tokenDefinition.getName(), match,
				tokenDefinition.getVisibility(), metaData);
		ParserTree myTree = new ParserTree(token);
		node.addChild(myTree);
		if (maxPosition < position + match.length()) {
			maxPosition = position + match.length();
		}
		return MemoEntry.success(match.length(), lineBreakNum, myTree);
	}

	/**
	 * <p>
	 * This method walks the whole tree and normalizes all children to point to
	 * their new parents. This is needed due to continuous changes of subtrees
	 * during packrat parsing. The answers from the memoization are added
	 * several times to different nodes. So the parent gets set to the latest
	 * parent, but this is not necessarily the right.
	 * </p>
	 * <p>
	 * For the parsing process itself it is not a big deal due to it is always
	 * looked from the parent to the child, but not upward. For later tree
	 * processing we need this normalization.
	 * </p>
	 * 
	 * @param tree
	 *            is the tree to be normalized.
	 * @throws ParserException
	 */
	private void normalizeParents(ParserTree tree) throws ParserException {
		try {
			final Field parentField = ParserTree.class
					.getDeclaredField("parent");
			parentField.setAccessible(true);
			TreeVisitor<ParserTree> visitor = new TreeVisitor<ParserTree>() {

				@Override
				public WalkingAction visit(ParserTree tree) {
					try {
						for (ParserTree child : tree.getChildren()) {
							parentField.set(child, tree);
						}
						return WalkingAction.PROCEED;
					} catch (IllegalArgumentException e) {
						throw new IllegalStateException(
								"Could not set new parent!", e);
					} catch (IllegalAccessException e) {
						throw new IllegalStateException(
								"Could not set new parent!", e);
					}
				}
			};
			new TreeWalker<ParserTree>(tree).walk(visitor);
			parentField.setAccessible(false);
		} catch (SecurityException e) {
			throw new ParserException("Could not normalize the parser tree.", e);
		} catch (NoSuchFieldException e) {
			throw new ParserException("Could not normalize the parser tree.", e);
		} catch (IllegalStateException e) {
			throw new ParserException("Could not normalize the parser tree.", e);
		}
	}
}
