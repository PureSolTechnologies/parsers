package com.puresoltechnologies.parsers.parser;

import java.io.Serializable;

import com.puresoltechnologies.commons.types.ObjectUtilities;
import com.puresoltechnologies.parsers.source.SourceCodeLocation;

/**
 * This class is used to store meta information for each node in an AST.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class ParserTreeMetaData implements Serializable, Cloneable {

	private static final long serialVersionUID = 7846977139079079374L;

	private final SourceCodeLocation source;
	private final int line;
	private final int lineNum;
	private final int hashcode;

	public ParserTreeMetaData(SourceCodeLocation source, int line, int lineNum) {
		super();
		this.source = source;
		this.line = line;
		this.lineNum = lineNum;

		hashcode = ObjectUtilities.calculateConstantHashCode(source, line,
				lineNum);
	}

	public SourceCodeLocation getSource() {
		return source;
	}

	public int getLine() {
		return line;
	}

	public int getLineNum() {
		return lineNum;
	}

	@Override
	public int hashCode() {
		return hashcode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ParserTreeMetaData other = (ParserTreeMetaData) obj;
		if (source == null) {
			if (other.source != null)
				return false;
		} else if (!source.equals(other.source))
			return false;
		if (line != other.line)
			return false;
		if (lineNum != other.lineNum)
			return false;
		return true;
	}

	@Override
	public String toString() {
		String string = source.toString() + ": " + line;
		if (lineNum > 1) {
			string += " - " + (line + lineNum - 1);
		}
		return string;
	}

	@Override
	public ParserTreeMetaData clone() {
		return new ParserTreeMetaData(source, line, lineNum);
	}
}
