package com.puresoltechnologies.parsers.ust.expressions;

public class Multiplication extends AbstractBinaryOperator {

	private static final long serialVersionUID = -1577433621788208449L;

	public Multiplication(String originalSymbol, Expression factor1,
			Expression factor2) {
		super("Multiplication", originalSymbol, factor1, factor2);
	}
}
