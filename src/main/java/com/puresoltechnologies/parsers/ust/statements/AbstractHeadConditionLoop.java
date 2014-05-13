package com.puresoltechnologies.parsers.ust.statements;

/**
 * This is an abstract implementation for all kind of loops which have a head
 * condition. That means, that a condition is checked whether to run the loop
 * body before the loop body is entered.
 * 
 * @author Rick-Rainer Ludwig
 */
public abstract class AbstractHeadConditionLoop extends AbstractLoop {

	private static final long serialVersionUID = -4625768744138060540L;

	public AbstractHeadConditionLoop(String name, String originalSymbol) {
		super(name, originalSymbol);
	}

}
