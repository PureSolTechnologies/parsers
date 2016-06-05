package com.puresoltechnologies.parsers.grammar;

import static com.puresoltechnologies.parsers.JSONSerializer.fromJSONString;
import static com.puresoltechnologies.parsers.JSONSerializer.toJSONString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Properties;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.puresoltechnologies.parsers.grammar.production.ProductionSet;
import com.puresoltechnologies.parsers.grammar.token.TokenDefinitionSet;
import com.puresoltechnologies.parsers.lexer.RegExpLexer;
import com.puresoltechnologies.parsers.parser.lr.LR0Parser;

public class GrammarTest {

    @Test(expected = GrammarException.class)
    public void testInvalidInstance() throws GrammarException {
	assertNotNull(new Grammar(new Properties(), new TokenDefinitionSet(), new ProductionSet()));
    }

    @Test
    public void testInstance() throws GrammarException {
	Properties options = new Properties();
	options.put("lexer", RegExpLexer.class.getName());
	options.put("parser", LR0Parser.class.getName());
	options.put("grammar.checks", "false");
	assertNotNull(new Grammar(options, new TokenDefinitionSet(), new ProductionSet()));
    }

    @Test
    public void testSettersAndGetters() throws GrammarException {
	Properties options = new Properties();
	options.put("lexer", RegExpLexer.class.getName());
	options.put("parser", LR0Parser.class.getName());
	options.put("grammar.checks", "false");
	TokenDefinitionSet tokenDefinitions = new TokenDefinitionSet();
	ProductionSet productions = new ProductionSet();

	Grammar grammar = new Grammar(options, tokenDefinitions, productions);
	assertSame(options, grammar.getOptions());
	assertSame(tokenDefinitions, grammar.getTokenDefinitions());
	assertSame(productions, grammar.getProductions());
    }

    @Test
    public void testJSONSerialization() throws JsonGenerationException, JsonMappingException, IOException {
	Grammar grammar = TestGrammars.getLR1TestGrammarFromDragonBook();
	String string = toJSONString(grammar);
	Grammar grammar2 = fromJSONString(string, Grammar.class);
	assertEquals(grammar, grammar2);
    }

    @Test
    public void testJavaSerialization()
	    throws JsonGenerationException, JsonMappingException, IOException, ClassNotFoundException {
	Grammar grammar = TestGrammars.getLR1TestGrammarFromDragonBook();
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
	objectOutputStream.writeObject(grammar);
	ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
	ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
	Grammar grammar2 = (Grammar) objectInputStream.readObject();
	assertEquals(grammar, grammar2);
    }
}
