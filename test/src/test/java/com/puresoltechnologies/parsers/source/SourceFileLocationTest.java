package com.puresoltechnologies.parsers.source;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SourceFileLocationTest {

    @Test
    public void testJSONSerialization() throws JsonGenerationException, JsonMappingException, IOException {
	SourceFileLocation fixedCodeLocation = new SourceFileLocation(new File("/path/to/repository"),
		new File("internal/path/to/file"));
	String serialized = JSONTestSerializer.serialize(fixedCodeLocation);
	SourceFileLocation deserialized = JSONTestSerializer.deserialize(serialized, SourceFileLocation.class);
	assertEquals(fixedCodeLocation, deserialized);
    }

}
