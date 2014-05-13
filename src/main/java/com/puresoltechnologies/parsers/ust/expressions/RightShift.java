package com.puresoltechnologies.parsers.ust.expressions;

public class RightShift extends AbstractBinaryOperator {

	private static final long serialVersionUID = -1577433621788208449L;

	public RightShift(String originalSymbol, Expression operand,
			Expression shiftWidth) {
		super("Right Shift", originalSymbol, operand, shiftWidth);
	}
}
