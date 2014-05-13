package com.puresoltechnologies.parsers.ust.expressions;

public abstract class AbstractBinaryOperator extends AbstractExpression {

	private static final long serialVersionUID = -6854009238011625739L;

	private final Expression operand1;
	private final Expression operand2;

	public AbstractBinaryOperator(String name, String originalSymbol,
			Expression operand1, Expression operand2) {
		super(name, originalSymbol, operand1, operand2);
		this.operand1 = operand1;
		this.operand2 = operand2;
	}

	public final Expression getOperand1() {
		return operand1;
	}

	public final Expression getOperand2() {
		return operand2;
	}
}
