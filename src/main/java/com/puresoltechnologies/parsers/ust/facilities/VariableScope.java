package com.puresoltechnologies.parsers.ust.facilities;

import com.puresoltechnologies.parsers.ust.AbstractProduction;

/**
 * This tree element signals a scope for variable and element validity. This
 * scope is introduced everywhere where a new scope is starting. The elements
 * within the scope are children of the this class.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class VariableScope extends AbstractProduction {

	private static final long serialVersionUID = 7165390340532196126L;

	public VariableScope(AbstractProduction block) {
		super("Variable Scope", "variable scope", block);
	}
}
