package com.puresoltechnologies.parsers.parser.lr;

import static org.junit.Assert.assertNotNull;

import java.io.StringReader;

import org.junit.Test;

import com.puresoltechnologies.parsers.grammar.Grammar;
import com.puresoltechnologies.parsers.grammar.TestGrammars;
import com.puresoltechnologies.parsers.lexer.Lexer;
import com.puresoltechnologies.parsers.lexer.RegExpLexer;
import com.puresoltechnologies.parsers.parser.Parser;
import com.puresoltechnologies.parsers.parser.ParserTree;
import com.puresoltechnologies.parsers.source.SourceCode;
import com.puresoltechnologies.parsers.source.UnspecifiedSourceCodeLocation;

public class LR1ParserTest {

	@Test
	public void testSimple() throws Throwable {
		// Logger.getRootLogger().setLevel(Level.TRACE);
		Grammar grammar = TestGrammars.getSLR1TestGrammarFromDragonBook();
		assertNotNull(grammar);
		Parser parser = new LR1Parser(grammar);
		Lexer lexer = new RegExpLexer(grammar);
		ParserTree syntaxTree = parser
				.parse(lexer.lex(SourceCode.read(new StringReader("1*2+3"),
						new UnspecifiedSourceCodeLocation())));
		assertNotNull(syntaxTree);
	}

	@Test
	public void testComplex() throws Throwable {
		// Logger.getRootLogger().setLevel(Level.TRACE);
		Grammar grammar = TestGrammars.getSLR1TestGrammarFromDragonBook();
		Lexer lexer = new RegExpLexer(grammar);
		Parser parser = new LR1Parser(grammar);
		ParserTree syntaxTree = parser.parse(lexer.lex(SourceCode.read(
				new StringReader("((1*(2+3)+4*5)+6)*7"),
				new UnspecifiedSourceCodeLocation())));
		assertNotNull(syntaxTree);
		// new TreePrinter(System.out).println(syntaxTree);
	}
}
