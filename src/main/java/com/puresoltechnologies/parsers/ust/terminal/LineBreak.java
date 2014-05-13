package com.puresoltechnologies.parsers.ust.terminal;


public class LineBreak extends AbstractTerminal {

	private static final long serialVersionUID = 5937521937552494525L;

	public LineBreak(String name, String content, int line, int column) {
		super(name, content, line, column, true);
	}

}
