package com.puresoltechnologies.parsers.parser.packrat;

import java.io.Serializable;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class represents a head for packrat parser to support indirect left
 * recursion.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class Head implements Serializable, Cloneable {

	private static final long serialVersionUID = 1217226903924359385L;

	/**
	 * Production for this head.
	 */
	private final String production;
	/**
	 * 
	 */
	private final Set<String> involvedSet = new LinkedHashSet<String>();
	/**
	 * 
	 */
	private final Set<String> evalSet = new LinkedHashSet<String>();

	/**
	 * This constructor creates a new head for packrat parser with an empty
	 * involvedSet and empty evalSet as used in setupLR.
	 * 
	 * @param production
	 */
	public Head(String production) {
		super();
		this.production = production;
	}

	String getProduction() {
		return production;
	}

	public void setInvolved(String production2) {
		involvedSet.clear();
		involvedSet.add(production);
	}

	void addInvolved(String production) {
		involvedSet.add(production);
	}

	Set<String> getInvolvedSet() {
		return involvedSet;
	}

	void addEvalSet(Set<String> involvedSet) {
		evalSet.addAll(involvedSet);
	}

	void setEvalSet(Set<String> involvedSet) {
		evalSet.clear();
		evalSet.addAll(involvedSet);
	}

	Set<String> getEvalSet() {
		return evalSet;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((evalSet == null) ? 0 : evalSet.hashCode());
		result = prime * result
				+ ((involvedSet == null) ? 0 : involvedSet.hashCode());
		result = prime * result
				+ ((production == null) ? 0 : production.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Head other = (Head) obj;
		if (evalSet == null) {
			if (other.evalSet != null)
				return false;
		} else if (!evalSet.equals(other.evalSet))
			return false;
		if (involvedSet == null) {
			if (other.involvedSet != null)
				return false;
		} else if (!involvedSet.equals(other.involvedSet))
			return false;
		if (production == null) {
			if (other.production != null)
				return false;
		} else if (!production.equals(other.production))
			return false;
		return true;
	}

	@Override
	public Head clone() {
		Head cloned = new Head(getProduction());
		cloned.evalSet.addAll(this.evalSet);
		cloned.involvedSet.addAll(this.involvedSet);
		return cloned;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(production);
		builder.append(" involved: (");
		for (String involved : involvedSet) {
			builder.append(" ");
			builder.append(involved);
		}
		builder.append(" )");
		builder.append(" eval: (");
		for (String eval : evalSet) {
			builder.append(" ");
			builder.append(eval);
		}
		builder.append(" )");
		return builder.toString();
	}
}
