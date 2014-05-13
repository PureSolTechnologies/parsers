package com.puresoltechnologies.parsers.parser.parsetable;

import java.io.Serializable;

import com.puresoltechnologies.commons.misc.ObjectUtilities;

/**
 * This is a single state table entry for a stored action. The action consists
 * of a parser action and an additional integer value containing the next state
 * or the grammar production to be used for reduction.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class ParserAction implements Serializable, Comparable<ParserAction> {

	private static final long serialVersionUID = -3440478384808031825L;

	private final ActionType action;
	private final int parameter;
	private final int hashCode;

	/**
	 * Constructor
	 * 
	 * @param action
	 *            to take when parsing.
	 * @param parameter
	 *            is the target state after the process. States small than 0
	 *            show no target states for Accept and Error.
	 */
	public ParserAction(ActionType action, int parameter) {
		super();
		this.action = action;
		this.parameter = parameter;
		hashCode = ObjectUtilities.calculateConstantHashCode(action, parameter);
	}

	/**
	 * @return the action
	 */
	public ActionType getAction() {
		return action;
	}

	/**
	 * @return the targetState
	 */
	public int getParameter() {
		return parameter;
	}

	@Override
	public String toString() {
		String result = action.toString();
		if (parameter >= 0) {
			result += parameter;
		}
		return result;
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
		ParserAction other = (ParserAction) obj;
		if (action != other.action)
			return false;
		if (parameter != other.parameter)
			return false;
		return true;
	}

	/**
	 * This comparison is used to sort parser action lists for ambiguous
	 * grammars to get the order of fastest progress.
	 * 
	 * The experience tells that the fastest progress is to shift first and try
	 * to reduce later. And the sorting of the productions within the grammar
	 * file should be from rough to fine definition and the reduction is tried
	 * from the latest production to the first. Means, from fine to rough.
	 */
	@Override
	public int compareTo(ParserAction other) {
		int result = this.action.compareTo(other.action);
		if (result != 0) {
			return result;
		}
		return other.parameter - this.parameter;
	}
}
