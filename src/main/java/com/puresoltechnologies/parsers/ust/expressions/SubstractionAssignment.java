package com.puresoltechnologies.parsers.ust.expressions;

public class SubstractionAssignment extends AssignmentExpression {

	private static final long serialVersionUID = -1577433621788208449L;

	public SubstractionAssignment(String originalSymbol, Expression target,
			Expression subtrahent) {
		super("Substraction Assignment", originalSymbol, target, subtrahent);
	}
}
