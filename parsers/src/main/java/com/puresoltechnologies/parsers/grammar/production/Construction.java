package com.puresoltechnologies.parsers.grammar.production;

import java.io.Serializable;

/**
 * This interface represents a single construction. It's weigher a terminal or
 * non-terminal.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public interface Construction extends Serializable, Comparable<Construction> {

	public String getName();

	public boolean isTerminal();

	public boolean isNonTerminal();

	public String toString();

	public String toShortString();

}