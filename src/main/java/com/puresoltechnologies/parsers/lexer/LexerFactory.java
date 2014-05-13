package com.puresoltechnologies.parsers.lexer;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.parsers.grammar.Grammar;

/**
 * This class creates a lexer based on options provided in a grammar file. This
 * is used to get the flexibility to used different lexers.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class LexerFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(LexerFactory.class);

	public static Lexer create(Grammar grammar) throws LexerFactoryException {
		try {
			Properties options = grammar.getOptions();
			if (options.containsKey("lexer")) {
				return (Lexer) Class.forName(options.getProperty("lexer"))
						.getConstructor(Grammar.class).newInstance(grammar);
			} else {
				return new RegExpLexer(grammar);
			}
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			throw new LexerFactoryException(e.getMessage());
		}
	}
}
