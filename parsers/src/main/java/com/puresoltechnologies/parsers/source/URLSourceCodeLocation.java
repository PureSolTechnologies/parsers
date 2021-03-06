package com.puresoltechnologies.parsers.source;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class URLSourceCodeLocation extends AbstractSourceCodeLocation {

    private static final long serialVersionUID = 7938452623238399125L;

    private static final String SOURCE_CODE_LOCATION_URL = "source.code.location.url";

    private final URL url;

    public URLSourceCodeLocation(@JsonProperty("url") URL url) {
	this.url = url;
    }

    public URLSourceCodeLocation(Properties properties) {
	try {
	    this.url = new URL(properties.getProperty(SOURCE_CODE_LOCATION_URL));
	} catch (MalformedURLException e) {
	    throw new IllegalStateException("Could not de-serialize source code location.", e);
	}
    }

    @Override
    public InputStream openStream() throws IOException {
	return url.openStream();
    }

    @Override
    @JsonIgnore
    public SourceCode getSourceCode() throws IOException {
	try (InputStream stream = url.openStream()) {
	    return SourceCode.read(stream, this);
	}
    }

    @Override
    @JsonIgnore
    public String getHumanReadableLocationString() {
	return url.toString();
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((url == null) ? 0 : url.toExternalForm().hashCode());
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
	URLSourceCodeLocation other = (URLSourceCodeLocation) obj;
	if (url == null) {
	    if (other.url != null)
		return false;
	} else if (!url.toExternalForm().equals(other.url.toExternalForm()))
	    return false;
	return true;
    }

    @Override
    public SourceCodeLocation newRelativeSource(String relativePath) {
	throw new IllegalStateException("This functionality is not implemented, yet!");
    }

    public URL getUrl() {
	return url;
    }

    @Override
    @JsonIgnore
    public String getName() {
	return new File(url.getPath()).getName();
    }

    @Override
    @JsonIgnore
    public String getInternalLocation() {
	return new File(url.getPath()).getParent();
    }

    @Override
    @JsonIgnore
    public boolean isAvailable() {
	try (InputStream stream = url.openStream()) {
	    return stream != null;
	} catch (IOException e) {
	    return false;
	}
    }

    @Override
    @JsonIgnore
    public Properties getSerialization() {
	Properties properties = new Properties();
	properties.setProperty(SOURCE_CODE_LOCATION_CLASS, getClass().getName());
	properties.setProperty(SOURCE_CODE_LOCATION_NAME, getName());
	properties.setProperty(SOURCE_CODE_LOCATION_URL, url.toExternalForm());
	return properties;
    }
}
