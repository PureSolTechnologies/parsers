package com.puresoltechnologies.parsers.lexer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.StringReader;
import java.util.Properties;

import org.junit.Test;

import com.puresoltechnologies.parsers.grammar.Grammar;
import com.puresoltechnologies.parsers.grammar.production.ProductionSet;
import com.puresoltechnologies.parsers.grammar.token.TokenDefinition;
import com.puresoltechnologies.parsers.grammar.token.TokenDefinitionSet;
import com.puresoltechnologies.parsers.parser.lr.LR0Parser;
import com.puresoltechnologies.parsers.source.SourceCode;
import com.puresoltechnologies.parsers.source.UnspecifiedSourceCodeLocation;

public class RegExpLexerTest {

	@Test
	public void test() throws Throwable {
		TokenDefinitionSet rules = new TokenDefinitionSet();
		rules.addDefinition(new TokenDefinition("NUMBER", "[0-9]+"));
		rules.addDefinition(new TokenDefinition("WHITESPACE", "[ \\t\\n]+"));
		Properties options = new Properties();
		options.put("lexer", RegExpLexer.class.getName());
		options.put("parser", LR0Parser.class.getName());
		options.put("grammar.checks", "false");
		Grammar grammar = new Grammar(options, rules, new ProductionSet());
		Lexer lexer = new RegExpLexer(grammar);
		TokenStream tokenStream = lexer.lex(SourceCode.read(
				new StringReader(
						"0\n1\t2\n\t3 \t4 \t5\t 6 7 8 9 10 11 12 13 14 15"),
				new UnspecifiedSourceCodeLocation()));
		assertNotNull(tokenStream);
		assertEquals(31, tokenStream.size());
		Token token0 = tokenStream.get(0);
		TokenMetaData metaData0 = token0.getMetaData();
		assertEquals("NUMBER", token0.getName());
		assertEquals("0", token0.getText());
		assertEquals(1, metaData0.getLine());
		assertEquals(1, metaData0.getLineNum());
		assertEquals(0, metaData0.getColumn());

		Token token1 = tokenStream.get(1);
		TokenMetaData metaData1 = token1.getMetaData();
		assertEquals("WHITESPACE", token1.getName());
		assertEquals("\n", token1.getText());
		assertEquals(1, metaData1.getLine());
		assertEquals(2, metaData1.getLineNum());
		assertEquals(1, metaData1.getColumn());

		Token token2 = tokenStream.get(2);
		TokenMetaData metaData2 = token2.getMetaData();
		assertEquals("NUMBER", token2.getName());
		assertEquals("1", token2.getText());
		assertEquals(2, metaData2.getLine());
		assertEquals(1, metaData2.getLineNum());
		assertEquals(0, metaData2.getColumn());

		Token token3 = tokenStream.get(3);
		TokenMetaData metaData3 = token3.getMetaData();
		assertEquals("WHITESPACE", token3.getName());
		assertEquals("\t", token3.getText());
		assertEquals(2, metaData3.getLine());
		assertEquals(1, metaData3.getLineNum());
		assertEquals(1, metaData3.getColumn());

		Token token4 = tokenStream.get(4);
		TokenMetaData metaData4 = token4.getMetaData();
		assertEquals("NUMBER", token4.getName());
		assertEquals("2", token4.getText());
		assertEquals(2, metaData4.getLine());
		assertEquals(1, metaData4.getLineNum());
		assertEquals(2, metaData4.getColumn());

		Token token5 = tokenStream.get(5);
		TokenMetaData metaData5 = token5.getMetaData();
		assertEquals("WHITESPACE", token5.getName());
		assertEquals("\n\t", token5.getText());
		assertEquals(2, metaData5.getLine());
		assertEquals(2, metaData5.getLineNum());
		assertEquals(3, metaData5.getColumn());

		Token token6 = tokenStream.get(6);
		TokenMetaData metaData6 = token6.getMetaData();
		assertEquals("NUMBER", token6.getName());
		assertEquals("3", token6.getText());
		assertEquals(3, metaData6.getLine());
		assertEquals(1, metaData6.getLineNum());
		assertEquals(1, metaData6.getColumn());
	}

	/**
	 * Checks a lex error.
	 * 
	 * @throws Exception
	 */
	@Test(expected = LexerException.class)
	public void testFindNextToken() throws Exception {
		TokenDefinitionSet rules = new TokenDefinitionSet();
		rules.addDefinition(new TokenDefinition("STRING", "\"[^\"]*\""));
		Properties options = new Properties();
		options.put("lexer", RegExpLexer.class.getName());
		options.put("parser", LR0Parser.class.getName());
		options.put("grammar.checks", "false");
		Grammar grammar = new Grammar(options, rules, new ProductionSet());
		Lexer lexer = new RegExpLexer(grammar);
		lexer.lex(SourceCode.read(new StringReader(
				"\"String without trailing double quote!"),
				new UnspecifiedSourceCodeLocation()));
	}
}
