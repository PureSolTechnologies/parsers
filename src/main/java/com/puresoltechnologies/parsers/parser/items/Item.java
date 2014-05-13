package com.puresoltechnologies.parsers.parser.items;

import java.io.Serializable;

import com.puresoltechnologies.parsers.grammar.production.Construction;
import com.puresoltechnologies.parsers.grammar.production.Production;

/**
 * This interface represents a single item which is a production with a position
 * marker for parser table generation.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public interface Item extends Serializable {

	public Production getProduction();

	public int getPosition();

	public Construction getNext();

	public boolean hasNext();

	public Construction get2ndNext();

	public boolean has2ndNext();

}
