package com.puresoltechnologies.parsers.ust.statements;

/**
 * This is an abstract implementation for all kind of loops which have a tail
 * condition. That means, that a condition is checked whether to run the loop
 * body again after the loop body was entered at least one time.
 * 
 * @author Rick-Rainer Ludwig
 */
public abstract class AbstractTailConditionLoop extends AbstractLoop {

	private static final long serialVersionUID = 556020073871576482L;

	public AbstractTailConditionLoop(String name, String originalSymbol) {
		super(name, originalSymbol);
	}

}
