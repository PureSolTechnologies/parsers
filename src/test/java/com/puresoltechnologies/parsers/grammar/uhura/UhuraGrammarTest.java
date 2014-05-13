package com.puresoltechnologies.parsers.grammar.uhura;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.puresoltechnologies.parsers.grammar.Grammar;
import com.puresoltechnologies.parsers.grammar.GrammarException;
import com.puresoltechnologies.parsers.grammar.internal.UhuraGrammar;
import com.puresoltechnologies.parsers.parser.functions.Closure0;
import com.puresoltechnologies.parsers.parser.functions.Closure1;
import com.puresoltechnologies.parsers.parser.functions.First;
import com.puresoltechnologies.parsers.parser.functions.Follow;
import com.puresoltechnologies.parsers.parser.functions.Goto0;
import com.puresoltechnologies.parsers.parser.functions.Goto1;
import com.puresoltechnologies.parsers.parser.lr.LR0ItemSetCollection;
import com.puresoltechnologies.parsers.parser.lr.LR1ItemSetCollection;
import com.puresoltechnologies.parsers.parser.lr.LR1ParserTable;
import com.puresoltechnologies.parsers.parser.lr.SLR1ParserTable;

public class UhuraGrammarTest {

	@Test
	public void test() {
		Grammar grammar = UhuraGrammar.getGrammar();
		assertNotNull(grammar);
		System.out.println(grammar);
	}

	@Test
	public void testFirst() {
		System.out.println("======");
		System.out.println("First:");
		System.out.println("======");
		Grammar grammar = UhuraGrammar.getGrammar();
		First first = new First(grammar);
		System.out.println(first.toString());
	}

	@Test
	public void testFollow() {
		System.out.println("=======");
		System.out.println("Follow:");
		System.out.println("=======");
		Grammar grammar = UhuraGrammar.getGrammar();
		Follow follow = new Follow(grammar, new First(grammar));
		System.out.println(follow.toString());
	}

	@Test
	public void testLR0TransitionTable() {
		try {
			System.out.println("=============================");
			System.out.println("LR(0) State Transition Graph:");
			System.out.println("=============================");
			Grammar grammar = UhuraGrammar.getGrammar();
			Closure0 closure0 = new Closure0(grammar);
			Goto0 goto0 = new Goto0(closure0);
			LR0ItemSetCollection first = new LR0ItemSetCollection(grammar,
					closure0, goto0);
			System.out.println(first.toString());
		} catch (GrammarException e) {
			e.printStackTrace();
			fail("No exception was expected!");
		}
	}

	@Test
	public void testSLR1ParseTable() {
		try {
			System.out.println("===================");
			System.out.println("SLR(1) Parse Table:");
			System.out.println("===================");
			Grammar grammar = UhuraGrammar.getGrammar();
			SLR1ParserTable lr0ParserTable = new SLR1ParserTable(grammar);
			System.out.println(lr0ParserTable.toString());
		} catch (GrammarException e) {
			e.printStackTrace();
			fail("No exception was expected!");
		}
	}

	@Test
	public void testLR1TransitionTable() {
		try {
			System.out.println("=============================");
			System.out.println("LR(1) State Transition Graph:");
			System.out.println("=============================");
			Grammar grammar = UhuraGrammar.getGrammar();
			First first = new First(grammar);
			Closure1 closure1 = new Closure1(grammar, first);
			Goto1 goto1 = new Goto1(closure1);
			LR1ItemSetCollection itemSetCollection = new LR1ItemSetCollection(
					grammar, closure1, goto1);
			System.out.println(itemSetCollection.toString());
		} catch (GrammarException e) {
			e.printStackTrace();
			fail("No exception was expected!");
		}
	}

	@Test
	public void testLR1ParseTable() {
		try {
			System.out.println("==================");
			System.out.println("LR(1) Parse Table:");
			System.out.println("==================");
			Grammar grammar = UhuraGrammar.getGrammar();
			LR1ParserTable lr1ParserTable = new LR1ParserTable(grammar);
			System.out.println(lr1ParserTable.toString());
		} catch (GrammarException e) {
			e.printStackTrace();
			fail("No exception was expected!");
		}
	}

}
