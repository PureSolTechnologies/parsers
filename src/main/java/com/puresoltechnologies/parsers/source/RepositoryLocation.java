package com.puresoltechnologies.parsers.source;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import com.puresoltechnologies.commons.misc.FileSearchConfiguration;

/**
 * This is an interface for a repository source. The actual implementation may
 * be an access to a SCM system like GIT, Subversion or CVS or it might be a
 * directory which contains the checked out code. The actual implementation is
 * free to get a wide variety of repository sources connected.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public interface RepositoryLocation extends Serializable {

	public static final String REPOSITORY_LOCATION_CLASS = "repository.class";
	public static final String REPOSITORY_LOCATION_TYPE = "repository.type";
	public static final String REPOSITORY_LOCATION_NAME = "repository.name";

	/**
	 * This method returns the repository's name,
	 * 
	 * @return A {@link String} is returned containing the name of the
	 *         repository.
	 */
	public String getName();

	/**
	 * This method generates a human readable string where the source comes
	 * from. For {@link File} and {@link URL}, the normal toString()
	 * implementations might be sufficient. For {@link DatabaseCodeLocation} for
	 * example, the string might be something else. This String is generated
	 * here.
	 * 
	 * @return A {@link String} is returned telling the original location in
	 *         human readable way.
	 */
	public String getHumanReadableLocationString();

	/**
	 * This method returns a list of {@link SourceCodeLocation} from all source
	 * codes which are available within the specified repository.
	 * 
	 * @param fileSearchConfiguration
	 *            is the {@link FileSearchConfiguration} object specifying what
	 *            files to take into account.
	 * @return A {@link List} of {@link SourceCodeLocation} is returned
	 *         containing all locations.
	 */
	public List<SourceCodeLocation> getSourceCodes(
			FileSearchConfiguration fileSearchConfiguration);

	/**
	 * This method creates a representation of the repository location as
	 * {@link Properties} object.
	 * 
	 * @return A {@link Properties} object is returned which represents this
	 *         location.
	 */
	public Properties getSerialization();
}
