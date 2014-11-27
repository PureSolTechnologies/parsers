package com.puresoltechnologies.parsers.grammar.internal;

import java.util.Properties;

import com.puresoltechnologies.parsers.grammar.Grammar;
import com.puresoltechnologies.parsers.grammar.GrammarException;
import com.puresoltechnologies.parsers.grammar.production.NonTerminal;
import com.puresoltechnologies.parsers.grammar.production.Production;
import com.puresoltechnologies.parsers.grammar.production.ProductionSet;
import com.puresoltechnologies.parsers.grammar.production.Terminal;
import com.puresoltechnologies.parsers.grammar.token.TokenDefinition;
import com.puresoltechnologies.parsers.grammar.token.TokenDefinitionSet;
import com.puresoltechnologies.parsers.grammar.token.Visibility;
import com.puresoltechnologies.parsers.lexer.RegExpLexer;
import com.puresoltechnologies.parsers.parser.lr.SLR1Parser;

/**
 * This class contains the grammar for Nyota Uhura's grammar files. This grammar
 * is programmed due to the inability to read grammar files without a grammar.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class UhuraGrammar {

	public static final String UHURA_GRAMMAR_KEYWORD_OPTIONS = "OPTIONS";
	public static final String UHURA_GRAMMAR_KEYWORD_HELPER = "HELPER";
	public static final String UHURA_GRAMMAR_KEYWORD_TOKENS = "TOKENS";
	public static final String UHURA_GRAMMAR_KEYWORD_PRODUCTIONS = "PRODUCTIONS";
	public static final String UHURA_GRAMMAR_KEYWORD_HIDE = "hide";
	public static final String UHURA_GRAMMAR_KEYWORD_IGNORE = "ignore";
	public static final String UHURA_GRAMMAR_KEYWORD_NODE = "node";
	public static final String UHURA_GRAMMAR_KEYWORD_STACK = "stack";

	public static Grammar getGrammar() {
		try {
			return new Grammar(getOptions(), getTokenDefinitions(),
					getProductions());
		} catch (GrammarException e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	private static Properties getOptions() {
		Properties options = new Properties();
		options.put("lexer", RegExpLexer.class.getName());
		options.put("parser", SLR1Parser.class.getName());
		return options;
	}

	private static TokenDefinitionSet getTokenDefinitions()
			throws GrammarException {
		TokenDefinitionSet tokenDefinitions = new TokenDefinitionSet();
		addCommentsAndAllSymbols(tokenDefinitions);
		addKeywords(tokenDefinitions);
		addLiteralsAndIdentifier(tokenDefinitions);
		return tokenDefinitions;
	}

	private static void addCommentsAndAllSymbols(
			TokenDefinitionSet tokenDefinitions) throws GrammarException {
		tokenDefinitions.addDefinition(new TokenDefinition("COMMENT",
				UhuraTokenRegExps.COMMENT, Visibility.HIDDEN));
		tokenDefinitions.addDefinition(new TokenDefinition("WHITE_SPACE",
				UhuraTokenRegExps.WHITE_SPACE, Visibility.HIDDEN));

		tokenDefinitions.addDefinition(new TokenDefinition("EQUALS", "="));
		tokenDefinitions.addDefinition(new TokenDefinition("SEMICOLON", ";"));
		tokenDefinitions.addDefinition(new TokenDefinition("COMMA", ","));
		tokenDefinitions.addDefinition(new TokenDefinition("COLON", ":"));
		tokenDefinitions.addDefinition(new TokenDefinition("DOT", "\\."));
		tokenDefinitions.addDefinition(new TokenDefinition("PLUS", "\\+"));
		tokenDefinitions.addDefinition(new TokenDefinition("STAR", "\\*"));
		tokenDefinitions.addDefinition(new TokenDefinition("QUESTION_MARK",
				"\\?"));
		tokenDefinitions.addDefinition(new TokenDefinition("LPAREN", "\\("));
		tokenDefinitions.addDefinition(new TokenDefinition("RPAREN", "\\)"));
		tokenDefinitions.addDefinition(new TokenDefinition("VERTICAL_BAR",
				"\\|"));

		tokenDefinitions.addDefinition(new TokenDefinition("LEFT_BRACKET",
				"\\["));
		tokenDefinitions.addDefinition(new TokenDefinition("RIGHT_BRACKET",
				"\\]"));

		tokenDefinitions.addDefinition(new TokenDefinition(
				"LEFT_CURLY_BRACKET", "\\{"));
		tokenDefinitions.addDefinition(new TokenDefinition(
				"RIGHT_CURLY_BRACKET", "\\}"));
	}

	private static void addKeywords(TokenDefinitionSet tokenDefinitions)
			throws GrammarException {
		tokenDefinitions.addDefinition(new TokenDefinition("OPTIONS",
				UHURA_GRAMMAR_KEYWORD_OPTIONS));
		tokenDefinitions.addDefinition(new TokenDefinition("HELPER",
				UHURA_GRAMMAR_KEYWORD_HELPER));
		tokenDefinitions.addDefinition(new TokenDefinition("TOKENS",
				UHURA_GRAMMAR_KEYWORD_TOKENS));
		tokenDefinitions.addDefinition(new TokenDefinition("PRODUCTIONS",
				UHURA_GRAMMAR_KEYWORD_PRODUCTIONS));
		tokenDefinitions.addDefinition(new TokenDefinition("HIDE",
				UHURA_GRAMMAR_KEYWORD_HIDE));
		tokenDefinitions.addDefinition(new TokenDefinition("IGNORE",
				UHURA_GRAMMAR_KEYWORD_IGNORE));
		tokenDefinitions.addDefinition(new TokenDefinition("NODE",
				UHURA_GRAMMAR_KEYWORD_NODE));
		tokenDefinitions.addDefinition(new TokenDefinition("STACK",
				UHURA_GRAMMAR_KEYWORD_STACK));
	}

	private static void addLiteralsAndIdentifier(
			TokenDefinitionSet tokenDefinitions) throws GrammarException {
		tokenDefinitions.addDefinition(new TokenDefinition("STRING_LITERAL",
				UhuraTokenRegExps.STRING_LITERAL));
		tokenDefinitions.addDefinition(new TokenDefinition("INTEGER_LITERAL",
				UhuraTokenRegExps.INTEGER_LITERAL));
		tokenDefinitions.addDefinition(new TokenDefinition(
				"FLOATING_POINT_LITERAL",
				UhuraTokenRegExps.FLOATING_POINT_LITERAL));
		tokenDefinitions.addDefinition(new TokenDefinition("BOOLEAN_LITERAL",
				UhuraTokenRegExps.BOOLEAN_LITERAL));
		tokenDefinitions.addDefinition(new TokenDefinition("IDENTIFIER",
				UhuraTokenRegExps.IDENTIFIER));
	}

	private static ProductionSet getProductions() throws GrammarException {
		ProductionSet productions = new ProductionSet();
		addStart(productions);
		addGrammarFile(productions);
		addGrammarOptionsSection(productions);
		addGrammarOption(productions);
		addHelpersSection(productions);
		addHelperDefinition(productions);
		addTokensSection(productions);
		addTokenDefinition(productions);
		addTokenConstruction(productions);
		addProductionsSection(productions);
		addProductionsDefinition(productions);
		addProductionConstruction(productions);
		addQuantifiers(productions);
		addLiterals(productions);
		return productions;
	}

	private static void addStart(ProductionSet productions)
			throws GrammarException {
		Production production = new Production("_START_");
		production.addConstruction(new NonTerminal("GrammarFile"));
		productions.add(production);
	}

	private static void addGrammarFile(ProductionSet productions)
			throws GrammarException {
		Production production = new Production("GrammarFile");
		production.addConstruction(new NonTerminal("GrammarOptions"));
		production.addConstruction(new NonTerminal("Helper"));
		production.addConstruction(new NonTerminal("Tokens"));
		production.addConstruction(new NonTerminal("Productions"));
		productions.add(production);
	}

	private static void addGrammarOptionsSection(ProductionSet productions)
			throws GrammarException {
		Production production = new Production("GrammarOptions");
		production.addConstruction(new Terminal("OPTIONS", null));
		production.addConstruction(new NonTerminal("GrammarOptionList"));
		productions.add(production);
	}

	private static void addGrammarOption(ProductionSet productions)
			throws GrammarException {
		Production production = new Production("GrammarOptionList");
		production.addConstruction(new NonTerminal("GrammarOptionList"));
		production.addConstruction(new NonTerminal("GrammarOption"));
		production.setNode(true);
		production.setStackingAllowed(false);
		productions.add(production);

		production = new Production("GrammarOptionList");
		production.setNode(true);
		production.setStackingAllowed(false);
		productions.add(production);

		production = new Production("GrammarOption");
		production.addConstruction(new NonTerminal("PropertyIdentifier"));
		production.addConstruction(new Terminal("EQUALS", null));
		production.addConstruction(new NonTerminal("Literal"));
		production.addConstruction(new Terminal("SEMICOLON", null));
		productions.add(production);

		production = new Production("PropertyIdentifier");
		production.addConstruction(new Terminal("IDENTIFIER", null));
		productions.add(production);

		production = new Production("PropertyIdentifier");
		production.addConstruction(new NonTerminal("PropertyIdentifier"));
		production.addConstruction(new Terminal("DOT", null));
		production.addConstruction(new Terminal("IDENTIFIER", null));
		productions.add(production);
	}

	private static void addHelpersSection(ProductionSet productions)
			throws GrammarException {
		Production production = new Production("Helper");
		production.addConstruction(new Terminal("HELPER", null));
		production.addConstruction(new NonTerminal("HelperDefinitions"));
		productions.add(production);
	}

	private static void addHelperDefinition(ProductionSet productions)
			throws GrammarException {
		Production production = new Production("HelperDefinitions");
		production.addConstruction(new NonTerminal("HelperDefinitions"));
		production.addConstruction(new NonTerminal("HelperDefinition"));
		production.setNode(true);
		production.setStackingAllowed(false);
		productions.add(production);

		production = new Production("HelperDefinitions");
		production.setNode(true);
		production.setStackingAllowed(false);
		productions.add(production);

		production = new Production("HelperDefinition");
		production.addConstruction(new Terminal("IDENTIFIER", null));
		production.addConstruction(new Terminal("COLON", null));
		production.addConstruction(new NonTerminal("TokenConstructions"));
		production.addConstruction(new Terminal("SEMICOLON", null));
		productions.add(production);
	}

	private static void addTokensSection(ProductionSet productions)
			throws GrammarException {
		Production production = new Production("Tokens");
		production.addConstruction(new Terminal("TOKENS", null));
		production.addConstruction(new NonTerminal("TokenDefinitions"));
		productions.add(production);
	}

	private static void addTokenDefinition(ProductionSet productions)
			throws GrammarException {
		Production production = new Production("TokenDefinitions");
		production.addConstruction(new NonTerminal("TokenDefinitions"));
		production.addConstruction(new NonTerminal("TokenDefinition"));
		production.setNode(true);
		production.setStackingAllowed(false);
		productions.add(production);

		production = new Production("TokenDefinitions");
		production.addConstruction(new NonTerminal("TokenDefinition"));
		production.setNode(true);
		production.setStackingAllowed(false);
		productions.add(production);

		production = new Production("TokenDefinition");
		production.addConstruction(new Terminal("IDENTIFIER", null));
		production.addConstruction(new Terminal("COLON", null));
		production.addConstruction(new NonTerminal("TokenConstructions"));
		production.addConstruction(new NonTerminal("Visibility"));
		production.addConstruction(new Terminal("SEMICOLON", null));
		productions.add(production);

	}

	private static void addTokenConstruction(ProductionSet productions)
			throws GrammarException {
		Production production = new Production("TokenConstructions");
		production.addConstruction(new NonTerminal("TokenConstructions"));
		production.addConstruction(new Terminal("VERTICAL_BAR", null));
		production.addConstruction(new NonTerminal("TokenConstruction"));
		production.setNode(true);
		production.setStackingAllowed(false);
		productions.add(production);

		production = new Production("TokenConstructions");
		production.addConstruction(new NonTerminal("TokenConstruction"));
		production.setNode(true);
		production.setStackingAllowed(false);
		productions.add(production);

		production = new Production("TokenConstruction");
		production.addConstruction(new NonTerminal("TokenConstruction"));
		production.addConstruction(new NonTerminal("TokenPart"));
		production.setNode(true);
		production.setStackingAllowed(false);
		productions.add(production);

		production = new Production("TokenConstruction");
		production.addConstruction(new NonTerminal("TokenPart"));
		production.setNode(true);
		production.setStackingAllowed(false);
		productions.add(production);

		production = new Production("TokenPart");
		production.addConstruction(new Terminal("LPAREN", null));
		production.addConstruction(new NonTerminal("TokenConstruction"));
		production.addConstruction(new Terminal("RPAREN", null));
		production.addConstruction(new NonTerminal("Quantifier"));
		productions.add(production);

		production = new Production("TokenPart");
		production.addConstruction(new Terminal("IDENTIFIER", null));
		production.addConstruction(new NonTerminal("Quantifier"));
		productions.add(production);

		production = new Production("TokenPart");
		production.addConstruction(new Terminal("STRING_LITERAL", null));
		production.addConstruction(new NonTerminal("Quantifier"));
		productions.add(production);

		production = new Production("Visibility");
		production.addConstruction(new Terminal("LEFT_BRACKET", null));
		production.addConstruction(new Terminal("HIDE", null));
		production.addConstruction(new Terminal("RIGHT_BRACKET", null));
		productions.add(production);

		production = new Production("Visibility");
		production.addConstruction(new Terminal("LEFT_BRACKET", null));
		production.addConstruction(new Terminal("IGNORE", null));
		production.addConstruction(new Terminal("RIGHT_BRACKET", null));
		productions.add(production);

		production = new Production("Visibility");
		productions.add(production);
	}

	private static void addProductionsSection(ProductionSet productions)
			throws GrammarException {
		Production production = new Production("Productions");
		production.addConstruction(new Terminal("PRODUCTIONS", null));
		production.addConstruction(new NonTerminal("ProductionDefinitions"));
		productions.add(production);
	}

	private static void addProductionsDefinition(ProductionSet productions)
			throws GrammarException {
		Production production = new Production("ProductionDefinitions");
		production.addConstruction(new NonTerminal("ProductionDefinitions"));
		production.addConstruction(new NonTerminal("ProductionDefinition"));
		production.setNode(true);
		production.setStackingAllowed(false);
		productions.add(production);

		production = new Production("ProductionDefinitions");
		production.setNode(true);
		production.setStackingAllowed(false);
		productions.add(production);

		production = new Production("ProductionDefinition");
		production.addConstruction(new Terminal("IDENTIFIER", null));
		production.addConstruction(new Terminal("COLON", null));
		production.addConstruction(new NonTerminal("ProductionConstructions"));
		production.addConstruction(new Terminal("SEMICOLON", null));
		productions.add(production);

	}

	private static void addProductionConstruction(ProductionSet productions)
			throws GrammarException {
		Production production = new Production("ProductionConstructions");
		production.addConstruction(new NonTerminal("ProductionConstructions"));
		production.addConstruction(new Terminal("VERTICAL_BAR", null));
		production.addConstruction(new NonTerminal("ProductionConstruction"));
		production.setNode(true);
		production.setStackingAllowed(false);
		productions.add(production);

		production = new Production("ProductionConstructions");
		production.addConstruction(new NonTerminal("ProductionConstruction"));
		production.setNode(true);
		production.setStackingAllowed(false);
		productions.add(production);

		production = new Production("ProductionConstruction");
		production.addConstruction(new NonTerminal("AlternativeIdentifier"));
		production.addConstruction(new NonTerminal("ProductionParts"));
		production.addConstruction(new NonTerminal("ProductionOptions"));
		productions.add(production);

		production = new Production("ProductionParts");
		production.addConstruction(new NonTerminal("ProductionParts"));
		production.addConstruction(new NonTerminal("ProductionPart"));
		production.setNode(true);
		production.setStackingAllowed(false);
		productions.add(production);

		// empty productions are also allowed...
		production = new Production("ProductionConstruction");
		productions.add(production);

		production = new Production("ProductionParts");
		production.addConstruction(new NonTerminal("ProductionPart"));
		production.setNode(true);
		production.setStackingAllowed(false);
		productions.add(production);

		production = new Production("ProductionPart", "ConstructionGroup");
		production.addConstruction(new Terminal("LPAREN", null));
		production.addConstruction(new NonTerminal("ProductionConstructions"));
		production.addConstruction(new Terminal("RPAREN", null));
		production.addConstruction(new NonTerminal("Quantifier"));
		productions.add(production);

		production = new Production("ProductionPart", "ConstructionIdentifier");
		production.addConstruction(new Terminal("IDENTIFIER", null));
		production.addConstruction(new NonTerminal("Quantifier"));
		productions.add(production);

		production = new Production("ProductionPart", "ConstructionLiteral");
		production.addConstruction(new Terminal("STRING_LITERAL", null));
		production.addConstruction(new NonTerminal("Quantifier"));
		productions.add(production);

		production = new Production("AlternativeIdentifier");
		production.addConstruction(new Terminal("LEFT_CURLY_BRACKET", null));
		production.addConstruction(new Terminal("IDENTIFIER", null));
		production.addConstruction(new Terminal("RIGHT_CURLY_BRACKET", null));
		productions.add(production);

		production = new Production("AlternativeIdentifier");
		productions.add(production);

		production = new Production("ProductionOptions");
		production.addConstruction(new Terminal("LEFT_BRACKET", null));
		production.addConstruction(new NonTerminal("ProductionOptionList"));
		production.addConstruction(new Terminal("RIGHT_BRACKET", null));
		productions.add(production);

		production = new Production("ProductionOptions");
		productions.add(production);

		production = new Production("ProductionOptionList");
		production.addConstruction(new NonTerminal("ProductionOptionList"));
		production.addConstruction(new Terminal("COMMA", null));
		production.addConstruction(new NonTerminal("ProductionOption"));
		production.setNode(true);
		production.setStackingAllowed(false);
		productions.add(production);

		production = new Production("ProductionOptionList");
		production.addConstruction(new NonTerminal("ProductionOption"));
		production.setNode(true);
		production.setStackingAllowed(false);
		productions.add(production);

		production = new Production("ProductionOption");
		production.addConstruction(new Terminal("NODE", null));
		production.addConstruction(new Terminal("EQUALS", null));
		production.addConstruction(new Terminal("BOOLEAN_LITERAL", null));
		productions.add(production);

		production = new Production("ProductionOption");
		production.addConstruction(new Terminal("STACK", null));
		production.addConstruction(new Terminal("EQUALS", null));
		production.addConstruction(new Terminal("BOOLEAN_LITERAL", null));
		productions.add(production);
	}

	private static void addQuantifiers(ProductionSet productions)
			throws GrammarException {
		Production production = new Production("Quantifier");
		production.addConstruction(new Terminal("PLUS", null));
		productions.add(production);

		production = new Production("Quantifier");
		production.addConstruction(new Terminal("STAR", null));
		productions.add(production);

		production = new Production("Quantifier");
		production.addConstruction(new Terminal("QUESTION_MARK", null));
		productions.add(production);

		production = new Production("Quantifier");
		productions.add(production);
	}

	private static void addLiterals(ProductionSet productions)
			throws GrammarException {
		Production production = new Production("Literal");
		production.addConstruction(new Terminal("STRING_LITERAL", null));
		productions.add(production);

		production = new Production("Literal");
		production.addConstruction(new Terminal("INTEGER_LITERAL", null));
		productions.add(production);

		production = new Production("Literal");
		production
				.addConstruction(new Terminal("FLOATING_POINT_LITERAL", null));
		productions.add(production);

		production = new Production("Literal");
		production.addConstruction(new Terminal("BOOLEAN_LITERAL", null));
		productions.add(production);
	}
}
