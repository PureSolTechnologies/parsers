package com.puresoltechnologies.parsers.ust.terminal;


public class Comment extends AbstractTerminal {

	private static final long serialVersionUID = -5436207235168500593L;

	public Comment(String name, String content, int line, int column) {
		super(name, content, line, column, false);
	}

}
