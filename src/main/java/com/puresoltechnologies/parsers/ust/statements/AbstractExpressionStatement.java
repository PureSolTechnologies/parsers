package com.puresoltechnologies.parsers.ust.statements;

/**
 * This is an abstract implementation of an expression statement
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public abstract class AbstractExpressionStatement extends AbstractStatement {

	private static final long serialVersionUID = 1734223272528966860L;

	public AbstractExpressionStatement(String name, String originalSymbol) {
		super(name, originalSymbol);
	}
}
