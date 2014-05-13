package com.puresoltechnologies.parsers.ust.statements;

/**
 * This is a throw statement which throws exceptions.
 * 
 * @author Rick-Rainer Ludwig
 */
public class ThrowStatement extends AbstractStatement {

	private static final long serialVersionUID = -197165528870354560L;

	public ThrowStatement(String originalSymbol) {
		super("Throw Statement", originalSymbol);
	}
}
