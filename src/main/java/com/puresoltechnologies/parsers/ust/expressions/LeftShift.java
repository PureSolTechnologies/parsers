package com.puresoltechnologies.parsers.ust.expressions;

public class LeftShift extends AbstractBinaryOperator {

	private static final long serialVersionUID = -1577433621788208449L;

	public LeftShift(String originalSymbol, Expression operand,
			Expression shiftWidth) {
		super("Left Shift", originalSymbol, operand, shiftWidth);
	}
}
