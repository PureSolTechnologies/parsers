package com.puresoltechnologies.parsers.source;

public class StringWithLocation {

	private final String text;
	private final SourceCodeLine[] sourceLineReferences;
	private final int[] columns;

	public StringWithLocation(SourceCode sourceCode) {
		super();
		StringBuffer buffer = new StringBuffer();
		for (SourceCodeLine line : sourceCode.getLines()) {
			buffer.append(line.getLine());
		}
		this.text = buffer.toString();

		sourceLineReferences = new SourceCodeLine[text.length()];
		int position = 0;
		for (SourceCodeLine line : sourceCode.getLines()) {
			for (int i = 0; i < line.getLine().length(); i++) {
				sourceLineReferences[position] = line;
				position++;
			}
		}
		columns = new int[text.length()];
		position = 0;
		for (SourceCodeLine line : sourceCode.getLines()) {
			for (int column = 0; column < line.getLine().length(); column++) {
				columns[position] = column;
				position++;
			}
		}
	}

	public String getText() {
		return text;
	}

	public SourceCodeLine getSource(int position) {
		return sourceLineReferences[position];
	}

	public int getLineNumber(int position) {
		return sourceLineReferences[position].getLineNumber();
	}

	public int getColumn(int position) {
		return columns[position];
	}

}
