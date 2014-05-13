package com.puresoltechnologies.parsers.ust.statements;

/**
 * This is a next statement which continues to the next loop iteration. In some
 * languages this is also called <code>next</code>.
 * 
 * @author Rick-Rainer Ludwig
 */
public class Continue extends AbstractStatement {

	private static final long serialVersionUID = -2327674896241450199L;

	public Continue(String originalSymbol) {
		super("Next Statement", originalSymbol);
	}
}
