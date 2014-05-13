package com.puresoltechnologies.parsers.parser.functions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import com.puresoltechnologies.parsers.grammar.Grammar;
import com.puresoltechnologies.parsers.grammar.TestGrammars;
import com.puresoltechnologies.parsers.grammar.production.Construction;
import com.puresoltechnologies.parsers.grammar.production.EmptyTerminal;
import com.puresoltechnologies.parsers.grammar.production.NonTerminal;
import com.puresoltechnologies.parsers.grammar.production.Terminal;

public class FirstTest {

	@Test
	public void testLRPamphletGrammar() {
		System.out.println("=====================");
		System.out.println("LR Pampghlet Grammar:");
		System.out.println("=====================");
		Grammar grammar = TestGrammars.getGrammarFromLRkPamphlet();
		System.out.println("Productions:");
		System.out.println(grammar.toProductionsString());

		First first = new First(grammar);
		System.out.println("First:");
		System.out.println(first.toString());

		Construction productionZ = new NonTerminal("Z");
		Construction productionS = new NonTerminal("S");
		Construction productionA = new NonTerminal("A");
		Construction terminalA = new Terminal("a", null);
		Construction terminalB = new Terminal("b", null);
		Construction terminalC = new Terminal("c", null);

		assertEquals(1, first.get(productionZ).size());
		assertEquals(terminalB, first.get(productionZ).iterator().next());

		assertEquals(1, first.get(productionS).size());
		assertEquals(terminalB, first.get(productionS).iterator().next());

		assertEquals(1, first.get(productionA).size());
		assertEquals(terminalA, first.get(productionA).iterator().next());

		assertEquals(1, first.get(terminalA).size());
		assertEquals(terminalA, first.get(terminalA).iterator().next());

		assertEquals(1, first.get(terminalB).size());
		assertEquals(terminalB, first.get(terminalB).iterator().next());

		assertEquals(1, first.get(terminalC).size());
		assertEquals(terminalC, first.get(terminalC).iterator().next());
	}

	@Test
	public void testDragonBookGrammar() {
		System.out.println("====================");
		System.out.println("Dragon Book Grammar:");
		System.out.println("====================");
		Grammar grammar = TestGrammars.getSLR1TestGrammarFromDragonBook();
		System.out.println("Productions:");
		System.out.println(grammar.toProductionsString());

		First first = new First(grammar);
		System.out.println("First:");
		System.out.println(first.toString());

		Construction productionZ = new NonTerminal("Z");
		Construction productionE = new NonTerminal("E");
		Construction productionT = new NonTerminal("T");
		Construction productionF = new NonTerminal("F");
		Construction terminalId = new Terminal("id", null);
		Construction terminalLParen = new Terminal("LPAREN", "(");

		assertEquals(2, first.get(productionZ).size());
		Iterator<Terminal> iterator = first.get(productionZ).iterator();
		assertEquals(terminalLParen, iterator.next());
		assertEquals(terminalId, iterator.next());

		assertEquals(2, first.get(productionE).size());
		iterator = first.get(productionE).iterator();
		assertEquals(terminalLParen, iterator.next());
		assertEquals(terminalId, iterator.next());

		assertEquals(2, first.get(productionT).size());
		iterator = first.get(productionT).iterator();
		assertEquals(terminalLParen, iterator.next());
		assertEquals(terminalId, iterator.next());

		assertEquals(2, first.get(productionF).size());
		iterator = first.get(productionF).iterator();
		assertEquals(terminalLParen, iterator.next());
		assertEquals(terminalId, iterator.next());

	}

	@Test
	public void testDragonBookLLGrammar() {
		System.out.println("=======================");
		System.out.println("Dragon Book LL Grammar:");
		System.out.println("=======================");
		Grammar grammar = TestGrammars.getLLGrammarFromDragonBook();
		System.out.println("Productions:");
		System.out.println(grammar.toProductionsString());

		First first = new First(grammar);
		System.out.println("First:");
		System.out.println(first.toString());

		Construction productionE = new NonTerminal("E");
		Construction productionES = new NonTerminal("E'");
		Construction productionT = new NonTerminal("T");
		Construction productionTS = new NonTerminal("T'");
		Construction productionF = new NonTerminal("F");
		Construction terminalId = new Terminal("id", null);
		Construction terminalLParen = new Terminal("LPAREN", "(");
		Construction terminalStar = new Terminal("STAR", "*");
		Construction terminalPlus = new Terminal("PLUS", "+");
		Construction empty = EmptyTerminal.getInstance();

		assertEquals(2, first.get(productionE).size());
		Iterator<Terminal> iterator = first.get(productionF).iterator();
		assertEquals(terminalLParen, iterator.next());
		assertEquals(terminalId, iterator.next());

		assertEquals(2, first.get(productionT).size());
		iterator = first.get(productionT).iterator();
		assertEquals(terminalLParen, iterator.next());
		assertEquals(terminalId, iterator.next());

		assertEquals(2, first.get(productionF).size());
		iterator = first.get(productionF).iterator();
		assertEquals(terminalLParen, iterator.next());
		assertEquals(terminalId, iterator.next());

		assertEquals(2, first.get(productionES).size());
		iterator = first.get(productionES).iterator();
		assertEquals(terminalPlus, iterator.next());
		assertEquals(empty, iterator.next());

		assertEquals(2, first.get(productionTS).size());
		iterator = first.get(productionTS).iterator();
		assertEquals(terminalStar, iterator.next());
		assertEquals(empty, iterator.next());

	}

	/**
	 * This test is a part of the results on page 327-328 in Dragon book.
	 */
	@Test
	public void testFirstForLALR1GrammarStartItem() {

		Grammar grammar = TestGrammars.getLALR1TestGrammarFromDragonBook();

		First first = new First(grammar);
		Set<Terminal> constructions = first
				.get(grammar.getProductions().get(0));
		assertEquals(2, constructions.size());
		assertTrue(constructions.contains(new Terminal("id", null)));
		assertTrue(constructions.contains(new Terminal("STAR", "*")));

	}

}
