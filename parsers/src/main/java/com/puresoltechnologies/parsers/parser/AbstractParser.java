package com.puresoltechnologies.parsers.parser;

import java.lang.reflect.Field;

import com.puresoltechnologies.parsers.grammar.Grammar;
import com.puresoltechnologies.parsers.lexer.TokenStream;

public abstract class AbstractParser implements Parser {

    private static final long serialVersionUID = 6779683403706893973L;

    private final Grammar grammar;
    private TokenStream tokenStream = null;

    public AbstractParser(Grammar grammar) {
	super();
	this.grammar = grammar;
    }

    /**
     * @return the grammar
     */
    protected final Grammar getGrammar() {
	return grammar;
    }

    /**
     * @return the tokenStream
     */
    protected final TokenStream getTokenStream() {
	return tokenStream;
    }

    protected final void setTokenStream(TokenStream tokenStream) {
	this.tokenStream = tokenStream;
    }

    @Override
    public Parser clone() {
	try {
	    AbstractParser cloned = (AbstractParser) super.clone();
	    Field grammar = AbstractParser.class.getDeclaredField("grammar");
	    grammar.setAccessible(true);
	    grammar.set(cloned, this.grammar);
	    grammar.setAccessible(false);
	    cloned.tokenStream = null;
	    return cloned;
	} catch (CloneNotSupportedException e) {
	    throw new RuntimeException(e);
	} catch (IllegalArgumentException e) {
	    throw new RuntimeException(e);
	} catch (IllegalAccessException e) {
	    throw new RuntimeException(e);
	} catch (SecurityException e) {
	    throw new RuntimeException(e);
	} catch (NoSuchFieldException e) {
	    throw new RuntimeException(e);
	}
    }
}
