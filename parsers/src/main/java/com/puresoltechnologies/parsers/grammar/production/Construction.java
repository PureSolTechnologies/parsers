package com.puresoltechnologies.parsers.grammar.production;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

/**
 * This interface represents a single construction. It's weigher a terminal or
 * non-terminal.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
@JsonTypeInfo(use = Id.CLASS, include = As.PROPERTY, property = "class")
public interface Construction extends Serializable, Comparable<Construction> {

    public String getName();

    public boolean isTerminal();

    public boolean isNonTerminal();

    @Override
    public String toString();

    public String toShortString();

}