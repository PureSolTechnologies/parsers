package com.puresoltechnologies.parsers.parser.packrat;

/**
 * This class is used to put a status information into the memoizer of a packrat
 * parser. This status is associated with a position and a production.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
enum Status {

	/**
	 * Means processing was successfully done.
	 */
	SUCCEEDED,

	/**
	 * A process already failed to parse at this position the production
	 * associated.
	 */
	FAILED;

	@Override
	public String toString() {
		return name().toLowerCase();
	}
}
