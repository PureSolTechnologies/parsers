package com.puresoltechnologies.parsers.parser.lr;

import java.io.StringReader;

import org.junit.Test;

import com.puresoltechnologies.parsers.grammar.Grammar;
import com.puresoltechnologies.parsers.grammar.TestGrammars;
import com.puresoltechnologies.parsers.lexer.Lexer;
import com.puresoltechnologies.parsers.lexer.RegExpLexer;
import com.puresoltechnologies.parsers.parser.Parser;
import com.puresoltechnologies.parsers.parser.ParseTreeNode;
import com.puresoltechnologies.parsers.source.SourceCode;
import com.puresoltechnologies.parsers.source.UnspecifiedSourceCodeLocation;
import com.puresoltechnologies.trees.TreePrinter;

public class SLR1ParserTest {

    @Test
    public void testSimple() throws Throwable {
	Grammar grammar = TestGrammars.getSLR1TestGrammarFromDragonBook();
	Parser parser = new SLR1Parser(grammar);
	Lexer lexer = new RegExpLexer(grammar);
	ParseTreeNode syntaxTree = parser
		.parse(lexer.lex(SourceCode.read(new StringReader("1*2+3"),
			new UnspecifiedSourceCodeLocation())));
	new TreePrinter(System.out).println(syntaxTree);
    }

    @Test
    public void testComplex() throws Throwable {
	Grammar grammar = TestGrammars.getSLR1TestGrammarFromDragonBook();
	Parser parser = new SLR1Parser(grammar);
	Lexer lexer = new RegExpLexer(grammar);
	ParseTreeNode syntaxTree = parser.parse(lexer.lex(SourceCode.read(
		new StringReader("((1*(2+3)+4*5)+6)*7"),
		new UnspecifiedSourceCodeLocation())));
	new TreePrinter(System.out).println(syntaxTree);
    }

}
