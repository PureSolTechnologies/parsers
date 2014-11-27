package com.puresoltechnologies.parsers.parser.items;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.puresoltechnologies.parsers.grammar.production.Production;
import com.puresoltechnologies.parsers.grammar.production.Terminal;

public class LR1ItemTest {

	@Test
	public void testInstance() {
		assertNotNull(new LR1Item(new Production("Production"), 1,
				new Terminal(".", null)));
	}

	@Test
	public void testInitialValues() {
		Production production = new Production("Production");
		Terminal construction = new Terminal(".", null);
		LR1Item item = new LR1Item(production, 1, construction);
		assertSame(production, item.getProduction());
		assertEquals(1, item.getPosition());
		assertSame(construction, item.getLookahead());
	}

	@Test
	public void testGetters() {
		Production production = new Production("Production");
		Terminal construction = new Terminal(".", null);
		LR1Item item = new LR1Item(production, 1, construction);
		assertSame(production, item.getProduction());
		assertEquals(1, item.getPosition());
		assertSame(construction, item.getLookahead());
		assertNull(item.getNext());
		assertNull(item.get2ndNext());
		assertEquals(0, item.getBeta().getConstructions().size());
	}

}
