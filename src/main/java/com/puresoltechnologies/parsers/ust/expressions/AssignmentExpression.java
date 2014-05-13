package com.puresoltechnologies.parsers.ust.expressions;

public abstract class AssignmentExpression extends AbstractBinaryOperator {

	private static final long serialVersionUID = 4249485571689334300L;

	public AssignmentExpression(String name, String originalSymbol,
			Expression operand1, Expression operand2) {
		super(name, originalSymbol, operand1, operand2);
	}

	public AssignmentExpression(String originalSymbol, Expression operand1,
			Expression operand2) {
		this("AssignmentExpression", originalSymbol, operand1, operand2);
	}

}
