package com.puresoltechnologies.parsers.grammar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.puresoltechnologies.commons.trees.TreeException;
import com.puresoltechnologies.parsers.grammar.production.Construction;
import com.puresoltechnologies.parsers.grammar.production.NonTerminal;
import com.puresoltechnologies.parsers.grammar.production.Production;
import com.puresoltechnologies.parsers.grammar.production.ProductionSet;
import com.puresoltechnologies.parsers.grammar.production.Terminal;
import com.puresoltechnologies.parsers.grammar.token.TokenDefinition;
import com.puresoltechnologies.parsers.grammar.token.TokenDefinitionSet;
import com.puresoltechnologies.parsers.grammar.token.Visibility;
import com.puresoltechnologies.parsers.parser.ParserTree;

/**
 * This converter converts an AST from a grammar file into a Grammar object. It
 * generates a Properties field with all grammar properties, a
 * TokenDefinitionSet with all token definitions found and a ProductionSet with
 * all BNF productions converted from the EBNF in the grammar file.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class GrammarConverter {

    private final ParserTree parserTree;

    private Properties options = null;
    private TokenDefinitionSet tokenDefinitions = null;
    private ProductionSet productions = null;
    private Grammar grammar = null;
    private final Map<String, Visibility> tokenVisibility = new HashMap<String, Visibility>();
    private int autogenId;

    // private boolean normalizeToBNF;
    /*
     * TODO the usage of this value is to be inserted!
     */

    /**
     * Constructor for file reading.
     * 
     * @param parserTree
     *            is the {@link ParserTree} from the grammar file which is to be
     *            converted.
     * @throws GrammarException
     *             is thrown in case of grammar issues.
     */
    public GrammarConverter(ParserTree parserTree) throws GrammarException {
	try {
	    this.parserTree = parserTree;
	    convert();
	} catch (TreeException e) {
	    throw new GrammarException(
		    "The syntax tree of the grammar is not consistent!", e);
	}
    }

    /**
     * This method returns the read grammar.
     * 
     * @return A {@link Grammar} object os returned.
     */
    public Grammar getGrammar() {
	return grammar;
    }

    /**
     * This method returns the syntax tree from the read grammar file to
     * retrieve additional information if needed.
     * 
     * @return A {@link ParserTree} object is returned.
     */
    public ParserTree getParserTree() {
	return parserTree;
    }

    /**
     * This method converts the read AST from grammar file into a final grammar
     * ready to be used.
     * 
     * @throws GrammarException
     * @throws TreeException
     */
    private void convert() throws GrammarException, TreeException {
	convertOptions();
	convertTokenDefinitions();
	convertProductions();
	grammar = new Grammar(options, tokenDefinitions, productions);
    }

    /**
     * This method converts the options part of the AST.
     * 
     * @throws TreeException
     */
    private void convertOptions() throws TreeException {
	options = new Properties();
	ParserTree optionList = parserTree.getChild("GrammarOptions").getChild(
		"GrammarOptionList");
	for (ParserTree option : optionList.getChildren("GrammarOption")) {
	    String name = option.getChild("PropertyIdentifier").getText();
	    String value = option.getChild("Literal").getText();
	    if (value.startsWith("'") || value.startsWith("\"")) {
		value = value.substring(1, value.length() - 1);
	    }
	    options.put(name, value);
	}
	// normalizeToBNF = Boolean.valueOf(options.getProperty(
	// "grammar.normalize_to_bnf", "true"));
    }

    /**
     * This method converts the token definitions to the token definition set.
     * 
     * @throws GrammarException
     * @throws TreeException
     */
    private void convertTokenDefinitions() throws GrammarException,
	    TreeException {
	tokenVisibility.clear();
	Map<String, ParserTree> helpers = getHelperTokens();
	Map<String, ParserTree> tokens = getTokens();
	convertTokenDefinitions(helpers, tokens);
    }

    /**
     * This method reads all helpers from AST and returns a map with all of
     * them.
     * 
     * @return
     * @throws TreeException
     */
    private Map<String, ParserTree> getHelperTokens() throws TreeException {
	Map<String, ParserTree> helpers = new HashMap<String, ParserTree>();
	ParserTree helperTree = parserTree.getChild("Helper");
	ParserTree helperDefinitions = helperTree.getChild("HelperDefinitions");
	for (ParserTree helperDefinition : helperDefinitions
		.getChildren("HelperDefinition")) {
	    String identifier = helperDefinition.getChild("IDENTIFIER")
		    .getText();
	    helpers.put(identifier, helperDefinition);
	}
	return helpers;
    }

    /**
     * This method reads all tokens from AST and returns a map with all of them.
     * 
     * @return
     * @throws TreeException
     */
    private Map<String, ParserTree> getTokens() throws GrammarException,
	    TreeException {
	Map<String, ParserTree> tokens = new HashMap<String, ParserTree>();
	ParserTree tokensTree = parserTree.getChild("Tokens");
	ParserTree tokenDefinitions = tokensTree.getChild("TokenDefinitions");
	for (ParserTree tokenDefinition : tokenDefinitions
		.getChildren("TokenDefinition")) {
	    // identifier...
	    String identifier = tokenDefinition.getChild("IDENTIFIER")
		    .getText();
	    // token parts...
	    tokens.put(identifier, tokenDefinition);
	    // visibility...
	    ParserTree visibilityAST = tokenDefinition.getChild("Visibility");
	    if (visibilityAST.hasChild("HIDE")) {
		tokenVisibility.put(identifier, Visibility.HIDDEN);
	    } else if (visibilityAST.hasChild("IGNORE")) {
		tokenVisibility.put(identifier, Visibility.IGNORED);
	    } else {
		tokenVisibility.put(identifier, Visibility.VISIBLE);
	    }
	}
	return tokens;
    }

    /**
     * This method starts and controls the process for final conversion of all
     * tokens from the token and helper skeletons.
     * 
     * @param helpers
     * @param tokens
     * @throws GrammarException
     * @throws TreeException
     */
    private void convertTokenDefinitions(Map<String, ParserTree> helpers,
	    Map<String, ParserTree> tokens) throws GrammarException,
	    TreeException {
	tokenDefinitions = new TokenDefinitionSet();
	for (ParserTree tokenDefinitionAST : parserTree.getChild("Tokens")
		.getChild("TokenDefinitions").getChildren("TokenDefinition")) {
	    TokenDefinition convertedTokenDefinition = getTokenDefinition(
		    tokenDefinitionAST, helpers, tokens);
	    tokenDefinitions.addDefinition(convertedTokenDefinition);
	}
    }

    /**
     * This is the method which merges all tokens with their helpers.
     * 
     * @param tokenName
     * @param helpers
     * @param tokens
     * @return
     * @throws GrammarException
     * @throws TreeException
     */
    private TokenDefinition getTokenDefinition(ParserTree tokenDefinition,
	    Map<String, ParserTree> helpers, Map<String, ParserTree> tokens)
	    throws GrammarException, TreeException {
	String tokenName = tokenDefinition.getChild("IDENTIFIER").getText();
	String pattern = createTokenDefinitionPattern(tokenDefinition, helpers,
		tokens);
	boolean ignoreCase = Boolean.valueOf((String) options
		.get("grammar.ignore-case"));
	if (tokenVisibility.get(tokenName) != null) {
	    return new TokenDefinition(tokenName, pattern,
		    tokenVisibility.get(tokenName), ignoreCase);
	} else {
	    return new TokenDefinition(tokenName, pattern, ignoreCase);
	}
    }

    private String createTokenDefinitionPattern(ParserTree tokenDefinition,
	    Map<String, ParserTree> helpers, Map<String, ParserTree> tokens)
	    throws TreeException, GrammarException {
	StringBuffer pattern = new StringBuffer();
	boolean firstConstruction = true;
	List<ParserTree> children = tokenDefinition.getChild(
		"TokenConstructions").getChildren("TokenConstruction");
	if (children.size() > 1) {
	    pattern.append("(");
	}
	for (ParserTree tokenConstruction : children) {
	    if (firstConstruction) {
		firstConstruction = false;
	    } else {
		pattern.append("|");
	    }
	    pattern.append(getTokenDefinitionPattern(tokenConstruction,
		    helpers, tokens));
	}
	if (children.size() > 1) {
	    pattern.append(")");
	}
	return pattern.toString();
    }

    private StringBuffer getTokenDefinitionPattern(
	    ParserTree tokenConstruction, Map<String, ParserTree> helpers,
	    Map<String, ParserTree> tokens) throws GrammarException,
	    TreeException {
	StringBuffer pattern = new StringBuffer();
	for (ParserTree tree : tokenConstruction.getChildren("TokenPart")) {
	    if (tree.hasChild("STRING_LITERAL")) {
		String text = tree.getChild("STRING_LITERAL").getText();
		text = text.replaceAll("\\\\\\\\", "\\\\");
		pattern.append(text.substring(1, text.length() - 1));
	    } else {
		String text = tree.getChild("IDENTIFIER").getText();
		if (helpers.containsKey(text)) {
		    text = getTokenDefinition(helpers.get(text), helpers,
			    tokens).getText();
		} else if (tokens.containsKey(text)) {
		    text = getTokenDefinition(tokens.get(text), helpers, tokens)
			    .getText();
		} else {
		    throw new GrammarException(
			    "Could not find definition for token '" + text
				    + "'!");
		}
		pattern.append(text);
	    }
	    if (tree.hasChild("Quantifier")) {
		pattern.append(tree.getChild("Quantifier").getText());
	    }
	}
	return pattern;
    }

    /**
     * This method converts all productions from the AST into a ProductionSet.
     * 
     * @throws TreeException
     * @throws GrammarException
     */
    private void convertProductions() throws TreeException, GrammarException {
	productions = new ProductionSet();
	ParserTree productionsTree = parserTree.getChild("Productions");
	ParserTree productionDefinitions = productionsTree
		.getChild("ProductionDefinitions");
	for (ParserTree productionDefinition : productionDefinitions
		.getChildren("ProductionDefinition")) {
	    convertProductionGroup(productionDefinition);
	}
    }

    /**
     * This method converts a production group (bind together by the vertical
     * bar '|') into a set of productions.
     * 
     * @param productionDefinition
     * @throws GrammarException
     * @throws TreeException
     */
    private void convertProductionGroup(ParserTree productionDefinition)
	    throws GrammarException, TreeException {
	String productionName = productionDefinition.getChild("IDENTIFIER")
		.getText();
	ParserTree productionConstructions = productionDefinition
		.getChild("ProductionConstructions");
	convertSingleProductions(productionName, productionConstructions);
    }

    /**
     * This method converts the list of productions from a group with a given
     * name into a set of productions.
     * 
     * @param productionName
     * @param productionConstructions
     * @throws TreeException
     * @throws GrammarException
     */
    private void convertSingleProductions(String productionName,
	    ParserTree productionConstructions) throws TreeException,
	    GrammarException {
	for (ParserTree productionConstruction : productionConstructions
		.getChildren("ProductionConstruction")) {
	    convertSingleProduction(productionName, productionConstruction);
	}
    }

    /**
     * This method converts a single production from a list of productions
     * within a production group into a single production. Some additional
     * productions might be created during the way due to construction grouping
     * and quantifiers.
     * 
     * The alternative name for a production is processed here, too.
     * 
     * @param productionName
     * @param productionConstruction
     * @throws TreeException
     * @throws GrammarException
     */
    private void convertSingleProduction(String productionName,
	    ParserTree productionConstruction) throws TreeException,
	    GrammarException {
	ParserTree alternativeIdentifier = productionConstruction
		.getChild("AlternativeIdentifier");
	Production production;
	if (alternativeIdentifier == null) {
	    production = new Production(productionName);
	} else {
	    ParserTree alternativeIdentifierName = alternativeIdentifier
		    .getChild("IDENTIFIER");
	    if (alternativeIdentifierName == null) {
		production = new Production(productionName);
	    } else {
		production = new Production(productionName,
			alternativeIdentifierName.getText());
	    }
	}
	production
		.addAllConstructions(getConstructions(productionConstruction));
	addOptions(production, productionConstruction);
	productions.add(production);
    }

    private List<Construction> getConstructions(
	    ParserTree productionConstruction) throws TreeException,
	    GrammarException {
	List<Construction> constructions = new ArrayList<Construction>();
	ParserTree productionParts = productionConstruction
		.getChild("ProductionParts");
	if (productionParts != null) {
	    for (ParserTree productionPart : productionParts.getChildren()) {
		constructions.add(getConstruction(productionPart,
			tokenDefinitions));
	    }
	}
	return constructions;
    }

    private Construction getConstruction(ParserTree productionPart,
	    TokenDefinitionSet tokenDefinitions) throws TreeException,
	    GrammarException {
	Quantity quantity = Quantity.fromSymbol(productionPart.getChild(
		"Quantifier").getText());
	if (quantity != Quantity.EXPECT) {
	    return generateExtraQuantifierRules(productionPart, quantity);
	}
	return createConstruction(productionPart);
    }

    private Construction createConstruction(ParserTree productionPart)
	    throws TreeException, GrammarException {
	if ("ConstructionIdentifier".equals(productionPart.getName())) {
	    String identifier = productionPart.getChild("IDENTIFIER").getText();
	    TokenDefinition tokenDefinition = tokenDefinitions
		    .getDefinition(identifier);
	    if (tokenDefinition != null) {
		return new Terminal(identifier, null);
	    } else {
		return new NonTerminal(identifier);
	    }
	} else if ("ConstructionLiteral".equals(productionPart.getName())) {
	    ParserTree stringLiteral = productionPart
		    .getChild("STRING_LITERAL");
	    String text = stringLiteral.getText();
	    text = text.substring(1, text.length() - 1);
	    Terminal terminal = null;
	    for (int i = 0; i < tokenDefinitions.getDefinitions().size(); i++) {
		TokenDefinition tokenDefinition = tokenDefinitions
			.getDefinition(i);
		if (tokenDefinition.getPattern().matcher(text).matches()) {
		    if (terminal != null) {
			throw new GrammarException(
				"Token text '"
					+ text
					+ "' satisfies several token definitions and is therefore ambiguous!");
		    }
		    terminal = new Terminal(tokenDefinition.getName(), text);
		}
	    }
	    if (terminal == null) {
		throw new GrammarException("Token text '" + text
			+ "' does not satisfy any token definition!");
	    }
	    return terminal;
	} else if ("ConstructionGroup".equals(productionPart.getName())) {
	    ParserTree productionConstructions = productionPart
		    .getChild("ProductionConstructions");
	    String newIdentifier = createNewIdentifier(productionPart, "group");
	    convertSingleProductions(newIdentifier, productionConstructions);
	    return new NonTerminal(newIdentifier);
	} else {
	    throw new TreeException("Invalid node '" + productionPart + "'!");
	}
    }

    private Construction generateExtraQuantifierRules(
	    ParserTree productionPart, Quantity quantity) throws TreeException,
	    GrammarException {
	if (quantity == Quantity.ACCEPT) {
	    return generateOptionalProduction(productionPart);
	} else if (quantity == Quantity.ACCEPT_MANY) {
	    return generateOptionalList(productionPart);
	} else if (quantity == Quantity.EXPECT_MANY) {
	    return generateList(productionPart);
	}
	throw new GrammarException("Impossible to generate extra rules for '"
		+ productionPart + "' with quantity '" + quantity + "'!");
    }

    /**
     * This method creates a new BNF for a construction which is marked with '?'
     * to be use zero or one time.
     * 
     * @param productionPart
     * @return
     * @throws TreeException
     * @throws GrammarException
     */
    private Construction generateOptionalProduction(ParserTree productionPart)
	    throws TreeException, GrammarException {
	String newIdentifier = createNewIdentifier(productionPart, "opt");
	Construction construction = createConstruction(productionPart);

	Production production = new Production(newIdentifier);
	production.addConstruction(construction);
	production.setNode(false);
	production.setStackingAllowed(false);
	productions.add(production);

	production = new Production(newIdentifier);
	production.setNode(false);
	production.setStackingAllowed(false);
	productions.add(production);

	return new NonTerminal(newIdentifier);
    }

    /**
     * This method generates a new construction identifier for an automatically
     * generated BNF production for optionals, optional lists and lists.
     * 
     * @param productionPart
     * @param suffix
     * @return
     * @throws TreeException
     */
    private String createNewIdentifier(ParserTree productionPart, String suffix)
	    throws TreeException {
	String identifier = "";
	if (productionPart.hasChild("IDENTIFIER")) {
	    identifier = productionPart.getChild("IDENTIFIER").getText();
	} else if (productionPart.hasChild("STRING_LITERAL")) {
	    identifier = productionPart.getChild("STRING_LITERAL").getText();
	}
	return createIdentifierName(identifier, suffix);
    }

    private String createIdentifierName(String identifier, String suffix) {
	autogenId++;
	return identifier + "_" + suffix + "_autogen_" + autogenId;
    }

    /**
     * This method creates a new BNF for a construction which is marked with '*'
     * to be use zero or more times.
     * 
     * @param productionPart
     * @return
     * @throws TreeException
     * @throws GrammarException
     */
    private Construction generateOptionalList(ParserTree productionPart)
	    throws TreeException, GrammarException {
	String newIdentifier = createNewIdentifier(productionPart, "optlist");
	Construction construction = createConstruction(productionPart);

	Production production = new Production(newIdentifier);
	production.addConstruction(new NonTerminal(newIdentifier));
	production.addConstruction(construction);
	production.setNode(false);
	production.setStackingAllowed(false);
	productions.add(production);

	production = new Production(newIdentifier);
	production.setNode(false);
	production.setStackingAllowed(false);
	productions.add(production);

	return new NonTerminal(newIdentifier);
    }

    /**
     * This method creates a new BNF for a construction which is marked with '+'
     * to be use one or more times.
     * 
     * @param productionPart
     * @return
     * @throws TreeException
     * @throws GrammarException
     */
    private Construction generateList(ParserTree productionPart)
	    throws TreeException, GrammarException {
	String newIdentifier = createNewIdentifier(productionPart, "list");
	Construction construction = createConstruction(productionPart);

	Production production = new Production(newIdentifier);
	production.addConstruction(new NonTerminal(newIdentifier));
	production.addConstruction(construction);
	production.setNode(false);
	production.setStackingAllowed(false);
	productions.add(production);

	production = new Production(newIdentifier);
	production.addConstruction(construction);
	production.setNode(false);
	production.setStackingAllowed(false);
	productions.add(production);

	return new NonTerminal(newIdentifier);
    }

    private void addOptions(Production production,
	    ParserTree productionConstruction) throws TreeException {
	ParserTree options = productionConstruction
		.getChild("ProductionOptions");
	if (options != null) {
	    for (ParserTree option : options
		    .getChildren("ProductionOptionList")) {
		if (option.hasChild("NODE")) {
		    production.setNode(Boolean.valueOf(option.getChild(
			    "BOOLEAN_LITERAL").getText()));
		}
		if (option.hasChild("STACK")) {
		    production.setStackingAllowed(Boolean.valueOf(option
			    .getChild("BOOLEAN_LITERAL").getText()));
		}
	    }
	}
    }
}
