package com.puresoltechnologies.parsers.ust;

import java.io.Serializable;
import java.util.List;

import com.puresoltechnologies.commons.trees.Tree;
import com.puresoltechnologies.commons.trees.TreeException;

/**
 * This is the central interface for universal syntax trees.
 * <p>
 * The condition for the syntax tree:
 * <ol>
 * <li>The UST must be immutable.</li>
 * <li>The UST may contain special classes implementing this interface which
 * contain meta information about the special node.</li>
 * </ol>
 * </p>
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public interface UniversalSyntaxTree extends Tree<UniversalSyntaxTree>,
		Serializable {

	/**
	 * This method returns the meta data for the node.
	 * 
	 * @return A {@link UniversalSyntaxTreeMetaData} object is returned.
	 */
	public UniversalSyntaxTreeMetaData getMetaData();

	/**
	 * This method returns the content of the node.
	 * 
	 * @return A {@link String} with the content is returned.
	 */
	public String getContent();

	/**
	 * This method returns the child with the given name.
	 * 
	 * @param name
	 *            is the name of the child which is to be returned.
	 * @return A {@link UniversalSyntaxTree} object is returned referencing the
	 *         child. <code>null</code> is returned in case of the child is not
	 *         found.
	 * @throws An
	 *             {@link IllegalStateException} is thrown if multiple children
	 *             are found.
	 */
	public UniversalSyntaxTree getChild(String name) throws TreeException;

	/**
	 * This method checks for the presence of a specified child.
	 * 
	 * @param name
	 *            is the name of the child to be looked for.
	 * @return <code>true</code> is returned if at least one child with this
	 *         name exists. <code>false</code> is returned otherwise.
	 */
	public boolean hasChild(String name);

	/**
	 * This method returns a list of all children of the node which have the
	 * specified name.
	 * 
	 * @param name
	 *            is the name of the children to be looked up.
	 * @return A {@link List} of {@link UniversalSyntaxTree} is returned
	 *         containing all children found.
	 */
	public List<UniversalSyntaxTree> getChildren(String name);

}
