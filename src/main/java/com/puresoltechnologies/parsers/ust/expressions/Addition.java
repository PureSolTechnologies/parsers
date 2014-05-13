package com.puresoltechnologies.parsers.ust.expressions;

public class Addition extends AbstractBinaryOperator {

	private static final long serialVersionUID = -1577433621788208449L;

	public Addition(String originalSymbol, Expression summand1,
			Expression summand2) {
		super("Addition", originalSymbol, summand1, summand2);
	}
}
