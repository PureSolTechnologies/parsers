package com.puresoltechnologies.parsers.grammar.production;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a single productions.
 * 
 * THIS CLASS IS NOT THREAD SAFE!!!
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class Production implements Serializable {

	private static final long serialVersionUID = -3776357938906717512L;

	private int id = -1;
	private final String name;
	private final String alternativeName;
	private final List<Construction> constructions = new ArrayList<Construction>();

	/**
	 * This flag is set to true if the current production is having an own node
	 * within the AST. Set false if the constructions of this productions are to
	 * be moved a node upward.
	 */
	private boolean node = true;

	/**
	 * This flag is used to set the permission of stacking the current
	 * production. Stacking means that in recursions for each iteration a node
	 * for this production is created. If this behavior is wanted the flag
	 * should be set to true. Otherwise false is set.
	 * 
	 * The default behavior is true.
	 */
	private boolean stackingAllowed = true;

	private boolean changed = true;
	private int hashCode = 0;

	public Production(String name) {
		super();
		this.name = name;
		this.alternativeName = name;
	}

	public Production(String name, String alternativeName) {
		super();
		this.name = name;
		this.alternativeName = alternativeName;
	}

	public void setId(int id) {
		if (this.id < 0) {
			this.id = id;
			changed = true;
		}
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the alternativeName
	 */
	public String getAlternativeName() {
		return alternativeName;
	}

	public void addConstruction(Construction construction) {
		constructions.add(construction);
		changed = true;
	}

	public void addAllConstructions(List<Construction> constructions) {
		this.constructions.addAll(constructions);
		changed = true;
	}

	/**
	 * @return the elements
	 */
	public List<Construction> getConstructions() {
		return constructions;
	}

	public boolean isEmpty() {
		return (constructions.size() == 0);
	}

	/**
	 * @return the node
	 */
	public boolean isNode() {
		return node;
	}

	/**
	 * @param node
	 *            the node to set
	 */
	public void setNode(boolean node) {
		this.node = node;
		changed = true;
	}

	/**
	 * @return the stackingAllowed
	 */
	public boolean isStackingAllowed() {
		return stackingAllowed;
	}

	/**
	 * @param stackingAllowed
	 *            the stackingAllowed to set
	 */
	public void setStackingAllowed(boolean stackingAllowed) {
		this.stackingAllowed = stackingAllowed;
		changed = true;
	}

	@Override
	public String toString() {
		return toString(-1);
	}

	public String toString(int itemPosition) {
		StringBuffer result = new StringBuffer(name);
		result.append("(");
		result.append(id);
		result.append(") ");
		result.append(name);
		if (alternativeName.equals(name)) {
			result.append(" {");
			result.append(alternativeName);
			result.append("}");
		}
		result.append(" : ");
		int position = 0;
		for (Construction element : constructions) {
			if (position == itemPosition) {
				result.append(" . ");
			}
			position++;
			result.append(" ");
			result.append(element.toShortString());
		}
		return result.toString();
	}

	public String toShortString(int itemPosition) {
		StringBuffer result = new StringBuffer();
		result.append("(");
		result.append(id);
		result.append(") ");
		result.append(name);
		if (!alternativeName.equals(name)) {
			result.append(" {");
			result.append(alternativeName);
			result.append("}");
		}
		result.append(" -> ");
		int position = 0;
		for (Construction element : constructions) {
			if (position == itemPosition) {
				result.append(" . ");
			}
			position++;
			result.append(" ");
			result.append(element.toShortString());
		}
		if (itemPosition == constructions.size()) {
			result.append(" . ");
		}
		return result.toString();
	}

	@Override
	public int hashCode() {
		if (changed) {
			final int prime = 31;
			int result = 1;
			result = prime
					* result
					+ ((alternativeName == null) ? 0 : alternativeName
							.hashCode());
			result = prime * result
					+ ((constructions == null) ? 0 : constructions.hashCode());
			result = prime * result + id;
			result = prime * result + ((name == null) ? 0 : name.hashCode());
			hashCode = result;
			changed = false;
		}
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
		Production other = (Production) obj;
		if (this.hashCode() != other.hashCode()) {
			return false;
		}
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (alternativeName == null) {
			if (other.alternativeName != null)
				return false;
		} else if (!alternativeName.equals(other.alternativeName))
			return false;
		if (constructions == null) {
			if (other.constructions != null)
				return false;
		} else if (!constructions.equals(other.constructions))
			return false;
		return true;
	}

}
