package com.puresoltechnologies.parsers.parser;

import java.lang.reflect.Constructor;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.parsers.grammar.Grammar;
import com.puresoltechnologies.parsers.parser.lr.SLR1Parser;

public class ParserFactory {

	private static final Logger logger = LoggerFactory
			.getLogger(ParserFactory.class);

	public static Parser create(Grammar grammar) throws ParserFactoryException {
		try {
			Properties options = grammar.getOptions();
			if (options.containsKey("parser")) {
				Class<?> clazz = Class.forName(options.getProperty("parser"));
				Constructor<?> constructor = clazz
						.getConstructor(Grammar.class);
				Object parser = constructor.newInstance(grammar);
				return (Parser) parser;
			} else {
				return new SLR1Parser(grammar);
			}
		} catch (Throwable e) {
			logger.error(e.getMessage(), e);
			throw new ParserFactoryException(e.getMessage());
		}
	}

}
