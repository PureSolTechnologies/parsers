package com.puresoltechnologies.parsers.ust.expressions;

public class ConditionalAssignment extends AbstractTertiaryOperator {

	private static final long serialVersionUID = 6754414167669177011L;

	public ConditionalAssignment(String originalSymbol, Expression target,
			Expression trueAlternative, Expression falseAlternative) {
		super("Conditional Assignment", originalSymbol, target,
				trueAlternative, falseAlternative);
	}
}
