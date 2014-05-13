package com.puresoltechnologies.parsers.ust.statements;

import com.puresoltechnologies.parsers.ust.AbstractProduction;

public class GotoLabel extends AbstractProduction {

	private static final long serialVersionUID = 5969840612811092250L;

	public GotoLabel(String originalSymbol) {
		super("Goto Label", originalSymbol);
	}
}
