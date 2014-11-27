package com.puresoltechnologies.parsers.preprocessor;

import com.puresoltechnologies.parsers.source.SourceCode;

/**
 * This is an interface for PreProcessor implementations. A PreProcessor is
 * meant to pre-process a source code to introduce more information or to
 * extract information which are not part of the source itself like control and
 * storage information.
 * 
 * A famous pre-processor is the C Preprocessor which is also implemented with
 * this interface.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public interface Preprocessor {

	/**
	 * This is the central method for any preprocessor. A source code object is
	 * provided which is processed by the preprocessor implementation. The
	 * return value is also a source code object. So several preprocessors can
	 * be stacked.
	 * 
	 * @param sourceCode
	 *            is the source code to be processed.
	 * @return A new {@link SourceCode} object is returned containing the
	 *         preprocessed source code.
	 * @throws PreprocessorException
	 *             is thrown if anything goes wrong during the process.
	 */
	public SourceCode process(SourceCode sourceCode)
			throws PreprocessorException;

}
