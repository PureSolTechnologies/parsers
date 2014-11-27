package com.puresoltechnologies.parsers.grammar;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.Properties;

import org.junit.Test;

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
 * This class contains several grammars from DragonBook or the LR1 Pamphlet.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class TestGrammars {

    /**
     * This is a dummy test to avoid a
     * "java.lang.Exception: No runnable methods" exception.
     */
    @Test
    public void test() {
	assertNotNull(new TestGrammars());
    }

    private static Properties createStandardOptions() {
	Properties properties = new Properties();
	properties.put("lexer", RegExpLexer.class.getName());
	properties.put("parser", SLR1Parser.class.getName());
	return properties;
    }

    /**
     * This grammar is from the LR(k)-Analyse fuer Pragmatiker page 10. The
     * document is located in the man directory.
     * 
     * The grammar is specially designed to support LR1 parsing.
     * 
     * @return A {@link Grammar} is returned.
     */
    public static Grammar getGrammarFromLRkPamphlet() {
	try {
	    TokenDefinitionSet tokenDefinitions = new TokenDefinitionSet();

	    tokenDefinitions.addDefinition(new TokenDefinition("a", "a",
		    Visibility.VISIBLE));
	    tokenDefinitions.addDefinition(new TokenDefinition("b", "b",
		    Visibility.VISIBLE));
	    tokenDefinitions.addDefinition(new TokenDefinition("c", "c",
		    Visibility.VISIBLE));

	    ProductionSet productions = new ProductionSet();

	    Production production = new Production("Z");
	    production.addConstruction(new NonTerminal("S"));
	    productions.add(production);

	    production = new Production("S");
	    production.addConstruction(new NonTerminal("S"));
	    production.addConstruction(new Terminal("b", null));
	    productions.add(production);

	    production = new Production("S");
	    production.addConstruction(new Terminal("b", null));
	    production.addConstruction(new NonTerminal("A"));
	    production.addConstruction(new Terminal("a", null));
	    productions.add(production);

	    production = new Production("A");
	    production.addConstruction(new Terminal("a", null));
	    production.addConstruction(new NonTerminal("S"));
	    production.addConstruction(new Terminal("c", null));
	    productions.add(production);

	    production = new Production("A");
	    production.addConstruction(new Terminal("a", null));
	    productions.add(production);

	    production = new Production("A");
	    production.addConstruction(new Terminal("a", null));
	    production.addConstruction(new NonTerminal("S"));
	    production.addConstruction(new Terminal("b", null));
	    productions.add(production);

	    return new Grammar(createStandardOptions(), tokenDefinitions,
		    productions);
	} catch (GrammarException e) {
	    e.printStackTrace();
	    fail("No exception was expected!");
	    return null;
	}
    }

    /**
     * This grammar is from the German edition of the Dragon book from page 295.
     * 
     * <pre>
     * 	E' -&gt; E
     * 	E -&gt; E + T | T
     * 	T -&gt; T * F | F
     * 	F -&gt; (E) | id
     * </pre>
     * 
     * @return A {@link Grammar} object is returned.
     */
    public static Grammar getSLR1TestGrammarFromDragonBook() {
	try {
	    TokenDefinitionSet tokenDefinitions = new TokenDefinitionSet();

	    tokenDefinitions.addDefinition(new TokenDefinition("id", "[0-9.]+",
		    Visibility.VISIBLE));
	    tokenDefinitions.addDefinition(new TokenDefinition("PLUS", "\\+",
		    Visibility.VISIBLE));
	    tokenDefinitions.addDefinition(new TokenDefinition("STAR", "\\*",
		    Visibility.VISIBLE));
	    tokenDefinitions.addDefinition(new TokenDefinition("LPAREN", "\\(",
		    Visibility.VISIBLE));
	    tokenDefinitions.addDefinition(new TokenDefinition("RPAREN", "\\)",
		    Visibility.VISIBLE));
	    tokenDefinitions.addDefinition(new TokenDefinition("WHITESPACE",
		    "[\\s]+", Visibility.VISIBLE));

	    ProductionSet productions = new ProductionSet();

	    Production production = new Production("Z");
	    production.addConstruction(new NonTerminal("E"));
	    productions.add(production);

	    production = new Production("E");
	    production.addConstruction(new NonTerminal("E"));
	    production.addConstruction(new Terminal("PLUS", "+"));
	    production.addConstruction(new NonTerminal("T"));
	    productions.add(production);

	    production = new Production("E");
	    production.addConstruction(new NonTerminal("T"));
	    productions.add(production);

	    production = new Production("T");
	    production.addConstruction(new NonTerminal("T"));
	    production.addConstruction(new Terminal("STAR", "*"));
	    production.addConstruction(new NonTerminal("F"));
	    productions.add(production);

	    production = new Production("T");
	    production.addConstruction(new NonTerminal("F"));
	    productions.add(production);

	    production = new Production("F");
	    production.addConstruction(new Terminal("LPAREN", "("));
	    production.addConstruction(new NonTerminal("E"));
	    production.addConstruction(new Terminal("RPAREN", ")"));
	    productions.add(production);

	    production = new Production("F");
	    production.addConstruction(new Terminal("id", null));
	    productions.add(production);

	    return new Grammar(createStandardOptions(), tokenDefinitions,
		    productions);
	} catch (GrammarException e) {
	    e.printStackTrace();
	    fail("No exception was expected!");
	    return null;
	}
    }

    public static Grammar getLR1TestGrammarFromDragonBook() {
	try {
	    TokenDefinitionSet tokenDefinitions = new TokenDefinitionSet();

	    tokenDefinitions.addDefinition(new TokenDefinition("c", "c",
		    Visibility.VISIBLE));
	    tokenDefinitions.addDefinition(new TokenDefinition("d", "d",
		    Visibility.VISIBLE));

	    ProductionSet productions = new ProductionSet();

	    Production production = new Production("Z");
	    production.addConstruction(new NonTerminal("S"));
	    productions.add(production);

	    production = new Production("S");
	    production.addConstruction(new NonTerminal("C"));
	    production.addConstruction(new NonTerminal("C"));
	    productions.add(production);

	    production = new Production("C");
	    production.addConstruction(new Terminal("c", null));
	    production.addConstruction(new NonTerminal("C"));
	    productions.add(production);

	    production = new Production("C");
	    production.addConstruction(new Terminal("d", null));
	    productions.add(production);
	    return new Grammar(createStandardOptions(), tokenDefinitions,
		    productions);
	} catch (GrammarException e) {
	    e.printStackTrace();
	    fail("No exception was expected!");
	    return null;
	}
    }

    /**
     * This grammar is from page 325 out of the German edition of the Dragon
     * Book.
     * 
     * @return A {@link Grammar} object is returned.
     */
    public static Grammar getLALR1TestGrammarFromDragonBook() {
	try {
	    TokenDefinitionSet tokenDefinitions = new TokenDefinitionSet();

	    tokenDefinitions.addDefinition(new TokenDefinition("id", "id",
		    Visibility.VISIBLE));
	    tokenDefinitions.addDefinition(new TokenDefinition("EQUALS", "=",
		    Visibility.VISIBLE));
	    tokenDefinitions.addDefinition(new TokenDefinition("STAR", "\\*",
		    Visibility.VISIBLE));

	    ProductionSet productions = new ProductionSet();

	    Production production = new Production("Z");
	    production.addConstruction(new NonTerminal("S"));
	    productions.add(production);

	    production = new Production("S");
	    production.addConstruction(new NonTerminal("L"));
	    production.addConstruction(new Terminal("EQUALS", "="));
	    production.addConstruction(new NonTerminal("R"));
	    productions.add(production);

	    production = new Production("S");
	    production.addConstruction(new NonTerminal("R"));
	    productions.add(production);

	    production = new Production("L");
	    production.addConstruction(new Terminal("STAR", "*"));
	    production.addConstruction(new NonTerminal("R"));
	    productions.add(production);

	    production = new Production("L");
	    production.addConstruction(new Terminal("id", null));
	    productions.add(production);

	    production = new Production("R");
	    production.addConstruction(new NonTerminal("L"));
	    productions.add(production);
	    return new Grammar(createStandardOptions(), tokenDefinitions,
		    productions);
	} catch (GrammarException e) {
	    e.printStackTrace();
	    fail("No exception was expected!");
	    return null;
	}
    }

    public static Grammar getLLGrammarFromDragonBook() {
	try {
	    TokenDefinitionSet tokenDefinitions = new TokenDefinitionSet();

	    tokenDefinitions.addDefinition(new TokenDefinition("id", "[0-9.]+",
		    Visibility.VISIBLE));
	    tokenDefinitions.addDefinition(new TokenDefinition("PLUS", "\\+",
		    Visibility.VISIBLE));
	    tokenDefinitions.addDefinition(new TokenDefinition("STAR", "\\*",
		    Visibility.VISIBLE));
	    tokenDefinitions.addDefinition(new TokenDefinition("LPAREN", "\\(",
		    Visibility.VISIBLE));
	    tokenDefinitions.addDefinition(new TokenDefinition("RPAREN", "\\)",
		    Visibility.VISIBLE));
	    tokenDefinitions.addDefinition(new TokenDefinition("WHITESPACE",
		    "[\\s]+", Visibility.VISIBLE));

	    ProductionSet productions = new ProductionSet();

	    Production production = new Production("E");
	    production.addConstruction(new NonTerminal("T"));
	    production.addConstruction(new NonTerminal("E'"));
	    productions.add(production);

	    production = new Production("E'");
	    production.addConstruction(new Terminal("PLUS", "+"));
	    production.addConstruction(new NonTerminal("T"));
	    production.addConstruction(new NonTerminal("E'"));
	    productions.add(production);

	    production = new Production("E'");
	    productions.add(production);

	    production = new Production("T");
	    production.addConstruction(new NonTerminal("F"));
	    production.addConstruction(new NonTerminal("T'"));
	    productions.add(production);

	    production = new Production("T'");
	    production.addConstruction(new Terminal("STAR", "*"));
	    production.addConstruction(new NonTerminal("F"));
	    production.addConstruction(new NonTerminal("T'"));
	    productions.add(production);

	    production = new Production("T'");
	    productions.add(production);

	    production = new Production("F");
	    production.addConstruction(new Terminal("LPAREN", "("));
	    production.addConstruction(new NonTerminal("E"));
	    production.addConstruction(new Terminal("RPAREN", ")"));
	    productions.add(production);

	    production = new Production("F");
	    production.addConstruction(new Terminal("id", null));
	    productions.add(production);

	    return new Grammar(createStandardOptions(), tokenDefinitions,
		    productions);
	} catch (GrammarException e) {
	    e.printStackTrace();
	    fail("No exception was expected!");
	    return null;
	}
    }
}
