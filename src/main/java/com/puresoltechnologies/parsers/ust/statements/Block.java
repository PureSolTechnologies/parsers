package com.puresoltechnologies.parsers.ust.statements;

import java.util.List;

public class Block extends AbstractStatement implements Statement {

	private static final long serialVersionUID = 5873944700707434142L;

	public Block(String originalSymbol, List<Statement> statements) {
		super("Block Statement", originalSymbol, statements
				.toArray(new Statement[statements.size()]));
	}
}
