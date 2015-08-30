package com.puresoltechnologies.parsers.source;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class FixedCodeLocationTest {

    @Test
    public void testJSONSerialization() throws JsonGenerationException, JsonMappingException, IOException {
	FixedCodeLocation fixedCodeLocation = new FixedCodeLocation("Line1;");
	String serialized = JSONTestSerializer.serialize(fixedCodeLocation);
	System.out.println(serialized);
	FixedCodeLocation deserialized = JSONTestSerializer.deserialize(serialized, FixedCodeLocation.class);
	assertEquals(fixedCodeLocation, deserialized);
    }

}
