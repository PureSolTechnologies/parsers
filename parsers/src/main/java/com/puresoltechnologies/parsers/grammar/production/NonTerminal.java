package com.puresoltechnologies.parsers.grammar.production;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class NonTerminal extends AbstractConstruction {

    private static final long serialVersionUID = 8346248512269952988L;

    @JsonCreator
    public NonTerminal(@JsonProperty("name") String name) {
	super(name, false);
    }

}
