package com.puresoltechnologies.parsers.grammar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.puresoltechnologies.commons.misc.StopWatch;
import com.puresoltechnologies.parsers.lexer.Lexer;
import com.puresoltechnologies.parsers.lexer.LexerException;
import com.puresoltechnologies.parsers.lexer.LexerFactory;
import com.puresoltechnologies.parsers.lexer.LexerFactoryException;
import com.puresoltechnologies.parsers.lexer.RegExpLexer;
import com.puresoltechnologies.parsers.lexer.TokenStream;
import com.puresoltechnologies.parsers.parser.ParseTreeNode;
import com.puresoltechnologies.parsers.parser.Parser;
import com.puresoltechnologies.parsers.parser.ParserException;
import com.puresoltechnologies.parsers.parser.ParserFactory;
import com.puresoltechnologies.parsers.parser.ParserFactoryException;
import com.puresoltechnologies.parsers.parser.ParserManager;
import com.puresoltechnologies.parsers.source.SourceCode;
import com.puresoltechnologies.trees.TreePrinter;

/**
 * This class is a simple tester for checking grammars and parts of grammars.
 * Parts are single production which can be check by redirecting the start
 * element with id 0 to the production which is to be checked. With this trick
 * parts of grammars and single specifications can be tested.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class GrammarPartTester {

    private static final Logger logger = LoggerFactory.getLogger(GrammarPartTester.class);

    public static boolean test(Grammar grammar, String production, String... lines) {
	try {
	    if (logger.isDebugEnabled()) {
		logger.debug("Testing production '" + production + "' with text '" + Arrays.toString(lines) + "' ...");
	    }
	    grammar = grammar.createWithNewStartProduction(production);
	    Lexer lexer = LexerFactory.create(grammar);
	    TokenStream tokenStream = lexer.lex(SourceCode.fromStringArray(lines));

	    Parser parser = ParserFactory.create(grammar);
	    ParseTreeNode ast = parser.parse(tokenStream);
	    if (logger.isTraceEnabled()) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		new TreePrinter(new PrintStream(out, true, Charset.defaultCharset().name())).println(ast);
		logger.trace(new String(out.toByteArray(), Charset.defaultCharset()));
	    }
	    logger.debug("passed.");
	    return true;
	} catch (GrammarException | LexerException | ParserFactoryException | ParserException | LexerFactoryException
		| UnsupportedEncodingException e) {
	    e.printStackTrace();
	    return false;
	}
    }

    public static boolean test(File parserDirectory, String parserName, Grammar grammar, String production,
	    String... lines) {
	try {
	    if (logger.isDebugEnabled()) {
		logger.debug("Testing text '" + production + "' with text '" + Arrays.toString(lines) + "' ...");
	    }
	    grammar = grammar.createWithNewStartProduction(production);
	    StopWatch watch = new StopWatch();
	    Lexer lexer = new RegExpLexer(grammar);
	    watch.start();
	    TokenStream tokenStream = lexer.lex(SourceCode.fromStringArray(lines));
	    watch.stop();
	    logger.debug("Lexer time: " + watch);

	    Parser parser = ParserManager.getManagerParser(parserDirectory, parserName, grammar);
	    watch.start();
	    ParseTreeNode ast = parser.parse(tokenStream);
	    watch.stop();
	    logger.debug("Parser time: " + watch);
	    if (logger.isTraceEnabled()) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		new TreePrinter(new PrintStream(out, true, Charset.defaultCharset().name())).println(ast);
		logger.trace(new String(out.toByteArray(), Charset.defaultCharset()));
	    }
	    logger.debug("passed.");
	    return true;
	} catch (GrammarException | ParserException | LexerException | UnsupportedEncodingException e) {
	    e.printStackTrace();
	    return false;
	}
    }
}
