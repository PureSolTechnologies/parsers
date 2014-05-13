package com.puresoltechnologies.parsers.ust.literals;

public class CharacterLiteral extends NumericalLiteral {

	private static final long serialVersionUID = -5622529938750219170L;

	private final char value;

	public CharacterLiteral(String originalSymbol, char value) {
		super("Character Literal", originalSymbol);
		this.value = value;
	}

	public char getValue() {
		return value;
	}
}
