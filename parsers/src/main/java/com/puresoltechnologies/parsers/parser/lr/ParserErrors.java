package com.puresoltechnologies.parsers.parser.lr;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ParserErrors implements Serializable {

    private static final long serialVersionUID = -7138189685638152264L;

    private final Map<Integer, Integer> errorStates = new HashMap<Integer, Integer>();

    public ParserErrors() {
	super();
    }

    public void addError(int state) {
	if (errorStates.containsKey(state)) {
	    errorStates.put(state, errorStates.get(state) + 1);
	} else {
	    errorStates.put(state, 1);
	}
    }

    @Override
    public String toString() {
	StringBuffer buffer = new StringBuffer();
	for (Entry<Integer, Integer> errorState : errorStates.entrySet()) {
	    Integer state = errorState.getKey();
	    buffer.append("State ");
	    buffer.append(state);
	    buffer.append(":\t");
	    buffer.append(errorState.getValue());
	    buffer.append("\n");
	}
	return buffer.toString();
    }

    public void clear() {
	errorStates.clear();
    }
}
