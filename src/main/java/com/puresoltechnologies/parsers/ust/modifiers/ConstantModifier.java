package com.puresoltechnologies.parsers.ust.modifiers;

/**
 * This class represents a modifier which sets a variable to immutable. This
 * modifier has several name in different languages:
 * 
 * <pre>
 * Java: final
 * C/C++: const
 * Fortran: parameter
 * </pre>
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class ConstantModifier extends AbstractModifier {

	private static final long serialVersionUID = -5800466279299023817L;

	public ConstantModifier(String originalSymbol) {
		super("Constant Modifier", originalSymbol);
	}
}
