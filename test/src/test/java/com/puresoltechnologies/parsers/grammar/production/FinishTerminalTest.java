package com.puresoltechnologies.parsers.grammar.production;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

public class FinishTerminalTest {

    @Test
    public void testSingleton() {
	Construction finish = FinishTerminal.getInstance();
	assertNotNull(finish);
	assertSame(finish, FinishTerminal.getInstance());
	assertEquals("_FINISH_", finish.getName());
    }
}
