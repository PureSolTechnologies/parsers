package com.puresoltechnologies.parsers.parser.lr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.puresoltechnologies.parsers.grammar.Grammar;
import com.puresoltechnologies.parsers.grammar.GrammarException;
import com.puresoltechnologies.parsers.grammar.TestGrammars;
import com.puresoltechnologies.parsers.grammar.production.NonTerminal;
import com.puresoltechnologies.parsers.grammar.production.Terminal;
import com.puresoltechnologies.parsers.parser.functions.Closure0;
import com.puresoltechnologies.parsers.parser.functions.Goto0;

public class LR0StateTransitionsTest {

	/**
	 * This test is from picture 4.31 from German edition of the Dragon Book at
	 * page 294.
	 */
	@Test
	public void test() {
		try {
			Grammar grammar = TestGrammars.getSLR1TestGrammarFromDragonBook();
			Closure0 closure0 = new Closure0(grammar);
			Goto0 goto0 = new Goto0(closure0);
			LR0ItemSetCollection itemSetCollection = new LR0ItemSetCollection(
					grammar, closure0, goto0);
			LR0StateTransitions transitions = new LR0StateTransitions(
					itemSetCollection, goto0);

			assertEquals(5, transitions.getTransitions(0).size());
			assertTrue(transitions.getTransition(0, new NonTerminal("E")) == 1);
			assertTrue(transitions.getTransition(0, new NonTerminal("T")) == 2);
			assertTrue(transitions.getTransition(0, new Terminal("id", null)) == 5);
			assertTrue(transitions
					.getTransition(0, new Terminal("LPAREN", "(")) == 4);
			assertTrue(transitions.getTransition(0, new NonTerminal("F")) == 3);

			assertEquals(1, transitions.getTransitions(1).size());
			assertTrue(transitions.getTransition(1, new Terminal("PLUS", "+")) == 6);

			assertEquals(1, transitions.getTransitions(2).size());
			assertTrue(transitions.getTransition(2, new Terminal("STAR", "*")) == 7);

			assertEquals(0, transitions.getTransitions(3).size());

			assertEquals(5, transitions.getTransitions(4).size());
			assertTrue(transitions.getTransition(4, new Terminal("id", null)) == 5);
			assertTrue(transitions.getTransition(4, new NonTerminal("E")) == 8);
			assertTrue(transitions
					.getTransition(4, new Terminal("LPAREN", "(")) == 4);
			assertTrue(transitions.getTransition(4, new NonTerminal("T")) == 2);
			assertTrue(transitions.getTransition(4, new NonTerminal("F")) == 3);

			assertEquals(0, transitions.getTransitions(5).size());

			assertEquals(4, transitions.getTransitions(6).size());
			assertTrue(transitions.getTransition(6, new NonTerminal("T")) == 9);
			assertTrue(transitions.getTransition(6, new NonTerminal("F")) == 3);
			assertTrue(transitions
					.getTransition(6, new Terminal("LPAREN", "(")) == 4);
			assertTrue(transitions.getTransition(6, new Terminal("id", null)) == 5);

			assertEquals(3, transitions.getTransitions(7).size());
			assertTrue(transitions.getTransition(7, new NonTerminal("F")) == 10);
			assertTrue(transitions
					.getTransition(7, new Terminal("LPAREN", "(")) == 4);
			assertTrue(transitions.getTransition(7, new Terminal("id", null)) == 5);

			assertEquals(2, transitions.getTransitions(8).size());
			assertTrue(transitions.getTransition(8, new Terminal("PLUS", "+")) == 6);
			assertTrue(transitions
					.getTransition(8, new Terminal("RPAREN", ")")) == 11);

			assertEquals(1, transitions.getTransitions(9).size());
			assertTrue(transitions.getTransition(9, new Terminal("STAR", "*")) == 7);

			assertEquals(0, transitions.getTransitions(10).size());

			assertEquals(0, transitions.getTransitions(11).size());

		} catch (GrammarException e) {
			e.printStackTrace();
			fail("No exception was expected!");
		}
	}

}
