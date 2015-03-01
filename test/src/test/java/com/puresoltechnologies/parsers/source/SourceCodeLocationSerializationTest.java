package com.puresoltechnologies.parsers.source;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class SourceCodeLocationSerializationTest {

    @Test
    public void testFixedCodeLocation() throws JsonGenerationException,
	    JsonMappingException, IOException {
	SourceCodeLocation location = new FixedCodeLocation("Line 1");
	String jsonString = JSONTestSerializer.serialize(location);
	assertNotNull(jsonString);
	assertFalse(jsonString.isEmpty());
	SourceCodeLocation deserialized = JSONTestSerializer.deserialize(
		jsonString, SourceCodeLocation.class);
	assertNotNull(deserialized);
    }

    @Test
    public void testUnspecifiedSourceCodeLocation()
	    throws JsonGenerationException, JsonMappingException, IOException {
	SourceCodeLocation location = new UnspecifiedSourceCodeLocation();
	String jsonString = JSONTestSerializer.serialize(location);
	assertNotNull(jsonString);
	assertFalse(jsonString.isEmpty());
	SourceCodeLocation deserialized = JSONTestSerializer.deserialize(
		jsonString, SourceCodeLocation.class);
	assertNotNull(deserialized);
    }

    @Test
    public void testSourceFileLocation() throws JsonGenerationException,
	    JsonMappingException, IOException {
	SourceCodeLocation location = new SourceFileLocation(new File(
		"repository"), "internal");
	String jsonString = JSONTestSerializer.serialize(location);
	assertNotNull(jsonString);
	assertFalse(jsonString.isEmpty());
	System.out.println(jsonString);
	SourceCodeLocation deserialized = JSONTestSerializer.deserialize(
		jsonString, SourceCodeLocation.class);
	assertNotNull(deserialized);
    }

    @Test
    public void testURLSourceCodeLocation() throws JsonGenerationException,
	    JsonMappingException, IOException {
	SourceCodeLocation location = new URLSourceCodeLocation(new URL(
		"file:///home/ludwig/some.file"));
	String jsonString = JSONTestSerializer.serialize(location);
	assertNotNull(jsonString);
	assertFalse(jsonString.isEmpty());
	System.out.println(jsonString);
	SourceCodeLocation deserialized = JSONTestSerializer.deserialize(
		jsonString, SourceCodeLocation.class);
	assertNotNull(deserialized);
    }

}
