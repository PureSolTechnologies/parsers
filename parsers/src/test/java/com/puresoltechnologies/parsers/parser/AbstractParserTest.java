package com.puresoltechnologies.parsers.parser;

import static org.junit.Assert.*;

import org.junit.Test;

import com.puresoltechnologies.parsers.parser.AbstractParser;

public class AbstractParserTest {

	@Test
	public void testCloningPreconditions() {
		try {
			AbstractParser.class.getDeclaredField("grammar");
			AbstractParser.class.getDeclaredField("tokenStream");
		} catch (SecurityException e) {
			e.printStackTrace();
			fail("No exception was expected!");
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
			fail("No exception was expected!");
		}
	}

}
