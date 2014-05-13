package com.puresoltechnologies.parsers.lexer;

import java.util.ArrayList;

public class TokenStream extends ArrayList<Token> {

    private static final long serialVersionUID = 4992743487086731635L;

    public TokenStream() {
	super();
    }

    /**
     * This method prepares a code sample out of a position defined by parameter
     * position. This code sample marks the position and prints code around it.
     * 
     * @param position
     *            is the token id which is to be highlighted. The code around it
     *            is printed, too, to give the user a glue where to find the
     *            position in the source.
     * @return
     */
    public String getCodeSample(int position) {
	StringBuffer buffer = new StringBuffer();
	int startPosition = Math.max(0, position - 10);
	if (startPosition >= size()) {
	    startPosition = 0;
	}
	int stopPosition = Math.min(size() - 1, startPosition + 20);
	if (startPosition > 0) {
	    buffer.append("[...]");
	}
	for (int i = startPosition; i <= stopPosition; i++) {
	    if (i == position) {
		buffer.append(" >>> ");
	    }
	    buffer.append(get(i).getText());
	    if (i == position) {
		buffer.append(" <<< ");
	    }
	}
	if (stopPosition < size() - 1) {
	    buffer.append("[...]");
	}
	return buffer.toString();
    }

}
