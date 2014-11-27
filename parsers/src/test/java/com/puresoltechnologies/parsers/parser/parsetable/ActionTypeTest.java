package com.puresoltechnologies.parsers.parser.parsetable;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.puresoltechnologies.parsers.parser.parsetable.ActionType;

/**
 * This class checks the ActionType class which represents the four different
 * actions a parser can perform during its run.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class ActionTypeTest {

	/**
	 * This number of actions in the enum ActionType is exactly and limited to
	 * five.
	 */
	@Test
	public void testConstantNumber() {
		assertEquals(5, ActionType.class.getEnumConstants().length);
	}

	/**
	 * This test checks the coding standards for the four states as used in the
	 * Dragon Book.
	 */
	@Test
	public void testToString() {
		assertEquals("acc", ActionType.ACCEPT.toString());
		assertEquals("s", ActionType.SHIFT.toString());
		assertEquals("r", ActionType.REDUCE.toString());
		assertEquals("err", ActionType.ERROR.toString());
		assertEquals("", ActionType.GOTO.toString());
	}

	/**
	 * This test checks the behavior for comparison. This is used to force one
	 * action before another.
	 */
	@Test
	public void testCompareTo() {
		assertTrue(ActionType.SHIFT.compareTo(ActionType.ACCEPT) > 0);
		assertTrue(ActionType.REDUCE.compareTo(ActionType.SHIFT) > 0);
		assertTrue(ActionType.GOTO.compareTo(ActionType.REDUCE) > 0);
		assertTrue(ActionType.ERROR.compareTo(ActionType.GOTO) > 0);
	}

	@Test
	public void testSort() {
		List<ActionType> actions = new ArrayList<ActionType>();
		actions.add(ActionType.GOTO);
		actions.add(ActionType.ERROR);
		actions.add(ActionType.REDUCE);
		actions.add(ActionType.SHIFT);
		actions.add(ActionType.ACCEPT);
		Collections.sort(actions);
		assertEquals(ActionType.ACCEPT, actions.get(0));
		assertEquals(ActionType.SHIFT, actions.get(1));
		assertEquals(ActionType.REDUCE, actions.get(2));
		assertEquals(ActionType.GOTO, actions.get(3));
		assertEquals(ActionType.ERROR, actions.get(4));
	}
}
