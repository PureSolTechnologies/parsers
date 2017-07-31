package com.puresoltechnologies.parsers.parser.lr;

import com.puresoltechnologies.parsers.grammar.GrammarException;
import com.puresoltechnologies.parsers.grammar.production.Construction;
import com.puresoltechnologies.parsers.parser.functions.Goto0;
import com.puresoltechnologies.parsers.parser.items.LR0ItemSet;
import com.puresoltechnologies.parsers.parser.parsetable.StateTransitions;

public class LR0StateTransitions extends StateTransitions {

    private static final long serialVersionUID = -5970793225278358360L;

    private final LR0ItemSetCollection itemSetCollection;
    private final Goto0 goto0;

    public LR0StateTransitions(LR0ItemSetCollection itemSetCollection, Goto0 goto0) throws GrammarException {
	super();
	this.itemSetCollection = itemSetCollection;
	this.goto0 = goto0;
	calculate();
    }

    private void calculate() throws GrammarException {
	for (int stateId = 0; stateId < itemSetCollection.getStateNumber(); stateId++) {
	    LR0ItemSet lr0ItemSet = itemSetCollection.getItemSet(stateId);
	    for (Construction next : lr0ItemSet.getNextConstructions()) {
		LR0ItemSet gotoSet = goto0.calc(lr0ItemSet, next);
		int targetState = itemSetCollection.getStateId(gotoSet);
		addTransition(stateId, next, targetState);
	    }
	}
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((goto0 == null) ? 0 : goto0.hashCode());
	result = prime * result + ((itemSetCollection == null) ? 0 : itemSetCollection.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (!super.equals(obj))
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	LR0StateTransitions other = (LR0StateTransitions) obj;
	if (goto0 == null) {
	    if (other.goto0 != null)
		return false;
	} else if (!goto0.equals(other.goto0))
	    return false;
	if (itemSetCollection == null) {
	    if (other.itemSetCollection != null)
		return false;
	} else if (!itemSetCollection.equals(other.itemSetCollection))
	    return false;
	return true;
    }

}
