package com.puresoltechnologies.parsers.parser.items;

import java.util.Set;

public class LR1ItemSet extends AbstractItemSet<LR1Item> {

	private static final long serialVersionUID = -4166037691502608458L;

	public LR1ItemSet(LR1Item primaryItem) {
		super(primaryItem);
	}

	public LR1ItemSet(Set<LR1Item> primaryItems) {
		super(primaryItems);
	}

	public LR1ItemSet(LR1ItemSet initialItemSet) {
		super(initialItemSet);
	}

}
