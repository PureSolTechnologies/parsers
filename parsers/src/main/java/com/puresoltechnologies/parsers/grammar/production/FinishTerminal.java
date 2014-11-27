package com.puresoltechnologies.parsers.grammar.production;

/**
 * This singleton class represents the finishing terminal at the end of the
 * parsing process.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class FinishTerminal {

	public static final String FINISH_CONSTRUCTION_NAME = "_FINISH_";

	private static Terminal instance = null;

	public static Terminal getInstance() {
		if (instance == null) {
			createInstance();
		}
		return instance;
	}

	private static synchronized void createInstance() {
		if (instance == null) {
			instance = new Terminal(FINISH_CONSTRUCTION_NAME, null);
		}
	}

}
