package com.puresoltechnologies.parsers.grammar.production;

/**
 * This singleton class represents an empty terminal.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class EmptyTerminal {

	public static final String EMPTY_CONSTRUCTION_NAME = "_EMPTY_";

	private static volatile Terminal instance = null;

	public static Terminal getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	private static synchronized void createInstance() {
		if (instance == null) {
			instance = new Terminal(EMPTY_CONSTRUCTION_NAME, null);
		}
	}

}
