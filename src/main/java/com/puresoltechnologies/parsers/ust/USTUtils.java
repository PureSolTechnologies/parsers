package com.puresoltechnologies.parsers.ust;

import java.util.ArrayList;
import java.util.List;

/**
 * This class contains some helper methods to deal with
 * {@link UniversalSyntaxTree} trees.
 * 
 * @author Rick-Rainer Ludwig
 */
public class USTUtils {

	/**
	 * A private constructor to avoid instantiation.
	 */
	private USTUtils() {
	}

	/**
	 * This method searches a whole tree specified by a node for occurrences of
	 * specified sub trees. These sub trees can be tree leafs, too.
	 * 
	 * @param node
	 *            is the starting point for the search.
	 * @param name
	 *            is the name of the subtree to be searched.
	 * @return A {@link List} of {@link UniversalSyntaxTree} is returned
	 *         containing all sub tree which were be found.
	 */
	public static List<UniversalSyntaxTree> getSubTrees(
			UniversalSyntaxTree node, String name) {
		List<UniversalSyntaxTree> trees = new ArrayList<>();
		lookForSubTrees(trees, node, name);
		return trees;
	}

	private static void lookForSubTrees(List<UniversalSyntaxTree> trees,
			UniversalSyntaxTree node, String name) {
		if (node.getName().equals(name)) {
			trees.add(node);
		}
		for (UniversalSyntaxTree child : node.getChildren()) {
			lookForSubTrees(trees, child, name);
		}
	}
}
