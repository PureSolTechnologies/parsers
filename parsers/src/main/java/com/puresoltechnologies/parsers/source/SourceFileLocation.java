package com.puresoltechnologies.parsers.source;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SourceFileLocation extends AbstractSourceCodeLocation {

    private static final long serialVersionUID = -4803348905641081874L;

    private static final String SOURCE_CODE_LOCATION_REPOSITORY_DIRECTORY = "source.code.location.repository.directory";
    private static final String SOURCE_CODE_LOCATION_INTERNAL_PATH = "source.code.location.internal.path";

    private final File repositoryDirectory;
    private final String internalLocation;
    private final File file;

    public SourceFileLocation(String repositoryDirectory,
	    String internalLocation) {
	this(new File(repositoryDirectory), internalLocation);
    }

    public SourceFileLocation(
	    @JsonProperty("repositoryDirectory") File repositoryDirectory,
	    @JsonProperty("internalLocation") String internalLocation) {
	this.repositoryDirectory = repositoryDirectory;
	this.internalLocation = internalLocation;
	file = new File(repositoryDirectory, internalLocation);
    }

    public SourceFileLocation(File repositoryDirectory, File internalPath) {
	this(repositoryDirectory, internalPath.getPath());
    }

    public SourceFileLocation(Properties properties) {
	this.repositoryDirectory = new File(
		properties
			.getProperty(SOURCE_CODE_LOCATION_REPOSITORY_DIRECTORY));
	this.internalLocation = properties
		.getProperty(SOURCE_CODE_LOCATION_INTERNAL_PATH);
	if (internalLocation == null) {
	    throw new IllegalStateException("The property "
		    + SOURCE_CODE_LOCATION_INTERNAL_PATH + " was not set.");
	}
	file = new File(repositoryDirectory, this.internalLocation);
    }

    @Override
    public InputStream openStream() throws IOException {
	return new FileInputStream(file);
    }

    @Override
    @JsonIgnore
    public SourceCode getSourceCode() throws IOException {
	try (FileInputStream stream = new FileInputStream(file)) {
	    return SourceCode.read(stream, this);
	}
    }

    @Override
    @JsonIgnore
    public String getHumanReadableLocationString() {
	return internalLocation;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime
		* result
		+ ((internalLocation == null) ? 0 : internalLocation.hashCode());
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
	SourceFileLocation other = (SourceFileLocation) obj;
	if (internalLocation == null) {
	    if (other.internalLocation != null)
		return false;
	} else if (!internalLocation.equals(other.internalLocation))
	    return false;
	return true;
    }

    @Override
    public SourceCodeLocation newRelativeSource(String relativePath) {
	File internalFile = new File(internalLocation);
	File internalParentFile = internalFile.getParentFile();
	File newInternalPath;
	if (internalParentFile == null) {
	    newInternalPath = new File(relativePath);
	} else {
	    newInternalPath = new File(internalParentFile.getPath(),
		    relativePath);
	}
	return new SourceFileLocation(repositoryDirectory,
		newInternalPath.getPath());
    }

    @Override
    @JsonIgnore
    public String getName() {
	return file.getName();
    }

    public File getRepositoryDirectory() {
	return repositoryDirectory;
    }

    @Override
    public String getInternalLocation() {
	return internalLocation;
    }

    @Override
    @JsonIgnore
    public boolean isAvailable() {
	return file.exists();
    }

    @Override
    @JsonIgnore
    public Properties getSerialization() {
	Properties properties = new Properties();
	properties
		.setProperty(SOURCE_CODE_LOCATION_CLASS, getClass().getName());
	properties.setProperty(SOURCE_CODE_LOCATION_NAME, getName());
	properties.setProperty(SOURCE_CODE_LOCATION_REPOSITORY_DIRECTORY,
		repositoryDirectory.getPath());
	properties.setProperty(SOURCE_CODE_LOCATION_INTERNAL_PATH,
		internalLocation);
	return properties;
    }
}
