package com.puresoltechnologies.parsers.grammar.token;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.puresoltechnologies.parsers.grammar.GrammarException;

/**
 * A token definitions set is meant to be the total collection of all token
 * definitions defined within a single grammar.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class TokenDefinitionSet implements Serializable {

    private static final long serialVersionUID = 8379268661591883917L;

    private final Map<String, Integer> name2DefinitionID = new HashMap<String, Integer>();
    private final Map<String, TokenDefinition> name2Definition = new HashMap<String, TokenDefinition>();
    private final List<TokenDefinition> tokenDefinitions = new ArrayList<TokenDefinition>();

    public synchronized void addDefinition(TokenDefinition definition) throws GrammarException {
	if (definition == null) {
	    return;
	}
	if (tokenDefinitions.contains(definition)) {
	    throw new GrammarException("Double defined token definition '" + definition + "'!");
	}
	String name = definition.getName();
	name2DefinitionID.put(name, tokenDefinitions.size());
	tokenDefinitions.add(definition);
	name2Definition.put(name, definition);
    }

    public List<TokenDefinition> getDefinitions() {
	return tokenDefinitions;
    }

    public TokenDefinition getDefinition(int id) {
	return tokenDefinitions.get(id);
    }

    public TokenDefinition getDefinition(String name) {
	TokenDefinition definition = name2Definition.get(name);
	if (definition == null) {
	    if (name2Definition.size() < tokenDefinitions.size()) {
		rebuildIndex();
		definition = name2Definition.get(name);
	    }
	}
	return definition;
    }

    public int getID(String name) {
	Integer id = name2DefinitionID.get(name);
	if (id == null) {
	    if (name2DefinitionID.size() < tokenDefinitions.size()) {
		rebuildIndex();
		id = name2DefinitionID.get(name);
	    }
	    if (id == null) {
		throw new IllegalStateException("Definition id was not found. This should not happen...");
	    }
	}
	return id;
    }

    private void rebuildIndex() {
	name2Definition.clear();
	name2DefinitionID.clear();
	for (int id = 0; id < tokenDefinitions.size(); ++id) {
	    TokenDefinition tokenDefinition = tokenDefinitions.get(id);
	    name2Definition.put(tokenDefinition.getName(), tokenDefinition);
	    name2DefinitionID.put(tokenDefinition.getName(), id);
	}
    }

    public String getName(int id) {
	return tokenDefinitions.get(id).getName();
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((tokenDefinitions == null) ? 0 : tokenDefinitions.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	TokenDefinitionSet other = (TokenDefinitionSet) obj;
	if (tokenDefinitions == null) {
	    if (other.tokenDefinitions != null)
		return false;
	} else if (!tokenDefinitions.equals(other.tokenDefinitions))
	    return false;
	return true;
    }

}
