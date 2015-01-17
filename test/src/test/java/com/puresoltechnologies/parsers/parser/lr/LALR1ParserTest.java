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

public class LALR1ParserTest {

    @Test
    public void testSimple() throws Throwable {
	Grammar grammar = TestGrammars.getLALR1TestGrammarFromDragonBook();
	Parser parser = new LALR1Parser(grammar);
	Lexer lexer = new RegExpLexer(grammar);
	ParseTreeNode syntaxTree = parser.parse(lexer.lex(SourceCode
		.read(new StringReader("id=*id"),
			new UnspecifiedSourceCodeLocation())));
	new TreePrinter(System.out).println(syntaxTree);
    }

}
