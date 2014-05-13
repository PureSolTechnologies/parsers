package com.puresoltechnologies.parsers.ust.comments;

public class EndOfLineComment extends AbstractComment {

	private static final long serialVersionUID = 8664830280231683442L;

	private final String commentText;

	public EndOfLineComment(String originalSymbol, String commentText) {
		super("End-Of-Line Comment", originalSymbol);
		this.commentText = commentText;
	}

}
