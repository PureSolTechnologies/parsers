package com.puresoltechnologies.parsers.parser.packrat;

import java.io.Serializable;

/**
 * This class represents a LR element in packrat parser. This LR element is used
 * to track the left recursion growing process.
 * 
 * <b>This class is not thread-safe!</b>
 * 
 * @author Rick-Rainer Ludwig
 */
public class RuleInvocation implements Serializable {

    private static final long serialVersionUID = -5890456350006297830L;

    private final String production;
    private Head head;
    private final RuleInvocation next;

    public RuleInvocation(MemoEntry seed, String production, Head head,
	    RuleInvocation next) {
	super();
	this.production = production;
	this.head = head;
	this.next = next;
    }

    String getProduction() {
	return production;
    }

    void setHead(Head head) {
	this.head = head;
    }

    Head getHead() {
	return head;
    }

    RuleInvocation getNext() {
	return next;
    }

    @Override
    public String toString() {
	String result = production;
	if (head != null)
	    result += " ";
	else
	    result += "*";
	result += "; head: " + head;
	return result;
    }
}
