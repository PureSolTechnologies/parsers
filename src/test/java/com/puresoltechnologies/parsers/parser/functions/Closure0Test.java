package com.puresoltechnologies.parsers.parser.functions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import com.puresoltechnologies.parsers.grammar.Grammar;
import com.puresoltechnologies.parsers.grammar.TestGrammars;
import com.puresoltechnologies.parsers.parser.items.LR0Item;
import com.puresoltechnologies.parsers.parser.items.LR0ItemSet;

public class Closure0Test {

	@Test
	public void testFromLR1Pamphlet() {
		Grammar grammar = TestGrammars.getGrammarFromLRkPamphlet();
		System.out.println("Productions:");
		System.out.println(grammar.toProductionsString());

		Closure0 closure = new Closure0(grammar);
		System.out.println("Closure0:");
		LR0Item primItem = new LR0Item(grammar.getProductions().get(0), 0);
		LR0ItemSet itemSet = closure.calc(primItem);
		System.out.println(itemSet.toString());

		assertEquals(1, itemSet.getKernelItems().size());
		assertEquals(primItem, itemSet.getKernelItems().iterator().next());

		Set<LR0Item> addedItems = itemSet.getNonKernelItems();
		assertNotNull(addedItems);
		assertEquals(2, addedItems.size());
		Iterator<LR0Item> iterator = addedItems.iterator();
		LR0Item item1 = iterator.next();
		assertEquals(0, item1.getPosition());
		assertEquals(grammar.getProductions().getList().get(1),
				item1.getProduction());
		LR0Item item2 = iterator.next();
		assertEquals(0, item2.getPosition());
		assertEquals(grammar.getProductions().getList().get(2),
				item2.getProduction());
	}

	@Test
	public void testFromDragonBook() {
		Grammar grammar = TestGrammars.getSLR1TestGrammarFromDragonBook();
		System.out.println("Productions:");
		System.out.println(grammar.toProductionsString());

		Closure0 closure = new Closure0(grammar);
		System.out.println("Closure0:");
		LR0Item primItem = new LR0Item(grammar.getProductions().get(0), 0);
		LR0ItemSet itemSet = closure.calc(primItem);
		System.out.println(itemSet.toString());

		assertEquals(1, itemSet.getKernelItems().size());
		assertEquals(primItem, itemSet.getKernelItems().iterator().next());

		Set<LR0Item> addedItems = itemSet.getNonKernelItems();
		assertNotNull(addedItems);
		assertEquals(6, addedItems.size());
		Iterator<LR0Item> iterator = addedItems.iterator();
		LR0Item item1 = iterator.next();
		assertEquals(0, item1.getPosition());
		assertEquals(grammar.getProductions().getList().get(1),
				item1.getProduction());
		LR0Item item2 = iterator.next();
		assertEquals(0, item2.getPosition());
		assertEquals(grammar.getProductions().getList().get(2),
				item2.getProduction());
	}
}
