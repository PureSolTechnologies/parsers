package com.puresoltechnologies.parsers.lexer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import org.junit.Test;

import com.puresoltechnologies.parsers.lexer.TokenMetaData;
import com.puresoltechnologies.parsers.source.UnspecifiedSourceCodeLocation;

public class TokenMetaDataTest {

	@Test
	public void testInstance() {
		assertNotNull(new TokenMetaData(new UnspecifiedSourceCodeLocation(), 1,
				2, 0));
	}

	@Test
	public void testInitValues() {
		TokenMetaData metaData = new TokenMetaData(
				new UnspecifiedSourceCodeLocation(), 1, 2, 3);
		assertEquals(
				new UnspecifiedSourceCodeLocation()
						.getHumanReadableLocationString(),
				metaData.getSource().getHumanReadableLocationString());
		assertEquals(1, metaData.getLine());
		assertEquals(2, metaData.getLineNum());
		assertEquals(3, metaData.getColumn());
	}

	@Test
	public void testClone() {
		TokenMetaData metaData = new TokenMetaData(
				new UnspecifiedSourceCodeLocation(), 1, 2, 3);
		TokenMetaData cloned = metaData.clone();
		assertNotSame(metaData, cloned);
		assertEquals(metaData, cloned);
		assertEquals(3, metaData.getColumn());
	}
}
