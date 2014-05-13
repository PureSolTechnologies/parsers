package com.puresoltechnologies.parsers.ust;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.puresoltechnologies.parsers.ust.NameTranslator;

public class NameTranslatorTest {

	@Test
	public void test() {
		String productionClassName = NameTranslator
				.getProductionClassName("CorrectName");
		assertEquals("CorrectName", productionClassName);
	}

	@Test
	public void test2() {
		String productionClassName = NameTranslator
				.getProductionClassName("correctName");
		assertEquals("CorrectName", productionClassName);
	}

	@Test
	public void test3() {
		String productionClassName = NameTranslator
				.getProductionClassName("correct-name");
		assertEquals("CorrectName", productionClassName);
	}

	@Test
	public void test4() {
		String productionClassName = NameTranslator
				.getProductionClassName("_START_");
		assertEquals("START", productionClassName);
	}

}
