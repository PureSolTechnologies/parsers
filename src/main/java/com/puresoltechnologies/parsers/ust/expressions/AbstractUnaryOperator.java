package com.puresoltechnologies.parsers.ust.expressions;

public abstract class AbstractUnaryOperator extends AbstractExpression {

	private static final long serialVersionUID = -6854009238011625739L;

	private final Expression operand;

	public AbstractUnaryOperator(String name, String originalSymbol,
			Expression operand) {
		super(name, originalSymbol);
		this.operand = operand;
	}

	public final Expression getOperand() {
		return operand;
	}
}
