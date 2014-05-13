package com.puresoltechnologies.parsers.ust.packages;

import com.puresoltechnologies.parsers.ust.AbstractProduction;

/**
 * This is a name space like found in C++ or C#. Name spaces do not have a name
 * to directory correlation like packages. For package declaration an own class
 * is provided. For name spaces an own class is provided.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class NameSpace extends AbstractProduction {

	private static final long serialVersionUID = 4747868405422668380L;

	private final String nameSpaceName;

	public NameSpace(String originalSymbol, String nameSpaceName) {
		super("Name Space", originalSymbol);
		this.nameSpaceName = nameSpaceName;
	}

	public final String getNameSpaceName() {
		return nameSpaceName;
	}
}
