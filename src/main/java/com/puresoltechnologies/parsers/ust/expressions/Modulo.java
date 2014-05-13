package com.puresoltechnologies.parsers.ust.expressions;

public class Modulo extends AbstractBinaryOperator {

	private static final long serialVersionUID = -1577433621788208449L;

	public Modulo(String originalSymbol, Expression divident, Expression divisor) {
		super("Modulo", originalSymbol, divident, divisor);
	}
}
