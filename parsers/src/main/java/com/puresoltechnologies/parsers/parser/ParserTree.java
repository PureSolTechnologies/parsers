package com.puresoltechnologies.parsers.parser;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.puresoltechnologies.parsers.grammar.production.Production;
import com.puresoltechnologies.parsers.lexer.Token;
import com.puresoltechnologies.trees.Tree;
import com.puresoltechnologies.trees.TreeException;
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
public class ParserTree implements Tree<ParserTree>, Serializable, Cloneable {

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
    private ParserTree parent = null;
    /**
     * This field contains all references to the node's children.
     */
    private final ArrayList<ParserTree> children = new ArrayList<ParserTree>();
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

    public ParserTree(Token token) {
	super();
	this.name = token.getName();
	this.token = token;
	this.node = true;
	this.stackingAllowed = true;
    }

    public ParserTree(Production production) {
	this.name = production.getAlternativeName();
	this.token = null;
	this.node = production.isNode();
	this.stackingAllowed = production.isStackingAllowed();
    }

    public ParserTree(String name) {
	this.name = name;
	this.token = null;
	this.node = true;
	this.stackingAllowed = true;
    }

    public ParserTree(String name, Token token, boolean node,
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
    public void setParent(ParserTree parent) {
	this.parent = parent;
    }

    /**
     * @return the parent
     */
    @Override
    public ParserTree getParent() {
	return parent;
    }

    public ParserTree getRoot() {
	ParserTree root = this;
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
    public List<ParserTree> getChildren() {
	return children;
    }

    public void addChild(ParserTree child) throws TreeException {
	children.add(child);
	child.setParent(this);
    }

    public void addChildren(List<ParserTree> children) throws TreeException {
	this.children.addAll(children);
	for (ParserTree child : children) {
	    child.setParent(this);
	}
    }

    public void addChildInFront(ParserTree child) throws TreeException {
	children.add(0, child);
	child.setParent(this);
    }

    public void addChildrenInFront(List<ParserTree> children)
	    throws TreeException {
	this.children.addAll(0, children);
	for (ParserTree child : children) {
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
    public ParserTree getChild(String name) throws TreeException {
	ParserTree result = null;
	for (ParserTree child : children) {
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
    public List<ParserTree> getChildren(String name) throws TreeException {
	List<ParserTree> result = new ArrayList<ParserTree>();
	for (ParserTree child : children) {
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
	for (ParserTree child : children) {
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

    public List<ParserTree> getSubTrees(String name) {
	List<ParserTree> subTrees = new ArrayList<ParserTree>();
	getSubTrees(this, subTrees, name);
	return subTrees;
    }

    private void getSubTrees(ParserTree branch, List<ParserTree> subTrees,
	    String name) {
	if (branch.getName().equals(name)) {
	    subTrees.add(branch);
	}
	for (ParserTree subBranch : branch.getChildren()) {
	    getSubTrees(subBranch, subTrees, name);
	}
    }

    private static class TextWalkerClient implements TreeVisitor<ParserTree> {

	private final StringBuffer text = new StringBuffer();

	@Override
	public WalkingAction visit(ParserTree syntaxTree) {
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
	TreeWalker<ParserTree> treeWalker = new TreeWalker<ParserTree>(this);
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

    private void fillBuffer(StringBuffer buffer, ParserTree parserTree,
	    int depth) {
	for (int i = 0; i < depth; i++) {
	    buffer.append("  ");
	}
	buffer.append(parserTree.getName() + ": \"" + parserTree.getText()
		+ "\"\n");
	for (ParserTree child : parserTree.getChildren()) {
	    fillBuffer(buffer, child, depth + 1);
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public ParserTree clone() {
	ParserTree cloned;
	if (token != null)
	    cloned = new ParserTree(name, token.clone(), node, stackingAllowed);
	else
	    cloned = new ParserTree(name, null, node, stackingAllowed);
	cloned.parent = parent;
	if (metaData != null)
	    cloned.metaData = metaData.clone();
	cloned.children.addAll((ArrayList<ParserTree>) children.clone());
	return cloned;
    }
}
