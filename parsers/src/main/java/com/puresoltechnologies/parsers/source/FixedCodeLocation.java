package com.puresoltechnologies.parsers.source;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.puresoltechnologies.parsers.lexer.TokenStream;

/**
 * This source implementation is used for build-in sources like source
 * generators. This source is put into {@link TokenStream} and other objects
 * directly and it is expected, that load is not started anymore!
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class FixedCodeLocation extends AbstractSourceCodeLocation {

    private static final long serialVersionUID = -694681290095697777L;

    private final String lines[];

    public FixedCodeLocation(@JsonProperty("lines") String... lines) {
	this.lines = lines;
    }

    @Override
    public InputStream openStream() throws IOException {
	StringBuilder builder = new StringBuilder();
	for (SourceCodeLine line : createSourceCode().getLines()) {
	    builder.append(line.getLine());
	}
	return new ByteArrayInputStream(builder.toString().getBytes(
		Charset.defaultCharset()));
    }

    @Override
    @JsonIgnore
    public SourceCode getSourceCode() throws IOException {
	return createSourceCode();
    }

    private SourceCode createSourceCode() {
	SourceCode sourceCode = new SourceCode();
	int lineNum = 0;
	for (String line : lines) {
	    if ((line != null) && (!line.isEmpty())) {
		lineNum++;
		sourceCode.addSourceCodeLine(new SourceCodeLine(this, lineNum,
			line));
	    }
	}
	return sourceCode;
    }

    @Override
    @JsonIgnore
    public String getHumanReadableLocationString() {
	return "build-in";
    }

    public String[] getLines() {
	return lines;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((lines == null) ? 0 : lines.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	FixedCodeLocation other = (FixedCodeLocation) obj;
	if (lines == null) {
	    if (other.lines != null)
		return false;
	} else if (!lines.equals(other.lines))
	    return false;
	return true;
    }

    @Override
    public SourceCodeLocation newRelativeSource(String relativePath) {
	throw new IllegalStateException(
		"Cannot provide a new relative source to a built-in source!");
    }

    @Override
    @JsonIgnore
    public String getName() {
	return "built-in source";
    }

    @Override
    @JsonIgnore
    public String getInternalLocation() {
	return "";
    }

    @Override
    @JsonIgnore
    public boolean isAvailable() {
	return true;
    }

    @Override
    @JsonIgnore
    public Properties getSerialization() {
	throw new IllegalStateException("A fixed code cannot be serialized!");
    }
}
