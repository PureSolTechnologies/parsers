package com.puresoltechnologies.parsers.parser.lr;

import org.junit.Test;

import com.puresoltechnologies.parsers.parser.lr.AbstractLRParser;

public class AbstractLRParserTest {

	@Test
	public void testCloningPreconditions() throws Throwable {
		AbstractLRParser.class.getDeclaredField("backtrackEnabled");
		AbstractLRParser.class.getDeclaredField("backtrackDepth");
		AbstractLRParser.class.getDeclaredField("timeout");
		AbstractLRParser.class.getDeclaredField("parserTable");
		AbstractLRParser.class.getDeclaredField("backtrackStack");
		AbstractLRParser.class.getDeclaredField("stateStack");
		AbstractLRParser.class.getDeclaredField("actionStack");
		AbstractLRParser.class.getDeclaredField("parserErrors");
	}
}
