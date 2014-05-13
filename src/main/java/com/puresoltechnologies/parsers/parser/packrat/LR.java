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
public class LR implements Serializable {

	private static final long serialVersionUID = -5890456350006297830L;

	private MemoEntry seed;
	private final String production;
	private Head head;

	public LR(MemoEntry seed, String production, Head head) {
		super();
		this.seed = seed;
		this.production = production;
		this.head = head;
	}

	void setSeed(MemoEntry seed) {
		this.seed = seed;
	}

	MemoEntry getSeed() {
		return seed;
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

	@Override
	public String toString() {
		String result = production;
		if (head != null)
			result += " ";
		else
			result += "*";
		result += "; seed: " + seed;
		result += "; head: " + head;
		return result;
	}
}
