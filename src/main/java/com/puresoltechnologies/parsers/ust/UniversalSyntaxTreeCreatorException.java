package com.puresoltechnologies.parsers.ust;

/**
 * This exception is thrown by UST creator classes.
 * 
 * @author Rick-Rainer Ludwig
 */
public class UniversalSyntaxTreeCreatorException extends RuntimeException {

	private static final long serialVersionUID = -977915535666544669L;

	public UniversalSyntaxTreeCreatorException(String message, Throwable cause) {
		super(message, cause);
	}

	public UniversalSyntaxTreeCreatorException(String message) {
		super(message);
	}

}
