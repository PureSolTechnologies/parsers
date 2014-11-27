package com.puresoltechnologies.parsers.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.puresoltechnologies.parsers.source.SourceFileLocation;
import com.puresoltechnologies.parsers.source.UnspecifiedSourceCodeLocation;

public class ParserTreeMetaDataTest {

	@Test
	public void testInstance() {
		assertNotNull(new ParserTreeMetaData(
				new UnspecifiedSourceCodeLocation(), 1, 2));
	}

	@Test
	public void testInitValues() {
		ParserTreeMetaData metaData = new ParserTreeMetaData(
				new UnspecifiedSourceCodeLocation(), 1, 2);
		assertEquals(
				new UnspecifiedSourceCodeLocation()
						.getHumanReadableLocationString(),
				metaData.getSource().getHumanReadableLocationString());
		assertEquals(1, metaData.getLine());
		assertEquals(2, metaData.getLineNum());
	}

	@Test
	public void testEquals() {
		assertEquals(new ParserTreeMetaData(new SourceFileLocation("", "Test"),
				1, 1), new ParserTreeMetaData(
				new SourceFileLocation("", "Test"), 1, 1));
	}

	@Test
	public void testToString() {
		ParserTreeMetaData metaData = new ParserTreeMetaData(
				new UnspecifiedSourceCodeLocation(), 1, 1);
		assertEquals("unspecified source: 1", metaData.toString());

		metaData = new ParserTreeMetaData(new UnspecifiedSourceCodeLocation(),
				1, 2);
		assertEquals("unspecified source: 1 - 2", metaData.toString());
	}
}
