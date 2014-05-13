package com.puresoltechnologies.parsers.ust.literals;

public class FloatingPointLiteral extends NumericalLiteral {

	private static final long serialVersionUID = -5622529938750219170L;

	private final int bitLength;
	private final double value;

	public FloatingPointLiteral(String originalSymbol, int bitLength,
			double value) {
		super(bitLength + "bit Floating Point Literal", originalSymbol);
		this.bitLength = bitLength;
		this.value = value;
	}

	public int getBitLength() {
		return bitLength;
	}

	public double getValue() {
		return value;
	}
}
