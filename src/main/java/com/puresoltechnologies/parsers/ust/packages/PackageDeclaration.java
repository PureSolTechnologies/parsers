package com.puresoltechnologies.parsers.ust.packages;

import com.puresoltechnologies.parsers.ust.AbstractProduction;

/**
 * This is a package declaration like found in Java, Scala, D and other
 * languages. This packaged declaration also has a connection to the project's
 * directory structure. name spaces in contrast do not have such a correlation.
 * For name spaces an own class is provided.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class PackageDeclaration extends AbstractProduction {

	private static final long serialVersionUID = -828922003801631227L;

	private final String packageName;

	public PackageDeclaration(String originalSymbol, String packageName) {
		super("Package Declaration", originalSymbol);
		this.packageName = packageName;
	}

	public final String getPackageName() {
		return packageName;
	}
}
