package com.puresoltechnologies.parsers.ust.literals;

public class IntegerLiteral extends NumericalLiteral {

	private static final long serialVersionUID = -5622529938750219170L;

	private final int bitLength;
	private final long value;

	public IntegerLiteral(String originalSymbol, int bitLength, long value) {
		super(bitLength + "bit Integer Literal", originalSymbol);
		this.bitLength = bitLength;
		this.value = value;
	}

	public int getBitLength() {
		return bitLength;
	}

	public long getValue() {
		return value;
	}
}
