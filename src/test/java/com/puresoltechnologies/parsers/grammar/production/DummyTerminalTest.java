package com.puresoltechnologies.parsers.grammar.production;

import static org.junit.Assert.*;

import org.junit.Test;

import com.puresoltechnologies.parsers.grammar.production.Construction;
import com.puresoltechnologies.parsers.grammar.production.DummyTerminal;

public class DummyTerminalTest {

	@Test
	public void testSingleton() {
		Construction empty = DummyTerminal.getInstance();
		assertNotNull(empty);
		assertSame(empty, DummyTerminal.getInstance());
		assertEquals("_DUMMY_", empty.getName());
	}

}
