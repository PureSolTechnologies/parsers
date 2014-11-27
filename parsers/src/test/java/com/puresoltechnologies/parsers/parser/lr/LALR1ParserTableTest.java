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

public class LALR1ParserTableTest {

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
			ParserTable table = new LALR1ParserTable(grammar);
			System.out.println(table.toString());

			// expected actions in table...
			ParserAction r1 = new ParserAction(ActionType.REDUCE, 1);
			ParserAction r2 = new ParserAction(ActionType.REDUCE, 2);
			ParserAction r3 = new ParserAction(ActionType.REDUCE, 3);
			ParserAction s3 = new ParserAction(ActionType.SHIFT, 3);
			ParserAction s4 = new ParserAction(ActionType.SHIFT, 4);
			ParserAction g1 = new ParserAction(ActionType.GOTO, 1);
			ParserAction g2 = new ParserAction(ActionType.GOTO, 2);
			ParserAction g5 = new ParserAction(ActionType.GOTO, 5);
			ParserAction g6 = new ParserAction(ActionType.GOTO, 6);
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

			assertEquals(s3, table.getAction(2, c));
			assertEquals(s4, table.getAction(2, d));
			assertEquals(error, table.getAction(2, finish));
			assertEquals(error, table.getAction(2, pS));
			assertEquals(g5, table.getAction(2, pC));

			assertEquals(s3, table.getAction(3, c));
			assertEquals(s4, table.getAction(3, d));
			assertEquals(error, table.getAction(3, finish));
			assertEquals(error, table.getAction(3, pS));
			assertEquals(g6, table.getAction(3, pC));

			assertEquals(r3, table.getAction(4, c));
			assertEquals(r3, table.getAction(4, d));
			assertEquals(r3, table.getAction(4, finish));
			assertEquals(error, table.getAction(4, pS));
			assertEquals(error, table.getAction(4, pC));

			assertEquals(error, table.getAction(5, c));
			assertEquals(error, table.getAction(5, d));
			assertEquals(r1, table.getAction(5, finish));
			assertEquals(error, table.getAction(5, pS));
			assertEquals(error, table.getAction(5, pC));

			assertEquals(r2, table.getAction(6, c));
			assertEquals(r2, table.getAction(6, d));
			assertEquals(r2, table.getAction(6, finish));
			assertEquals(error, table.getAction(6, pS));
			assertEquals(error, table.getAction(6, pC));

		} catch (GrammarException e) {
			e.printStackTrace();
			fail("No exception was expected!");
		}
	}

}
