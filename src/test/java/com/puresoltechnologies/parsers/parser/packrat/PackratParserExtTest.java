package com.puresoltechnologies.parsers.parser.packrat;

import static org.junit.Assert.assertNotNull;

import java.io.InputStream;

import org.junit.Test;

import com.puresoltechnologies.commons.trees.TreePrinter;
import com.puresoltechnologies.parsers.grammar.Grammar;
import com.puresoltechnologies.parsers.grammar.GrammarConverter;
import com.puresoltechnologies.parsers.grammar.GrammarFile;
import com.puresoltechnologies.parsers.parser.ParserTree;
import com.puresoltechnologies.parsers.source.FixedCodeLocation;
import com.puresoltechnologies.parsers.source.SourceCodeLocation;

/**
 * This tests are taken from the paper 'Packrat Parsers Can Support Left
 * Recursion' by Warth et.al.. The grammar is from figure 10 and the results can
 * be found in table 1.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class PackratParserExtTest {

	private ParserTree parseText(SourceCodeLocation source) throws Throwable {
		InputStream inputStream = getClass()
				.getResourceAsStream(
						"/com/puresoltechnologies/parsers/grammar/TestGrammarForJavaPrimaryExpressions.g");
		assertNotNull(inputStream);
		try {
			GrammarFile grammarFile = new GrammarFile(inputStream);
			try {
				Grammar grammar = new GrammarConverter(grammarFile.getAST())
						.getGrammar();
				assertNotNull(grammar);
				PackratParser parser = new PackratParser(grammar);
				ParserTree parseTree = parser.parse(source.getSourceCode());
				assertNotNull(parseTree);
				TreePrinter printer = new TreePrinter(System.out);
				printer.println(parseTree);
				return parseTree;
			} finally {
				grammarFile.close();
			}
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void test1() throws Throwable {
		parseText(new FixedCodeLocation("this"));
	}

	@Test
	public void test2() throws Throwable {
		parseText(new FixedCodeLocation("this.x"));
	}

	@Test
	public void test3() throws Throwable {
		parseText(new FixedCodeLocation("this.x.y"));
	}

	@Test
	public void test4() throws Throwable {
		parseText(new FixedCodeLocation("this.x.m()"));
	}

	@Test
	public void test5() throws Throwable {
		parseText(new FixedCodeLocation("x[i][j].y"));
	}
}
