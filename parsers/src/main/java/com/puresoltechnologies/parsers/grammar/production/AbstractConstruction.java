package com.puresoltechnologies.parsers.grammar.production;

import com.puresoltechnologies.commons.types.ObjectUtilities;

/**
 * THIS CLASS IS NOT THREAD SAFE!!!
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public abstract class AbstractConstruction implements Construction {

	private static final long serialVersionUID = -8190479779174841005L;

	private final String name;
	private final boolean isTerminal;
	private final int hashCode;

	public AbstractConstruction(String name, boolean isTerminal) {
		super();
		this.name = name;
		this.isTerminal = isTerminal;
		hashCode = ObjectUtilities.calculateConstantHashCode(name, isTerminal);
	}

	/**
	 * @return the typeId
	 */
	@Override
	public final String getName() {
		return name;
	}

	@Override
	public final boolean isTerminal() {
		return isTerminal;
	}

	@Override
	public final boolean isNonTerminal() {
		return !isTerminal;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.puresoltechnologies.parser.impl.ParserRuleElementInterface#toString
	 * ()
	 */
	@Override
	public String toString() {
		if (isTerminal) {
			return name + ": (TERMINAL)";
		} else {
			return name + ": (NON-TERMINAL)";
		}
	}

	@Override
	public String toShortString() {
		return name;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AbstractConstruction other = (AbstractConstruction) obj;
		if (this.hashCode != other.hashCode) {
			return false;
		}
		if (isTerminal != other.isTerminal)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public int compareTo(Construction other) {
		if (other == null) {
			return 1;
		}
		int result = name.compareTo(((AbstractConstruction) other).name);
		if (result != 0) {
			return result;
		}
		return Boolean.valueOf(isTerminal()).compareTo(
				Boolean.valueOf(other.isTerminal()));
	}
}
