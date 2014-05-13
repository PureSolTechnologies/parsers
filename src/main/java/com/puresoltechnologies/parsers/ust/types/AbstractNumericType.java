package com.puresoltechnologies.parsers.ust.types;

/**
 * This is an abstract numeric type which is a just prototype.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public abstract class AbstractNumericType extends AbstractType {

	private static final long serialVersionUID = -348395824381107195L;

	private final int bitLength;

	public AbstractNumericType(String name, String originalSymbol, int bitLength) {
		super(name, originalSymbol);
		this.bitLength = bitLength;
	}

	public final int getBitLength() {
		return bitLength;
	}

}
