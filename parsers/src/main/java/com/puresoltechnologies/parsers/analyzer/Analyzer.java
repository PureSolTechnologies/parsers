package com.puresoltechnologies.parsers.analyzer;

import com.puresoltechnologies.parsers.grammar.Grammar;
import com.puresoltechnologies.parsers.grammar.GrammarException;
import com.puresoltechnologies.parsers.lexer.Lexer;
import com.puresoltechnologies.parsers.lexer.LexerException;
import com.puresoltechnologies.parsers.lexer.TokenStream;
import com.puresoltechnologies.parsers.parser.Parser;
import com.puresoltechnologies.parsers.parser.ParserException;
import com.puresoltechnologies.parsers.parser.ParserTree;
import com.puresoltechnologies.parsers.preprocessor.Preprocessor;
import com.puresoltechnologies.parsers.preprocessor.PreprocessorException;
import com.puresoltechnologies.parsers.source.SourceCode;

/**
 * This analyzer class bundles the functionality of an optional preprocessor, a
 * lexer and a parser to a complete analysis flow for easy usage of the parsing
 * functionality,
 * 
 * @author Rick-Rainer Ludwig
 */
public class Analyzer {

	private final Preprocessor preprocessor;
	private final Lexer lexer;
	private final Parser parser;

	public Analyzer(Grammar grammar, ClassLoader classLoader)
			throws GrammarException {
		if (grammar.usesPreProcessor()) {
			preprocessor = grammar.createPreprocessor(classLoader);
		} else {
			preprocessor = null;
		}
		lexer = grammar.createLexer(classLoader);
		parser = grammar.createParser(classLoader);
	}

	public ParserTree analyze(SourceCode sourceCode) throws LexerException,
			ParserException, PreprocessorException {
		SourceCode preProcessedSourceCode;
		if (preprocessor != null) {
			preProcessedSourceCode = preprocessor.process(sourceCode);
		} else {
			preProcessedSourceCode = sourceCode;
		}
		TokenStream tokenStream = lexer.lex(preProcessedSourceCode);
		return parser.parse(tokenStream);
	}

}
