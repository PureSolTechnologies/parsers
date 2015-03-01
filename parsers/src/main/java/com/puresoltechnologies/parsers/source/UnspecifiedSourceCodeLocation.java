package com.puresoltechnologies.parsers.source;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UnspecifiedSourceCodeLocation extends AbstractSourceCodeLocation {

    private static final long serialVersionUID = 5446070531559019716L;

    public UnspecifiedSourceCodeLocation() {
	super();
    }

    public UnspecifiedSourceCodeLocation(Properties properties) {
	super();
    }

    @Override
    @JsonIgnore
    public String getHumanReadableLocationString() {
	return "unspecified source";
    }

    @Override
    public InputStream openStream() throws IOException {
	throw new IOException(
		"This is an unspecified source. There is not source information available where the source can be loaded from.");
    }

    @Override
    @JsonIgnore
    public SourceCode getSourceCode() throws IOException {
	throw new IOException(
		"This is an unspecified source. There is not source information available where the source can be loaded from.");
    }

    @Override
    public boolean equals(Object obj) {
	return this == obj;
    }

    @Override
    public int hashCode() {
	return 42;
    }

    @Override
    public SourceCodeLocation newRelativeSource(String relativePath) {
	throw new IllegalStateException(
		"Cannot provide a new relative source to an unspecified source!");
    }

    @Override
    @JsonIgnore
    public String getName() {
	return "unspecified source";
    }

    @Override
    @JsonIgnore
    public String getInternalLocation() {
	return "";
    }

    @Override
    @JsonIgnore
    public boolean isAvailable() {
	return false;
    }

    @Override
    @JsonIgnore
    public Properties getSerialization() {
	Properties properties = new Properties();
	properties
		.setProperty(SOURCE_CODE_LOCATION_CLASS, getClass().getName());
	properties.setProperty(SOURCE_CODE_LOCATION_NAME, getName());
	return properties;
    }
}
