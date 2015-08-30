package com.puresoltechnologies.parsers.source;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class URLSourceCodeLocationTest {

    @Test
    public void testJSONSerialization() throws JsonGenerationException, JsonMappingException, IOException {
	URLSourceCodeLocation fixedCodeLocation = new URLSourceCodeLocation(new URL("http://server/directory/file"));
	String serialized = JSONTestSerializer.serialize(fixedCodeLocation);
	URLSourceCodeLocation deserialized = JSONTestSerializer.deserialize(serialized, URLSourceCodeLocation.class);
	assertEquals(fixedCodeLocation, deserialized);
    }

}
