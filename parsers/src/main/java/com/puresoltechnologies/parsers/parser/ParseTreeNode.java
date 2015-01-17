package com.puresoltechnologies.parsers.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.puresoltechnologies.parsers.grammar.production.Production;
import com.puresoltechnologies.parsers.lexer.Token;
import com.puresoltechnologies.trees.Tree;
import com.puresoltechnologies.trees.TreeException;
import com.puresoltechnologies.trees.TreeLink;
import com.puresoltechnologies.trees.TreeNode;
import com.puresoltechnologies.trees.TreeVisitor;
import com.puresoltechnologies.trees.TreeWalker;
import com.puresoltechnologies.trees.WalkingAction;

/**
 * This tree is a parser tree as it is the result of a parser run. This class
 * implements the {@link Tree} interface is therefore suitable for the
 * TreeWalker class for easy tree processing.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class ParseTreeNode implements TreeNode<ParseTreeNode>, Serializable,
	Cloneable {

    private static final long serialVersionUID = -651453440127029204L;

    /**
     * This is the name of the token.
     */
    private final String name;
    /**
     * Is the token which is assigned to this node.
     */
    private final Token token;
    /**
     * Contains the reference to the parent node if present.
     */
    private ParseTreeNode parent = null;
    /**
     * This field contains all references to the node's children.
     */
    private final ArrayList<ParseTreeNode> children = new ArrayList<ParseTreeNode>();
    /**
     * This flag specifies whether the node is allowed to be a node or not. If
     * not, the children will be assigned to its parent on the location of
     * itself and the node is deleted during tree normalization.
     */
    private final boolean node;
    /**
     * This flag specified whether a stacking of this node type is allowed or
     * not. If stacking is allowed, than nodes of this type can contain each
     * other. Otherwise the nodes would be reduced to one occurrence and all
     * children would be assigned to one single node during tree normalization.
     */
    private final boolean stackingAllowed;
    /**
     * This field contains the meta information of the node.
     */
    private ParserTreeMetaData metaData = null;

    public ParseTreeNode(Token token) {
	super();
	this.name = token.getName();
	this.token = token;
	this.node = true;
	this.stackingAllowed = true;
    }

    public ParseTreeNode(Production production) {
	this.name = production.getAlternativeName();
	this.token = null;
	this.node = production.isNode();
	this.stackingAllowed = production.isStackingAllowed();
    }

    public ParseTreeNode(String name) {
	this.name = name;
	this.token = null;
	this.node = true;
	this.stackingAllowed = true;
    }

    public ParseTreeNode(String name, Token token, boolean node,
	    boolean stackingAllowed) {
	this.name = name;
	this.token = token;
	this.node = node;
	this.stackingAllowed = stackingAllowed;
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
	return name;
    }

    /**
     * @return the text
     */
    public Token getToken() {
	return token;
    }

    /**
     * @param parent
     *            the parent to set
     */
    public void setParent(ParseTreeNode parent) {
	this.parent = parent;
    }

    /**
     * @return the parent
     */
    @Override
    public ParseTreeNode getParent() {
	return parent;
    }

    public ParseTreeNode getRoot() {
	ParseTreeNode root = this;
	while (root.getParent() != null) {
	    root = root.getParent();
	}
	return root;
    }

    @Override
    public boolean hasChildren() {
	return !children.isEmpty();
    }

    /**
     * @return the children
     */
    @Override
    public List<ParseTreeNode> getChildren() {
	return children;
    }

    @Override
    public Set<TreeLink<ParseTreeNode>> getEdges() {
	Set<TreeLink<ParseTreeNode>> edges = new HashSet<>();
	edges.add(new TreeLink<>(parent, this));
	for (ParseTreeNode child : children) {
	    edges.add(new TreeLink<ParseTreeNode>(this, child));
	}
	return edges;
    }

    public void addChild(ParseTreeNode child) throws TreeException {
	children.add(child);
	child.setParent(this);
    }

    public void addChildren(List<ParseTreeNode> children) throws TreeException {
	this.children.addAll(children);
	for (ParseTreeNode child : children) {
	    child.setParent(this);
	}
    }

    public void addChildInFront(ParseTreeNode child) throws TreeException {
	children.add(0, child);
	child.setParent(this);
    }

    public void addChildrenInFront(List<ParseTreeNode> children)
	    throws TreeException {
	this.children.addAll(0, children);
	for (ParseTreeNode child : children) {
	    child.setParent(this);
	}
    }

    /**
     * This method returns the child with the given name from this node.
     * 
     * It is used to extract exactly one child from this node. To get more
     * children, use {@link #getChildren()} for that purpose.
     * 
     * @param name
     *            is the name of the child.
     * @return The child is returned if found. Otherwise null is returned.
     * @throws TreeException
     *             is thrown if there is more than one child with the given
     *             name.
     */
    public ParseTreeNode getChild(String name) throws TreeException {
	ParseTreeNode result = null;
	for (ParseTreeNode child : children) {
	    if (child.getName().equals(name)) {
		if (result != null) {
		    throw new TreeException("Child '" + name
			    + "'is multiply defined!");
		}
		result = child;
	    }
	}
	return result;
    }

    /**
     * @param name
     *            is the name of the children.
     * @return the children
     * @throws TreeException
     *             is thrown in cases of tree issues.
     */
    public List<ParseTreeNode> getChildren(String name) throws TreeException {
	List<ParseTreeNode> result = new ArrayList<ParseTreeNode>();
	for (ParseTreeNode child : children) {
	    if (child.getName().equals(name)) {
		result.add(child);
	    }
	}
	return result;
    }

    /**
     * This method look for a child with a given name.
     * 
     * @param name
     *            is the name to look for.
     * @return true is returned in case a child with the specified name exists.
     */
    public boolean hasChild(String name) {
	for (ParseTreeNode child : children) {
	    if (child.getName().equals(name)) {
		return true;
	    }
	}
	return false;
    }

    /**
     * Returns whether this node is allowed to stay as node during tree
     * normalization or not. Have a look to {@link #node} for more details on
     * this subject.
     * 
     * @return A boolean is returned.
     */
    public boolean isNode() {
	return node;
    }

    /**
     * This method returns whether this node is allowed to be stacked or not.
     * See {@link #stackingAllowed} for details.
     * 
     * @return A boolean for stackingAllowed is returned.
     */
    public boolean isStackingAllowed() {
	return stackingAllowed;
    }

    public List<ParseTreeNode> getSubTrees(String name) {
	List<ParseTreeNode> subTrees = new ArrayList<ParseTreeNode>();
	getSubTrees(this, subTrees, name);
	return subTrees;
    }

    private void getSubTrees(ParseTreeNode branch, List<ParseTreeNode> subTrees,
	    String name) {
	if (branch.getName().equals(name)) {
	    subTrees.add(branch);
	}
	for (ParseTreeNode subBranch : branch.getChildren()) {
	    getSubTrees(subBranch, subTrees, name);
	}
    }

    private static class TextWalkerClient implements TreeVisitor<ParseTreeNode> {

	private final StringBuffer text = new StringBuffer();

	@Override
	public WalkingAction visit(ParseTreeNode syntaxTree) {
	    Token token = syntaxTree.getToken();
	    if (token != null) {
		text.append(token.getText());
	    }
	    return null;
	}

	public String getText() {
	    return text.toString();
	}

    }

    public String getText() {
	TreeWalker<ParseTreeNode> treeWalker = new TreeWalker<ParseTreeNode>(this);
	TextWalkerClient textClient = new TextWalkerClient();
	treeWalker.walk(textClient);
	return textClient.getText();
    }

    public ParserTreeMetaData getMetaData() {
	return metaData;
    }

    public void setMetaData(ParserTreeMetaData metaData) {
	this.metaData = metaData;
    }

    @Override
    public String toString() {
	return getName() + " \"" + getText() + "\"";
    }

    public String toTreeString() {
	StringBuffer buffer = new StringBuffer();
	fillBuffer(buffer, this, 0);
	return buffer.toString();
    }

    private void fillBuffer(StringBuffer buffer, ParseTreeNode parserTree,
	    int depth) {
	for (int i = 0; i < depth; i++) {
	    buffer.append("  ");
	}
	buffer.append(parserTree.getName() + ": \"" + parserTree.getText()
		+ "\"\n");
	for (ParseTreeNode child : parserTree.getChildren()) {
	    fillBuffer(buffer, child, depth + 1);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public ParseTreeNode clone() {
	ParseTreeNode cloned;
	if (token != null)
	    cloned = new ParseTreeNode(name, token.clone(), node, stackingAllowed);
	else
	    cloned = new ParseTreeNode(name, null, node, stackingAllowed);
	cloned.parent = parent;
	if (metaData != null)
	    cloned.metaData = metaData.clone();
	cloned.children.addAll((ArrayList<ParseTreeNode>) children.clone());
	return cloned;
    }
}
