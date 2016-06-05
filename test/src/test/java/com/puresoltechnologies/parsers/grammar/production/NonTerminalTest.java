package com.puresoltechnologies.parsers.grammar.production;

import static com.puresoltechnologies.parsers.JSONSerializer.fromJSONString;
import static com.puresoltechnologies.parsers.JSONSerializer.toJSONString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class NonTerminalTest {

    @Test
    public void testInstance() {
	assertNotNull(new NonTerminal("NAME"));
    }

    @Test
    public void testInitialValues() {
	Construction nonTerminal = new NonTerminal("NAME");
	assertEquals("NAME", nonTerminal.getName());
	assertTrue(nonTerminal.isNonTerminal());
	assertFalse(nonTerminal.isTerminal());
	assertEquals("NAME: (NON-TERMINAL)", nonTerminal.toString());
	assertEquals("NAME", nonTerminal.toShortString());
    }

    @Test
    public void testSerialization() throws JsonGenerationException, JsonMappingException, IOException {
	Construction nonTerminal = new NonTerminal("NAME");
	String string = toJSONString(nonTerminal);
	Construction nonTerminal2 = fromJSONString(string, Construction.class);
	assertEquals(nonTerminal, nonTerminal2);
    }

}
