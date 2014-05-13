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

	public synchronized void addDefinition(TokenDefinition definition)
			throws GrammarException {
		if (definition == null) {
			return;
		}
		if (tokenDefinitions.contains(definition)) {
			throw new GrammarException("Double defined token definition '"
					+ definition + "'!");
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
		return name2Definition.get(name);
	}

	public int getID(String name) {
		return name2DefinitionID.get(name);
	}

	public String getName(int id) {
		return tokenDefinitions.get(id).getName();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((tokenDefinitions == null) ? 0 : tokenDefinitions.hashCode());
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
