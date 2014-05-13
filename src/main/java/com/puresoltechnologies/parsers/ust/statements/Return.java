package com.puresoltechnologies.parsers.ust.statements;

/**
 * This is the return statement which finishes a method or function and which
 * might carry a return value.
 * 
 * @author Rick-Rainer Ludwig
 */
public class Return extends AbstractStatement {

	private static final long serialVersionUID = -8944859904478474768L;

	public Return(String originalSymbol) {
		super("Return Statement", originalSymbol);
	}
}
