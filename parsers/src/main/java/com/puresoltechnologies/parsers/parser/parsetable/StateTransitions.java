package com.puresoltechnologies.parsers.parser.parsetable;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.puresoltechnologies.parsers.grammar.GrammarException;
import com.puresoltechnologies.parsers.grammar.production.Construction;

/**
 * This class is used to collect all state transitions for a grammar.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class StateTransitions implements Serializable {

	private static final long serialVersionUID = 3448400511750197925L;

	private final Map<Integer, Map<Construction, Integer>> transitions = new HashMap<Integer, Map<Construction, Integer>>();

	private boolean changed = true;
	private int hashCode = 0;

	public StateTransitions() {
		super();
	}

	public int size() {
		return transitions.size();
	}

	protected final void addTransition(int initialState,
			Construction construction, int targetState) throws GrammarException {
		changed = true;
		Map<Construction, Integer> transitionMap = transitions
				.get(initialState);
		if (transitionMap == null) {
			transitionMap = new HashMap<Construction, Integer>();
			transitions.put(initialState, transitionMap);
		}
		Integer savedTargetState = transitions.get(initialState).get(
				construction);
		if (savedTargetState == null) {
			transitions.get(initialState).put(construction, targetState);
		} else {
			if (!savedTargetState.equals(targetState)) {
				throw new GrammarException("Ambiguous transitions found!");
			}
		}
	}

	public final Map<Construction, Integer> getTransitions(int initialState) {
		Map<Construction, Integer> result = transitions.get(initialState);
		if (result == null) {
			result = new HashMap<Construction, Integer>();
		}
		return result;
	}

	public final int getTransition(int initialState, Construction construction) {
		return transitions.get(initialState).get(construction);
	}

	@Override
	public int hashCode() {
		if (changed) {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((transitions == null) ? 0 : transitions.hashCode());
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
		StateTransitions other = (StateTransitions) obj;
		if (this.hashCode() != other.hashCode()) {
			return false;
		}
		if (transitions == null) {
			if (other.transitions != null)
				return false;
		} else if (!transitions.equals(other.transitions))
			return false;
		return true;
	}
}
