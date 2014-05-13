package com.puresoltechnologies.parsers.ust.expressions;

public class Division extends AbstractBinaryOperator {

	private static final long serialVersionUID = -1577433621788208449L;

	public Division(String originalSymbol, Expression divident,
			Expression divisor) {
		super("Division", originalSymbol, divident, divisor);
	}
}
