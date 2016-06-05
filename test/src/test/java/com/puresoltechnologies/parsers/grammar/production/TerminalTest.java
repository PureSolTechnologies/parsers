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
import com.puresoltechnologies.parsers.grammar.token.Visibility;
import com.puresoltechnologies.parsers.lexer.Token;
import com.puresoltechnologies.parsers.lexer.TokenMetaData;
import com.puresoltechnologies.parsers.source.UnspecifiedSourceCodeLocation;

public class TerminalTest {

    @Test
    public void testInstance() {
	assertNotNull(new Terminal("NAME", "name"));
    }

    @Test
    public void testInitialValues() {
	Terminal terminal = new Terminal("NAME", "name");
	assertEquals("NAME", terminal.getName());
	assertEquals("name", terminal.getText());
	assertFalse(terminal.isNonTerminal());
	assertTrue(terminal.isTerminal());
	assertEquals("NAME: (TERMINAL) 'name'", terminal.toString());
	assertEquals("NAME 'name'", terminal.toShortString());
    }

    @Test
    public void testEquals() {
	assertTrue(new Terminal("NAME", "TEXT").equals(new Terminal("NAME", "TEXT")));
	assertFalse(new Terminal("NAME", "TEXT").equals(new Terminal("NAME", "text")));
    }

    @Test
    public void testMatches() {
	Terminal terminal = new Terminal("TEST", null);
	assertTrue(terminal.matches(new Token("TEST", "anything", Visibility.VISIBLE,
		new TokenMetaData(new UnspecifiedSourceCodeLocation(), 1, 1, 0))));
	terminal = new Terminal("TEST", "");
	assertTrue(terminal.matches(new Token("TEST", "anything", Visibility.VISIBLE,
		new TokenMetaData(new UnspecifiedSourceCodeLocation(), 1, 1, 0))));
	terminal = new Terminal("TEST", "anything");
	assertTrue(terminal.matches(new Token("TEST", "anything", Visibility.VISIBLE,
		new TokenMetaData(new UnspecifiedSourceCodeLocation(), 1, 1, 0))));
    }

    @Test
    public void testSerialization() throws JsonGenerationException, JsonMappingException, IOException {
	Terminal terminal = new Terminal("TEST", null);
	String string = toJSONString(terminal);
	Construction terminal2 = fromJSONString(string, Construction.class);
	assertEquals(terminal, terminal2);
    }
}
