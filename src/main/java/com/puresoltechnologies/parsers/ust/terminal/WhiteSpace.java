package com.puresoltechnologies.parsers.ust.terminal;

public class WhiteSpace extends AbstractTerminal {

	private static final long serialVersionUID = -4930916064232367782L;

	public WhiteSpace(String name, String content, int line, int column) {
		super(name, content, line, column, false);
	}

}
