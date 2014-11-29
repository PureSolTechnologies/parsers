package com.puresoltechnologies.parsers.parser.parsetable;

import static org.junit.Assert.*;

import org.junit.Test;

import com.puresoltechnologies.parsers.grammar.GrammarException;
import com.puresoltechnologies.parsers.parser.parsetable.ActionType;
import com.puresoltechnologies.parsers.parser.parsetable.ParserAction;
import com.puresoltechnologies.parsers.parser.parsetable.ParserActionSet;

public class ParserActionSetTest {

	@Test
	public void testInstance() {
		assertNotNull(new ParserActionSet());
	}

	@Test
	public void testDefaultValues() {
		try {
			ParserActionSet set = new ParserActionSet();
			assertEquals(1, set.getActionNumber());
			assertEquals(new ParserAction(ActionType.ERROR, -1),
					set.getAction(0));
			assertEquals(new ParserAction(ActionType.ERROR, -1),
					set.getAction());
		} catch (GrammarException e) {
			e.printStackTrace();
			fail("No exception was expected!");
		}
	}

	@Test
	public void testAddAction() {
		ParserActionSet set = new ParserActionSet();

		set.addAction(new ParserAction(ActionType.SHIFT, 2));

		assertEquals(1, set.getActionNumber());
		assertEquals(new ParserAction(ActionType.SHIFT, 2), set.getAction(0));
		try {
			assertEquals(new ParserAction(ActionType.SHIFT, 2), set.getAction());
		} catch (GrammarException e) {
			e.printStackTrace();
			fail("No exception was expected!");
		}

		set.addAction(new ParserAction(ActionType.REDUCE, 12));

		assertEquals(2, set.getActionNumber());
		assertEquals(new ParserAction(ActionType.SHIFT, 2), set.getAction(0));
		assertEquals(new ParserAction(ActionType.REDUCE, 12), set.getAction(1));
		try {
			assertEquals(new ParserAction(ActionType.SHIFT, 2), set.getAction());
			fail("A grammar exception was expected!");
		} catch (GrammarException e) {
			// nothing to catch...
		}
	}

	@Test
	public void testActionSorting() {
		ParserAction r1 = new ParserAction(ActionType.REDUCE, 1);
		ParserAction s3 = new ParserAction(ActionType.SHIFT, 3);
		ParserAction r2 = new ParserAction(ActionType.REDUCE, 2);

		ParserActionSet set = new ParserActionSet();

		set.addAction(r1);
		set.addAction(s3);
		set.addAction(r2);

		assertEquals(s3, set.getAction(0));
		assertEquals(r2, set.getAction(1));
		assertEquals(r1, set.getAction(2));
	}
}
