package com.puresoltechnologies.parsers.ust.expressions;

public class DivisionAssignment extends AssignmentExpression {

	private static final long serialVersionUID = -1577433621788208449L;

	public DivisionAssignment(String originalSymbol, Expression target,
			Expression divisor) {
		super("Division Assignment", originalSymbol, target, divisor);
	}
}
