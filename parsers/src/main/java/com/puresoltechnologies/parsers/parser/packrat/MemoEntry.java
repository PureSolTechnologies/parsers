package com.puresoltechnologies.parsers.parser.packrat;

import java.io.Serializable;

import com.puresoltechnologies.parsers.parser.ParserException;
import com.puresoltechnologies.parsers.parser.ParserTree;

/**
 * This is a memo entry for packrat parsing. The memoization process keeps all
 * importan information for the run.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
class MemoEntry implements Serializable, Comparable<MemoEntry> {

    private static final long serialVersionUID = 2910217488523982637L;

    /**
     * Creates a simple successs memo entry.
     * 
     * @param deltaPosition
     * @param deltaId
     * @param deltaLine
     * @param tree
     * @return
     */
    static MemoEntry success(int deltaPosition, int deltaLine, ParserTree tree) {
	return new MemoEntry(deltaPosition, deltaLine, tree);
    }

    /**
     * Creates a simple failed memo entry.
     * 
     * @return
     */
    static MemoEntry failed() {
	return new MemoEntry(0, 0, Status.FAILED);
    }

    /**
     * Creates a memo entry for the given lr object.
     * 
     * @param lr
     * @return
     */
    static MemoEntry create(LR lr) {
	return new MemoEntry(0, 0, lr);
    }

    private int deltaPosition;
    private int deltaLine;
    private Object answer;

    private MemoEntry(int deltaPosition, int deltaLine, Object answer) {
	super();
	this.deltaPosition = deltaPosition;
	this.deltaLine = deltaLine;
	this.answer = answer;
    }

    void setDeltaPosition(int deltaPosition) {
	this.deltaPosition = deltaPosition;
    }

    int getDeltaPosition() {
	return deltaPosition;
    }

    int getDeltaLine() {
	return deltaLine;
    }

    void setDeltaLine(int deltaLine) {
	this.deltaLine = deltaLine;
    }

    void setAnswer(Object answer) {
	this.answer = answer;
    }

    Object getAnswer() {
	return answer;
    }

    void add(MemoEntry progress) throws ParserException {
	if ((deltaPosition < 0) || (deltaLine < 0)) {
	    throw new ParserException("Negative progress is not supported!");
	}
	deltaPosition += progress.deltaPosition;
	deltaLine += progress.deltaLine;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + deltaPosition;
	result = prime * result + ((answer == null) ? 0 : answer.hashCode());
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
	MemoEntry other = (MemoEntry) obj;
	if (deltaPosition != other.deltaPosition)
	    return false;
	if (answer == null) {
	    if (other.answer != null)
		return false;
	} else if (!answer.equals(other.answer))
	    return false;
	return true;
    }

    @Override
    public int compareTo(MemoEntry other) {
	if (this.deltaPosition < other.deltaPosition)
	    return -1;
	else if (this.deltaPosition > other.deltaPosition)
	    return 1;
	return 0;
    }

    public void set(MemoEntry ans) {
	this.deltaPosition = ans.deltaPosition;
	this.deltaLine = ans.deltaLine;
	this.answer = ans.answer;
    }

    @Override
    public String toString() {
	String result = "dPos: " + deltaPosition;
	result += "; dLine: " + deltaLine;
	result += "; Answer: " + answer;
	return result;
    }
}
