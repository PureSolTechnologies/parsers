package com.puresoltechnologies.parsers.ust.types;

/**
 * This class is a prototype for all kind of integer types (signed and
 * unsigned).
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public abstract class AbstractIntegerType extends AbstractNumericType {

	private static final long serialVersionUID = 1208013511094715942L;

	public AbstractIntegerType(String name, String originalSymbol, int bitLength) {
		super(name, originalSymbol, bitLength);
	}

}
