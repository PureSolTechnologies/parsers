package com.puresoltechnologies.parsers.ust.types;

/**
 * This class represents a floating point type. Floating point types have
 * different names and bit length in different languages:
 * 
 * <pre>
 * 	float (32 bit), double (64 bit) - C / C++, Java
 *  real (32 bit), double precision (64 bit) - Fortran
 * </pre>
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class FloatingPointType extends AbstractNumericType {

	private static final long serialVersionUID = 47713101164319131L;

	public FloatingPointType(String originalSymbol, int bitLength) {
		super(bitLength + "bit Floating Point Type", originalSymbol, bitLength);
	}
}
