package com.puresoltechnologies.parsers.ust.types;

/**
 * This type is for logical values. This type has different names in different
 * languages:
 * 
 * <pre>
 * 	bool - C++
 *  boolean - Java
 *  logical - Fortran
 * </pre>
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class LogicalType extends AbstractType {

	private static final long serialVersionUID = -5227270180409950680L;

	public LogicalType(String originalSymbol) {
		super("Logical Type", originalSymbol);
	}
}
