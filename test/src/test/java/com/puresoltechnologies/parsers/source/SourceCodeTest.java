package com.puresoltechnologies.parsers.source;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;

public class SourceCodeTest {

	@Test
	public void testSerialization() throws JsonGenerationException,
			JsonMappingException, IOException {
		SourceCode sourceCode = SourceCode.fromStringArray("Line1", "Line2");
		String serialized = JSONTestSerializer.serialize(sourceCode);
		assertNotNull(serialized);
		assertFalse(serialized.isEmpty());
		System.out.println(serialized);
		SourceCode deserialized = JSONTestSerializer.deserialize(serialized,
				SourceCode.class);
		assertNotNull(deserialized);
	}

}
