package com.puresoltechnologies.parsers.ust.expressions;

public class Exponent extends AbstractBinaryOperator {

	private static final long serialVersionUID = -1577433621788208449L;

	public Exponent(String originalSymbol, Expression mantissa,
			Expression exponent) {
		super("Exponent", originalSymbol, mantissa, exponent);
	}
}
