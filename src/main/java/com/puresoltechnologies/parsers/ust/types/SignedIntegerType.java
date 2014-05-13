package com.puresoltechnologies.parsers.ust.types;

/**
 * This class represents a signed integer type with variable to set bit length.
 * 
 * @author ludwig
 * 
 */
public class SignedIntegerType extends AbstractIntegerType {

	private static final long serialVersionUID = 2322028164241606186L;

	public SignedIntegerType(String originalSymbol, int bitLength) {
		super("Signed " + bitLength + "bit Integer Type", originalSymbol,
				bitLength);
	}
}
