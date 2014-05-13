package com.puresoltechnologies.parsers.ust.eval;

/**
 * This exception is thrown in cases of invalid values.
 * 
 * @author Rick-Rainer Ludwig
 */
public class ValueTypeException extends Exception {

    private static final long serialVersionUID = 7192663199525590048L;

    public ValueTypeException() {
	super();
    }

    public ValueTypeException(String message, Throwable cause) {
	super(message, cause);
    }

    public ValueTypeException(String message) {
	super(message);
    }

    public ValueTypeException(Throwable cause) {
	super(cause);
    }

}
