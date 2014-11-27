package com.puresoltechnologies.parsers.parser.lr;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.puresoltechnologies.parsers.grammar.Grammar;
import com.puresoltechnologies.parsers.grammar.GrammarException;
import com.puresoltechnologies.parsers.grammar.TestGrammars;
import com.puresoltechnologies.parsers.grammar.production.Construction;
import com.puresoltechnologies.parsers.grammar.production.FinishTerminal;
import com.puresoltechnologies.parsers.grammar.production.NonTerminal;
import com.puresoltechnologies.parsers.grammar.production.Terminal;
import com.puresoltechnologies.parsers.parser.parsetable.ActionType;
import com.puresoltechnologies.parsers.parser.parsetable.ParserAction;
import com.puresoltechnologies.parsers.parser.parsetable.ParserTable;

public class LR1ParserTableTest {

	@Test
	public void testLRPamphletGrammar() {
		try {
			System.out.println("=====================");
			System.out.println("LR Pampghlet Grammar:");
			System.out.println("=====================");
			Grammar grammar = TestGrammars.getGrammarFromLRkPamphlet();
			System.out.println("Productions:");
			System.out.println(grammar.toProductionsString());
			ParserTable table = new LR1ParserTable(grammar);
			System.out.println(table.toString());

			// expected actions in table...
			ParserAction r1 = new ParserAction(ActionType.REDUCE, 1);
			ParserAction r2 = new ParserAction(ActionType.REDUCE, 2);
			ParserAction r3 = new ParserAction(ActionType.REDUCE, 3);
			ParserAction r4 = new ParserAction(ActionType.REDUCE, 4);
			ParserAction r5 = new ParserAction(ActionType.REDUCE, 5);
			ParserAction s2 = new ParserAction(ActionType.SHIFT, 2);
			ParserAction s3 = new ParserAction(ActionType.SHIFT, 3);
			ParserAction s5 = new ParserAction(ActionType.SHIFT, 5);
			ParserAction s6 = new ParserAction(ActionType.SHIFT, 6);
			ParserAction s8 = new ParserAction(ActionType.SHIFT, 8);
			ParserAction s9 = new ParserAction(ActionType.SHIFT, 9);
			ParserAction s10 = new ParserAction(ActionType.SHIFT, 10);
			ParserAction s12 = new ParserAction(ActionType.SHIFT, 12);
			ParserAction g1 = new ParserAction(ActionType.GOTO, 1);
			ParserAction g4 = new ParserAction(ActionType.GOTO, 4);
			ParserAction g7 = new ParserAction(ActionType.GOTO, 7);
			ParserAction g11 = new ParserAction(ActionType.GOTO, 11);
			ParserAction accept = new ParserAction(ActionType.ACCEPT, -1);
			ParserAction error = new ParserAction(ActionType.ERROR, -1);
			Construction a = new Terminal("a", null);
			Construction b = new Terminal("b", null);
			Construction c = new Terminal("c", null);
			Construction pS = new NonTerminal("S");
			Construction pA = new NonTerminal("A");
			Construction finish = FinishTerminal.getInstance();

			// checks...
			assertEquals(s2, table.getAction(0, b));
			assertEquals(error, table.getAction(0, a));
			assertEquals(error, table.getAction(0, c));
			assertEquals(error, table.getAction(0, finish));
			assertEquals(g1, table.getAction(0, pS));
			assertEquals(error, table.getAction(0, pA));

			assertEquals(s3, table.getAction(1, b));
			assertEquals(error, table.getAction(1, a));
			assertEquals(error, table.getAction(1, c));
			assertEquals(accept, table.getAction(1, finish));
			assertEquals(error, table.getAction(1, pS));
			assertEquals(error, table.getAction(1, pA));

			assertEquals(error, table.getAction(2, b));
			assertEquals(s5, table.getAction(2, a));
			assertEquals(error, table.getAction(2, c));
			assertEquals(error, table.getAction(2, finish));
			assertEquals(error, table.getAction(2, pS));
			assertEquals(g4, table.getAction(2, pA));

			assertEquals(r1, table.getAction(3, b));
			assertEquals(error, table.getAction(3, a));
			assertEquals(error, table.getAction(3, c));
			assertEquals(r1, table.getAction(3, finish));
			assertEquals(error, table.getAction(3, pS));
			assertEquals(error, table.getAction(3, pA));

			assertEquals(error, table.getAction(4, b));
			assertEquals(s6, table.getAction(4, a));
			assertEquals(error, table.getAction(4, c));
			assertEquals(error, table.getAction(4, finish));
			assertEquals(error, table.getAction(4, pS));
			assertEquals(error, table.getAction(4, pA));

			assertEquals(s8, table.getAction(5, b));
			assertEquals(r4, table.getAction(5, a));
			assertEquals(error, table.getAction(5, c));
			assertEquals(error, table.getAction(5, finish));
			assertEquals(g7, table.getAction(5, pS));
			assertEquals(error, table.getAction(5, pA));

			assertEquals(r2, table.getAction(6, b));
			assertEquals(error, table.getAction(6, a));
			assertEquals(error, table.getAction(6, c));
			assertEquals(r2, table.getAction(6, finish));
			assertEquals(error, table.getAction(6, pS));
			assertEquals(error, table.getAction(6, pA));

			assertEquals(s10, table.getAction(7, b));
			assertEquals(error, table.getAction(7, a));
			assertEquals(s9, table.getAction(7, c));
			assertEquals(error, table.getAction(7, finish));
			assertEquals(error, table.getAction(7, pS));
			assertEquals(error, table.getAction(7, pA));

			assertEquals(error, table.getAction(8, b));
			assertEquals(s5, table.getAction(8, a));
			assertEquals(error, table.getAction(8, c));
			assertEquals(error, table.getAction(8, finish));
			assertEquals(error, table.getAction(8, pS));
			assertEquals(g11, table.getAction(8, pA));

			assertEquals(error, table.getAction(9, b));
			assertEquals(r3, table.getAction(9, a));
			assertEquals(error, table.getAction(9, c));
			assertEquals(error, table.getAction(9, finish));
			assertEquals(error, table.getAction(9, pS));
			assertEquals(error, table.getAction(9, pA));

			assertEquals(r1, table.getAction(10, b));
			assertEquals(r5, table.getAction(10, a));
			assertEquals(r1, table.getAction(10, c));
			assertEquals(error, table.getAction(10, finish));
			assertEquals(error, table.getAction(10, pS));
			assertEquals(error, table.getAction(10, pA));

			assertEquals(error, table.getAction(11, b));
			assertEquals(s12, table.getAction(11, a));
			assertEquals(error, table.getAction(11, c));
			assertEquals(error, table.getAction(11, finish));
			assertEquals(error, table.getAction(11, pS));
			assertEquals(error, table.getAction(11, pA));

			assertEquals(r2, table.getAction(12, b));
			assertEquals(error, table.getAction(12, a));
			assertEquals(r2, table.getAction(12, c));
			assertEquals(error, table.getAction(12, finish));
			assertEquals(error, table.getAction(12, pS));
			assertEquals(error, table.getAction(12, pA));

		} catch (GrammarException e) {
			fail("No Exception was expected!");
		}
	}

	/**
	 * The solution can be found at page 319 of 2. German edition of Dragon
	 * Book.
	 */
	@Test
	public void testDragonBookGrammar() {
		try {
			System.out.println("====================");
			System.out.println("Dragon Book Grammar:");
			System.out.println("====================");
			Grammar grammar = TestGrammars.getLR1TestGrammarFromDragonBook();
			System.out.println("Productions:");
			System.out.println(grammar.toProductionsString());
			ParserTable table = new LR1ParserTable(grammar);
			System.out.println(table.toString());

			// expected actions in table...
			ParserAction r1 = new ParserAction(ActionType.REDUCE, 1);
			ParserAction r2 = new ParserAction(ActionType.REDUCE, 2);
			ParserAction r3 = new ParserAction(ActionType.REDUCE, 3);
			ParserAction s3 = new ParserAction(ActionType.SHIFT, 3);
			ParserAction s4 = new ParserAction(ActionType.SHIFT, 4);
			ParserAction s6 = new ParserAction(ActionType.SHIFT, 6);
			ParserAction s7 = new ParserAction(ActionType.SHIFT, 7);
			ParserAction g1 = new ParserAction(ActionType.GOTO, 1);
			ParserAction g2 = new ParserAction(ActionType.GOTO, 2);
			ParserAction g5 = new ParserAction(ActionType.GOTO, 5);
			ParserAction g8 = new ParserAction(ActionType.GOTO, 8);
			ParserAction g9 = new ParserAction(ActionType.GOTO, 9);
			ParserAction accept = new ParserAction(ActionType.ACCEPT, -1);
			ParserAction error = new ParserAction(ActionType.ERROR, -1);
			Construction c = new Terminal("c", null);
			Construction d = new Terminal("d", null);
			Construction finish = FinishTerminal.getInstance();
			Construction pS = new NonTerminal("S");
			Construction pC = new NonTerminal("C");

			// checks...
			assertEquals(s3, table.getAction(0, c));
			assertEquals(s4, table.getAction(0, d));
			assertEquals(error, table.getAction(0, finish));
			assertEquals(g1, table.getAction(0, pS));
			assertEquals(g2, table.getAction(0, pC));

			assertEquals(error, table.getAction(1, c));
			assertEquals(error, table.getAction(1, d));
			assertEquals(accept, table.getAction(1, finish));
			assertEquals(error, table.getAction(1, pS));
			assertEquals(error, table.getAction(1, pC));

			assertEquals(s6, table.getAction(2, c));
			assertEquals(s7, table.getAction(2, d));
			assertEquals(error, table.getAction(2, finish));
			assertEquals(error, table.getAction(2, pS));
			assertEquals(g5, table.getAction(2, pC));

			assertEquals(s3, table.getAction(3, c));
			assertEquals(s4, table.getAction(3, d));
			assertEquals(error, table.getAction(3, finish));
			assertEquals(error, table.getAction(3, pS));
			assertEquals(g8, table.getAction(3, pC));

			assertEquals(r3, table.getAction(4, c));
			assertEquals(r3, table.getAction(4, d));
			assertEquals(error, table.getAction(4, finish));
			assertEquals(error, table.getAction(4, pS));
			assertEquals(error, table.getAction(4, pC));

			assertEquals(error, table.getAction(5, c));
			assertEquals(error, table.getAction(5, d));
			assertEquals(r1, table.getAction(5, finish));
			assertEquals(error, table.getAction(5, pS));
			assertEquals(error, table.getAction(5, pC));

			assertEquals(s6, table.getAction(6, c));
			assertEquals(s7, table.getAction(6, d));
			assertEquals(error, table.getAction(6, finish));
			assertEquals(error, table.getAction(6, pS));
			assertEquals(g9, table.getAction(6, pC));

			assertEquals(error, table.getAction(7, c));
			assertEquals(error, table.getAction(7, d));
			assertEquals(r3, table.getAction(7, finish));
			assertEquals(error, table.getAction(7, pS));
			assertEquals(error, table.getAction(7, pC));

			assertEquals(r2, table.getAction(8, c));
			assertEquals(r2, table.getAction(8, d));
			assertEquals(error, table.getAction(8, finish));
			assertEquals(error, table.getAction(8, pS));
			assertEquals(error, table.getAction(8, pC));

			assertEquals(error, table.getAction(9, c));
			assertEquals(error, table.getAction(9, d));
			assertEquals(r2, table.getAction(9, finish));
			assertEquals(error, table.getAction(9, pS));
			assertEquals(error, table.getAction(9, pC));

		} catch (GrammarException e) {
			e.printStackTrace();
			fail("No exception was expected!");
		}
	}

}
