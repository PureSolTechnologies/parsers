package com.puresoltechnologies.parsers.ust;

import java.util.List;

public class CompilationUnit extends AbstractProduction {

	private static final long serialVersionUID = -5790049234290910253L;

	public CompilationUnit(String name, String content,
			List<UniversalSyntaxTree> ustChildren) {
		super(name, content, ustChildren);
	}

}
