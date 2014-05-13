package com.puresoltechnologies.parsers.parser.parsetable;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import java.util.Set;

import com.puresoltechnologies.parsers.grammar.GrammarException;
import com.puresoltechnologies.parsers.grammar.production.Construction;
import com.puresoltechnologies.parsers.grammar.production.NonTerminal;
import com.puresoltechnologies.parsers.grammar.production.Terminal;

/**
 * This is the general interface for parser tables.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public interface ParserTable extends Serializable {

	public Map<Construction, ParserActionSet> getPossibleActions(
			int currentState) throws GrammarException;

	/**
	 * This method returns the parse action for the current state id and the
	 * construction next in stream.
	 * 
	 * @param currentStateId
	 * @param construction
	 * @return
	 * @throws GrammarException
	 */
	public ParserAction getAction(int currentStateId, Construction construction)
			throws GrammarException;

	public ParserActionSet getActionSet(int currentState,
			Construction construction);

	public Set<Terminal> getActionTerminals();

	public Set<NonTerminal> getGotoNonTerminals();

	public int getStateCount();

	/**
	 * This method is used to generated maximum detail information about the
	 * parser table and it's internal information and the information about the
	 * creation.
	 * 
	 * @param directory
	 *            is the directory to put the information into.
	 * @throws IOException
	 *             is thrown in cases of unexpected environment conditions like
	 *             missing permissions for writing.
	 * @throws GrammarException
	 *             is thrown in cases of invalid grammars.
	 */
	public void generateInspectionInformation(File directory)
			throws IOException, GrammarException;
}
