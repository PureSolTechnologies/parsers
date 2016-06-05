package com.puresoltechnologies.parsers.grammar.production;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.puresoltechnologies.parsers.grammar.GrammarException;

/**
 * This class contains all parser rules which are to be set for a parser.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class ProductionSet implements Serializable {

    private static final long serialVersionUID = 1122545599969259364L;

    private final Map<String, List<Production>> name2Production = new HashMap<String, List<Production>>();
    private final List<Production> productions = new ArrayList<Production>();

    public void add(Production production) throws GrammarException {
	if (production == null) {
	    return;
	}
	if (productions.contains(production)) {
	    throw new GrammarException("Double defined production '" + production + "'!");
	}
	production.setId(productions.size());
	productions.add(production);
	List<Production> values = name2Production.get(production.getName());
	if (values == null) {
	    values = new ArrayList<Production>();
	    name2Production.put(production.getName(), values);
	}
	values.add(production);
    }

    public void add(List<Production> productions) throws GrammarException {
	for (Production production : productions) {
	    add(production);
	}
    }

    public List<Production> getList() {
	return productions;
    }

    public Production get(int id) {
	return productions.get(id);
    }

    public List<Production> get(String productionName) {
	List<Production> list = name2Production.get(productionName);
	if (list == null) {
	    list = new ArrayList<>();
	    for (Production production : productions) {
		if (production.getName().equals(productionName)) {
		    list.add(production);
		}
	    }
	    if (list.size() > 0) {
		name2Production.put(productionName, list);
	    } else {
		list = null;
	    }
	}
	return list;
    }

    public boolean has(String productionName) {
	return (get(productionName) != null);
    }

    public int getId(Production production) throws GrammarException {
	for (int i = 0; i < productions.size(); i++) {
	    if (productions.get(i).equals(production)) {
		return i;
	    }
	}
	throw new GrammarException("Could not find production '" + production + "'!");
    }

    public boolean hasEmptyDerivation(String name) {
	for (Production production : get(name)) {
	    if (production.getConstructions().size() == 0) {
		return true;
	    }
	}
	return false;
    }

    public ProductionSet setNewStartProduction(String productionName) throws GrammarException {
	ProductionSet productionSet = new ProductionSet();
	Production startProduction = new Production("_START_");
	startProduction.addConstruction(new NonTerminal(productionName));
	productionSet.add(startProduction);
	if (productions.size() > 1) {
	    for (int i = 1; i < productions.size(); i++) {
		productionSet.add(productions.get(i));
	    }
	}
	return productionSet;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((productions == null) ? 0 : productions.hashCode());
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
	ProductionSet other = (ProductionSet) obj;
	if (productions == null) {
	    if (other.productions != null)
		return false;
	} else if (!productions.equals(other.productions))
	    return false;
	return true;
    }

}
