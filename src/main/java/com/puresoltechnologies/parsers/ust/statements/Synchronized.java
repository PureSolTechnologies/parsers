package com.puresoltechnologies.parsers.ust.statements;

/**
 * This is a synchronized statement for critical sections.
 * 
 * @author Rick-Rainer Ludwig
 */
public class Synchronized extends AbstractStatement {

	private static final long serialVersionUID = -81125348959215787L;

	public Synchronized(String originalSymbol) {
		super("Synchronized Statement", originalSymbol);
	}
}
