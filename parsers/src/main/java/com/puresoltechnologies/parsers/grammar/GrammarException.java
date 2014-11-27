package com.puresoltechnologies.parsers.grammar;

public class GrammarException extends Exception {

    private static final long serialVersionUID = -3264512209176857531L;

    public GrammarException(String message) {
	super(message);
    }

    public GrammarException(String message, Exception exception) {
	super(message, exception);
    }

}
