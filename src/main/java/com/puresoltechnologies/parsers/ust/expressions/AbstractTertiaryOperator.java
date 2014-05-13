package com.puresoltechnologies.parsers.ust.expressions;

public abstract class AbstractTertiaryOperator extends AbstractExpression {

	private static final long serialVersionUID = -6854009238011625739L;

	private final Expression operand1;
	private final Expression operand2;
	private final Expression operand3;

	public AbstractTertiaryOperator(String name, String originalSymbol,
			Expression operand1, Expression operand2, Expression operand3) {
		super(name, originalSymbol, operand1, operand2, operand3);
		this.operand1 = operand1;
		this.operand2 = operand2;
		this.operand3 = operand3;
	}

	public final Expression getOperand1() {
		return operand1;
	}

	public final Expression getOperand2() {
		return operand2;
	}

	public final Expression getOperand3() {
		return operand3;
	}
}
