package com.puresoltechnologies.parsers.parser.lr;

import java.io.Serializable;
import java.util.Stack;

public class BacktrackLocation implements Serializable {

	private static final long serialVersionUID = -2641716613348317852L;

	private final Stack<Integer> stateStack;
	private final int actionStackSize;
	private final int streamPosition;
	private final int stepCounter;
	private final int lastAlternative;

	public BacktrackLocation(Stack<Integer> stateStack, int actionStackSize,
			int streamPosition, int stepCounter, int lastAlternative) {
		super();
		this.stateStack = stateStack;
		this.actionStackSize = actionStackSize;
		this.streamPosition = streamPosition;
		this.stepCounter = stepCounter;
		this.lastAlternative = lastAlternative;
	}

	/**
	 * @return the stateStack
	 */
	public Stack<Integer> getStateStack() {
		return stateStack;
	}

	/**
	 * @return the treeStack
	 */
	public int getActionStackSize() {
		return actionStackSize;
	}

	/**
	 * @return the streamPosition
	 */
	public int getStreamPosition() {
		return streamPosition;
	}

	/**
	 * @return the stepCounter
	 */
	public int getStepCounter() {
		return stepCounter;
	}

	/**
	 * @return the lastAlternative
	 */
	public int getLastAlternative() {
		return lastAlternative;
	}
}
