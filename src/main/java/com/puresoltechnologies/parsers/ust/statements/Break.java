package com.puresoltechnologies.parsers.ust.statements;

/**
 * This is a break statement which breaks out of a loop. In some languages this
 * is also called <code>last</code>.
 * 
 * @author Rick-Rainer Ludwig
 */
public class Break extends AbstractStatement {

	private static final long serialVersionUID = 7552943875590939150L;

	public Break(String originalSymbol) {
		super("Break Statement", originalSymbol);
	}
}
