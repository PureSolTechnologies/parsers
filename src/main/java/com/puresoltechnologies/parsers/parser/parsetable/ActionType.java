package com.puresoltechnologies.parsers.parser.parsetable;

import java.io.Serializable;

/**
 * This enumeration contains all possible parser actions and their string
 * representations.
 * 
 * The order of the enum constants need to be ACCEPT, REDUCE, SHIFT, GOTO and
 * ERROR. This is used to get the maximum speed automatically in most cases.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public enum ActionType implements Serializable {

	/*
	 * The order of the constants must not be changed without deeper thought! In
	 * most situations the parser will proceed fastest with shift to get most of
	 * the file read.
	 */
	ACCEPT("acc"), SHIFT("s"), REDUCE("r"), GOTO(""), ERROR("err");

	private final String text;

	private ActionType(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return text;
	}
}
