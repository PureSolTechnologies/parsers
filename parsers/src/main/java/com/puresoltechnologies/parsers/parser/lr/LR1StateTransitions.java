package com.puresoltechnologies.parsers.parser.lr;

import com.puresoltechnologies.parsers.grammar.GrammarException;
import com.puresoltechnologies.parsers.grammar.production.Construction;
import com.puresoltechnologies.parsers.parser.functions.Goto1;
import com.puresoltechnologies.parsers.parser.items.LR1ItemSet;
import com.puresoltechnologies.parsers.parser.parsetable.StateTransitions;

public class LR1StateTransitions extends StateTransitions {

    private static final long serialVersionUID = -5970793225278358360L;

    private final LR1ItemSetCollection itemSetCollection;
    private final Goto1 goto1;

    public LR1StateTransitions(LR1ItemSetCollection itemSetCollection, Goto1 goto1) throws GrammarException {
	super();
	this.itemSetCollection = itemSetCollection;
	this.goto1 = goto1;
	calculate();
    }

    private void calculate() throws GrammarException {
	for (int stateId = 0; stateId < itemSetCollection.getStateNumber(); stateId++) {
	    LR1ItemSet lr1ItemSet = itemSetCollection.getItemSet(stateId);
	    for (Construction next : lr1ItemSet.getNextConstructions()) {
		LR1ItemSet gotoSet = goto1.calc(lr1ItemSet, next);
		int targetState = itemSetCollection.getStateId(gotoSet);
		addTransition(stateId, next, targetState);
	    }
	}
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = super.hashCode();
	result = prime * result + ((goto1 == null) ? 0 : goto1.hashCode());
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
	LR1StateTransitions other = (LR1StateTransitions) obj;
	if (goto1 == null) {
	    if (other.goto1 != null)
		return false;
	} else if (!goto1.equals(other.goto1))
	    return false;
	if (itemSetCollection == null) {
	    if (other.itemSetCollection != null)
		return false;
	} else if (!itemSetCollection.equals(other.itemSetCollection))
	    return false;
	return true;
    }

}
