package com.puresoltechnologies.parsers.parser.parsetable;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.puresoltechnologies.parsers.parser.parsetable.ActionType;
import com.puresoltechnologies.parsers.parser.parsetable.ParserAction;

public class ParserActionTest {

	@Test
	public void testInstance() {
		assertNotNull(new ParserAction(ActionType.SHIFT, 1));
	}

	@Test
	public void testInitialValue() {
		ParserAction action = new ParserAction(ActionType.SHIFT, 1);
		assertEquals(ActionType.SHIFT, action.getAction());
		assertEquals(1, action.getParameter());
	}

	@Test
	public void testCompareTo() {
		ParserAction s1 = new ParserAction(ActionType.SHIFT, 1);
		ParserAction s2 = new ParserAction(ActionType.SHIFT, 2);

		ParserAction r1 = new ParserAction(ActionType.REDUCE, 1);
		ParserAction r2 = new ParserAction(ActionType.REDUCE, 2);

		List<ParserAction> actions = new ArrayList<ParserAction>();
		actions.add(r2);
		actions.add(s2);
		actions.add(r1);
		actions.add(s1);
		Collections.sort(actions);
		for (ParserAction action : actions) {
			System.out.println(action);
		}
		assertEquals(s2, actions.get(0));
		assertEquals(s1, actions.get(1));
		assertEquals(r2, actions.get(2));
		assertEquals(r1, actions.get(3));
	}

}
