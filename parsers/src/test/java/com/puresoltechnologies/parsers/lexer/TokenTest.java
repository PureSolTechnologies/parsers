package com.puresoltechnologies.parsers.lexer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.Test;

import com.puresoltechnologies.parsers.grammar.token.Visibility;
import com.puresoltechnologies.parsers.lexer.Token;
import com.puresoltechnologies.parsers.lexer.TokenMetaData;
import com.puresoltechnologies.parsers.source.UnspecifiedSourceCodeLocation;

public class TokenTest {

	@Test
	public void testInstance() {
		assertNotNull(new Token("Name", "Text", Visibility.VISIBLE,
				new TokenMetaData(new UnspecifiedSourceCodeLocation(), 1, 2, 0)));
	}

	@Test
	public void testInitialValues() {
		TokenMetaData metaData = new TokenMetaData(
				new UnspecifiedSourceCodeLocation(), 1, 2, 0);
		Token token = new Token("Name", "Text", Visibility.VISIBLE, metaData);
		assertEquals("Name", token.getName());
		assertEquals("Text", token.getText());
		assertEquals(Visibility.VISIBLE, token.getVisibility());
		assertSame(metaData, token.getMetaData());
	}

	@Test
	public void testClone() {
		Token token = new Token("Name", "Text", Visibility.VISIBLE,
				new TokenMetaData(new UnspecifiedSourceCodeLocation(), 1, 2, 0));
		Token cloned = token.clone();
		assertNotSame(token, cloned);
		assertEquals(token, cloned);
	}

}
