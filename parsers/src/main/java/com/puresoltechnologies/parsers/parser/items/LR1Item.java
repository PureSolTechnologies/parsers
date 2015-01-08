package com.puresoltechnologies.parsers.parser.items;

import java.util.Objects;

import com.puresoltechnologies.parsers.grammar.production.Production;
import com.puresoltechnologies.parsers.grammar.production.Terminal;

/**
 * This class represents a single LR1 item for parser table generation.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class LR1Item extends LR0Item {

    private static final long serialVersionUID = -1434126363541910894L;

    private final Terminal lookahead;

    private final int hashCode;

    public LR1Item(Production production, int position, Terminal lookahead) {
	super(production, position);
	this.lookahead = lookahead;
	hashCode = Objects.hash(super.hashCode(), lookahead);
    }

    /**
     * @return the lookahead
     */
    public Terminal getLookahead() {
	return lookahead;
    }

    @Override
    public String toString() {
	StringBuffer buffer = new StringBuffer(super.toString());
	buffer.append(", {");
	buffer.append(lookahead.toShortString());
	buffer.append("}");
	return buffer.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
	return hashCode;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!super.equals(obj))
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	LR1Item other = (LR1Item) obj;
	if (this.hashCode() != other.hashCode()) {
	    return false;
	}
	if (lookahead == null) {
	    if (other.lookahead != null)
		return false;
	} else if (!lookahead.equals(other.lookahead))
	    return false;
	return true;
    }

}
