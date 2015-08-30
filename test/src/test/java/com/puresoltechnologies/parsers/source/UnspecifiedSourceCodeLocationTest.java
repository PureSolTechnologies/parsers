package com.puresoltechnologies.parsers.source;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class UnspecifiedSourceCodeLocationTest {

    @Test
    public void testJSONSerialization() throws JsonGenerationException, JsonMappingException, IOException {
	UnspecifiedSourceCodeLocation fixedCodeLocation = new UnspecifiedSourceCodeLocation();
	String serialized = JSONTestSerializer.serialize(fixedCodeLocation);
	UnspecifiedSourceCodeLocation deserialized = JSONTestSerializer.deserialize(serialized,
		UnspecifiedSourceCodeLocation.class);
	assertEquals(fixedCodeLocation, deserialized);
    }

}
