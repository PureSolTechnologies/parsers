package com.puresoltechnologies.parsers.ust;

import java.io.Serializable;

public class UniversalSyntaxTreeMetaData implements Serializable {

	private static final long serialVersionUID = -5461785672594531933L;

	private final int line;
	private final int lineNum;
	private final int column;
	private final int length;

	public UniversalSyntaxTreeMetaData(int line, int lineNum, int column,
			int length) {
		super();
		this.line = line;
		this.lineNum = lineNum;
		this.column = column;
		this.length = length;
	}

	public int getLine() {
		return line;
	}

	public int getLineNum() {
		return lineNum;
	}

	public int getColumn() {
		return column;
	}

	public int getLength() {
		return length;
	}

}
