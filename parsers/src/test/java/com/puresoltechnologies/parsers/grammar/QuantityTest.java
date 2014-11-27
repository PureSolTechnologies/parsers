package com.puresoltechnologies.parsers.grammar;

import static org.junit.Assert.*;

import org.junit.Test;

import com.puresoltechnologies.parsers.grammar.Quantity;

public class QuantityTest {

	@Test
	public void testExpect() {
		assertNull(Quantity.EXPECT.getSymbol());
		assertEquals((Integer) 1, Quantity.EXPECT.getMin());
		assertEquals((Integer) 1, Quantity.EXPECT.getMax());
		assertEquals(Quantity.EXPECT, Quantity.fromSymbol(null));
	}

	@Test
	public void testACCEPT() {
		assertEquals((Character) '?', Quantity.ACCEPT.getSymbol());
		assertEquals((Integer) 0, Quantity.ACCEPT.getMin());
		assertEquals((Integer) 1, Quantity.ACCEPT.getMax());
		assertEquals(Quantity.ACCEPT, Quantity.fromSymbol('?'));
	}

	@Test
	public void testExpectMany() {
		assertEquals((Character) '+', Quantity.EXPECT_MANY.getSymbol());
		assertEquals((Integer) 1, Quantity.EXPECT_MANY.getMin());
		assertNull(Quantity.EXPECT_MANY.getMax());
		assertEquals(Quantity.EXPECT_MANY, Quantity.fromSymbol('+'));
	}

	@Test
	public void testAcceptMany() {
		assertEquals((Character) '*', Quantity.ACCEPT_MANY.getSymbol());
		assertEquals((Integer) 0, Quantity.ACCEPT_MANY.getMin());
		assertNull(Quantity.ACCEPT_MANY.getMax());
		assertEquals(Quantity.ACCEPT_MANY, Quantity.fromSymbol('*'));
	}

}
