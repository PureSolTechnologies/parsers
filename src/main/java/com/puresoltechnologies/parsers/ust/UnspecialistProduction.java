package com.puresoltechnologies.parsers.ust;

import java.util.List;

/**
 * This class is used in cases where no special production can be used for
 * example in transition productions.
 * 
 * @author Rick-Rainer Ludwig
 */
public class UnspecialistProduction extends AbstractProduction {

	private static final long serialVersionUID = -820255465228987814L;

	public UnspecialistProduction(String name, String originalName,
			List<UniversalSyntaxTree> children) {
		super(name, originalName, children);
	}

}
