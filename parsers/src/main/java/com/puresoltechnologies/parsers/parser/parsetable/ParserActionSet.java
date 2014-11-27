package com.puresoltechnologies.parsers.parser.parsetable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.puresoltechnologies.parsers.grammar.GrammarException;

/**
 * This class contains a set of possible actions for a parser table. If the
 * grammar is non-ambiguous the number is always one.
 * 
 * This class was especially designed for non-distinct grammars and for the
 * parser's ability for backtracking while trial-n-error processing.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class ParserActionSet implements Serializable {

	private static final long serialVersionUID = -724846770893802355L;

	public static ParserActionSet getErrorSet() {
		ParserActionSet set = new ParserActionSet();
		set.addAction(new ParserAction(ActionType.ERROR, -1));
		return set;
	}

	private final List<ParserAction> actions = new ArrayList<ParserAction>();

	public ParserActionSet() {
		actions.add(new ParserAction(ActionType.ERROR, -1));
	}

	public void addAction(ParserAction action) {
		if (action.getAction() == ActionType.ERROR) {
			return;
		}
		if (actions.get(0).getAction() == ActionType.ERROR) {
			actions.clear();
		}
		if (!actions.contains(action)) {
			actions.add(action);
		}
		Collections.sort(actions);
	}

	public void addActions(ParserActionSet actionSet) {
		if (actionSet != null) {
			for (int id = 0; id < actionSet.getActionNumber(); id++) {
				addAction(actionSet.getAction(id));
			}
		}
	}

	public ParserAction getAction() throws GrammarException {
		if (actions.size() == 0) {
			return new ParserAction(ActionType.ERROR, -1);
		}
		if (actions.size() == 1) {
			return actions.get(0);
		}
		throw new GrammarException("Can not choose for one of '" + toString()
				+ "'!");
	}

	public ParserAction getAction(int id) {
		return actions.get(id);
	}

	public int getActionNumber() {
		return actions.size();
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		Iterator<ParserAction> iter = actions.iterator();
		boolean first = true;
		while (iter.hasNext()) {
			ParserAction action = iter.next();
			if (first) {
				first = false;
			} else {
				buffer.append(",");
			}
			buffer.append(action.toString());
		}
		return buffer.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((actions == null) ? 0 : actions.hashCode());
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
		ParserActionSet other = (ParserActionSet) obj;
		if (actions == null) {
			if (other.actions != null)
				return false;
		} else if (!actions.equals(other.actions))
			return false;
		return true;
	}
}
