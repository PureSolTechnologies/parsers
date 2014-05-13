package com.puresoltechnologies.parsers.ust.statements;

/**
 * This is an abstract implementation for all kind of loops.
 * 
 * @author Rick-Rainer Ludwig
 */
public abstract class AbstractLoop extends AbstractStatement {

	private static final long serialVersionUID = 6821436133082697055L;

	public AbstractLoop(String name, String originalSymbol) {
		super(name, originalSymbol);
	}

}
