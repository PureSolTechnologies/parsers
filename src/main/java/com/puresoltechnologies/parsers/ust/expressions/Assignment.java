package com.puresoltechnologies.parsers.ust.expressions;

public class Assignment extends AssignmentExpression {

	private static final long serialVersionUID = -1577433621788208449L;

	public Assignment(String originalSymbol, AbstractExpression target,
			AbstractExpression value) {
		super("Assignment", originalSymbol, target, value);
	}
}
