package com.puresoltechnologies.parsers.ust;

import java.util.ArrayList;
import java.util.List;

import com.puresoltechnologies.commons.trees.TreeException;

public abstract class AbstractUniversalSyntaxTreeNode implements
		UniversalSyntaxTree {

	private static final long serialVersionUID = -3031571790486296091L;

	private UniversalSyntaxTree parent = null;
	private final String name;
	private final UniversalSyntaxTreeMetaData metaData;
	private final String content;

	private final List<UniversalSyntaxTree> children = new ArrayList<>();

	public AbstractUniversalSyntaxTreeNode(String name, String content,
			UniversalSyntaxTreeMetaData metaData,
			List<UniversalSyntaxTree> children) {
		super();
		this.name = name;
		this.metaData = metaData;
		this.content = content;
		for (UniversalSyntaxTree child : children) {
			this.children.add(child);
			((AbstractUniversalSyntaxTreeNode) child).setParent(this);
		}
	}

	public AbstractUniversalSyntaxTreeNode(String name, String content,
			UniversalSyntaxTreeMetaData metaData) {
		super();
		this.name = name;
		this.metaData = metaData;
		this.content = content;
	}

	@Override
	public final UniversalSyntaxTree getParent() {
		return parent;
	}

	protected final void setParent(UniversalSyntaxTree parent) {
		this.parent = parent;
	}

	@Override
	public final String getName() {
		return name;
	}

	@Override
	public final UniversalSyntaxTreeMetaData getMetaData() {
		return metaData;
	}

	@Override
	public final String getContent() {
		return content;
	}

	@Override
	public final List<UniversalSyntaxTree> getChildren() {
		return children;
	}

	@Override
	public final boolean hasChildren() {
		return children.size() > 0;
	}

	@Override
	public final UniversalSyntaxTree getChild(String name) throws TreeException {
		UniversalSyntaxTree result = null;
		for (UniversalSyntaxTree child : children) {
			if (child.getName().equals(name)) {
				if (result != null) {
					throw new TreeException(
							"There were multiple children found for '" + name
									+ "' in '" + getName() + "'.");
				}
				result = child;
			}
		}
		return result;
	}

	@Override
	public final boolean hasChild(String name) {
		for (UniversalSyntaxTree child : children) {
			if (child.getName().equals(name)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public final List<UniversalSyntaxTree> getChildren(String name) {
		List<UniversalSyntaxTree> foundChildren = new ArrayList<>();
		for (UniversalSyntaxTree child : children) {
			if (child.getName().equals(name)) {
				foundChildren.add(child);
			}
		}
		return foundChildren;
	}

}
