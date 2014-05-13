package com.puresoltechnologies.parsers.parser.items;

import com.puresoltechnologies.commons.misc.ObjectUtilities;
import com.puresoltechnologies.parsers.grammar.production.Construction;
import com.puresoltechnologies.parsers.grammar.production.Production;

/**
 * This class represents a single item defined in the Dragon Book for keeping
 * the current parsing state for a production.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class LR0Item implements Item {

	private static final long serialVersionUID = 8522602012550565562L;

	/**
	 * This is the reference id for the production observed.
	 */
	private final Production production;
	/**
	 * This is the current position. Zero is the left most position which is at
	 * the beginning of the parsing process.
	 */
	private final int position;
	private final int hashCode;

	public LR0Item(Production production, int position) {
		this.production = production;
		this.position = position;
		hashCode = ObjectUtilities.calculateConstantHashCode(production,
				position);
	}

	/**
	 * @return the productionId
	 */
	@Override
	public Production getProduction() {
		return production;
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public Construction getNext() {
		if (position >= production.getConstructions().size()) {
			return null;
		}
		return production.getConstructions().get(position);
	}

	@Override
	public boolean hasNext() {
		return (position < production.getConstructions().size());
	}

	@Override
	public Construction get2ndNext() {
		if (position + 1 >= production.getConstructions().size()) {
			return null;
		}
		return production.getConstructions().get(position + 1);
	}

	@Override
	public boolean has2ndNext() {
		return (position + 1 < production.getConstructions().size());
	}

	/**
	 * This method returns the part beta from definition:
	 * 
	 * <pre>
	 * &quot;A --&gt; alpha . B beta&quot;
	 * </pre>
	 * 
	 * @return
	 */
	public Production getBeta() {
		Production production = new Production("beta");
		for (int pos = getPosition() + 1; pos < getProduction()
				.getConstructions().size(); pos++) {
			production.addConstruction(getProduction().getConstructions().get(
					pos));
		}
		return production;
	}

	@Override
	public String toString() {
		return production.toShortString(position);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return hashCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LR0Item other = (LR0Item) obj;
		if (this.hashCode != other.hashCode) {
			return false;
		}
		if (position != other.position)
			return false;
		if (production == null) {
			if (other.production != null)
				return false;
		} else if (!production.equals(other.production))
			return false;
		return true;
	}

}
