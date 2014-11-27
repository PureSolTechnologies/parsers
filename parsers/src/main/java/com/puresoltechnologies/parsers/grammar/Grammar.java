package com.puresoltechnologies.parsers.grammar;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Properties;

import com.puresoltechnologies.parsers.grammar.production.Construction;
import com.puresoltechnologies.parsers.grammar.production.Production;
import com.puresoltechnologies.parsers.grammar.production.ProductionSet;
import com.puresoltechnologies.parsers.grammar.production.Terminal;
import com.puresoltechnologies.parsers.grammar.token.TokenDefinition;
import com.puresoltechnologies.parsers.grammar.token.TokenDefinitionSet;
import com.puresoltechnologies.parsers.lexer.Lexer;
import com.puresoltechnologies.parsers.parser.Parser;
import com.puresoltechnologies.parsers.preprocessor.Preprocessor;

/**
 * This class keeps the complete information about a single grammar including
 * all options, token definitions and productions.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class Grammar implements Serializable {

	private static final long serialVersionUID = 8296461694750760942L;

	private final String name;
	private final Properties options;
	private final TokenDefinitionSet tokenDefinitions;
	private final ProductionSet productions;
	private final String preProcessorClassName;
	private final String lexerClassName;
	private final String parserClassName;
	private final boolean usesPreProcessor;
	private final boolean ignoreCase;

	public Grammar(Properties options, TokenDefinitionSet tokenDefinitions,
			ProductionSet productions) throws GrammarException {
		super();
		this.name = options.getProperty("grammar.name");
		this.options = options;
		this.tokenDefinitions = tokenDefinitions;
		this.productions = productions;
		this.ignoreCase = Boolean.valueOf(options
				.getProperty("grammar.ignore-case"));

		usesPreProcessor = Boolean.valueOf(options
				.getProperty("preprocessor.use"));
		if (usesPreProcessor) {
			this.preProcessorClassName = options.getProperty("preprocessor");
		} else {
			this.preProcessorClassName = null;
		}
		this.lexerClassName = options.getProperty("lexer");
		this.parserClassName = options.getProperty("parser");

		checkConsistencyIfConfigured();
	}

	private void checkConsistencyIfConfigured() throws GrammarException {
		String checkGrammarProperty = options.getProperty("grammar.checks");
		if (checkGrammarProperty != null) {
			Boolean checkGrammar = Boolean.valueOf(checkGrammarProperty);
			if (!Boolean.valueOf(checkGrammar)) {
				return;
			}
		}
		List<TokenDefinition> tokenDefinitionsList = tokenDefinitions
				.getDefinitions();
		if (tokenDefinitionsList.size() == 0) {
			throw new GrammarException(
					"There are not tokens specified which can be applied.");
		}
		List<Production> productionList = productions.getList();
		if (productionList.size() == 0) {
			throw new GrammarException(
					"There are no productions for this grammar.");
		}
		for (Production production : productionList) {
			for (Construction construction : production.getConstructions()) {
				if (construction.isTerminal()) {
					if (!construction.getClass().equals(Terminal.class)) {
						continue;
					}
					if (tokenDefinitions.getDefinition(construction.getName()) == null) {
						throw new GrammarException("Token definition '"
								+ construction.getName()
								+ "' used in production '" + production
								+ "'is not defined ");
					}
				} else {
					if (productions.get(construction.getName()) == null) {
						throw new GrammarException("Production '"
								+ construction.getName()
								+ "' used in production '" + production
								+ "'is not defined ");
					}
				}
			}
		}
	}

	public final String getName() {
		return name;
	}

	/**
	 * @return the options
	 */
	public final Properties getOptions() {
		return options;
	}

	/**
	 * @return the tokenDefinitions
	 */
	public final TokenDefinitionSet getTokenDefinitions() {
		return tokenDefinitions;
	}

	/**
	 * @return the productions
	 */
	public final ProductionSet getProductions() {
		return productions;
	}

	public final Production getProduction(int productionId) {
		return productions.get(productionId);
	}

	public final boolean isIgnoreCase() {
		return ignoreCase;
	}

	public Preprocessor createPreprocessor(ClassLoader classLoader)
			throws GrammarException {
		try {
			@SuppressWarnings("unchecked")
			Class<? extends Preprocessor> clazz = (Class<? extends Preprocessor>) classLoader
					.loadClass(preProcessorClassName);
			return clazz.newInstance();
		} catch (InstantiationException e) {
			throw new GrammarException(
					"Cannot instantiate preprocessor with class'"
							+ preProcessorClassName + "'!", e);
		} catch (IllegalAccessException e) {
			throw new GrammarException(
					"Cannot instantiate preprocessor with class'"
							+ preProcessorClassName + "'!", e);
		} catch (ClassNotFoundException e) {
			throw new GrammarException(
					"Cannot instantiate preprocessor with class'"
							+ preProcessorClassName + "'!", e);
		}
	}

	public Lexer createLexer(ClassLoader classLoader) throws GrammarException {
		try {
			@SuppressWarnings("unchecked")
			Class<? extends Lexer> clazz = (Class<? extends Lexer>) classLoader
					.loadClass(lexerClassName);
			return clazz.getConstructor(Grammar.class).newInstance(this);
		} catch (InstantiationException e) {
			throw new GrammarException("Cannot instantiate lexer with class'"
					+ lexerClassName + "'!", e);
		} catch (IllegalAccessException e) {
			throw new GrammarException("Cannot instantiate lexer with class'"
					+ lexerClassName + "'!", e);
		} catch (IllegalArgumentException e) {
			throw new GrammarException("Cannot instantiate lexer with class'"
					+ lexerClassName + "'!", e);
		} catch (SecurityException e) {
			throw new GrammarException("Cannot instantiate lexer with class'"
					+ lexerClassName + "'!", e);
		} catch (InvocationTargetException e) {
			throw new GrammarException("Cannot instantiate lexer with class'"
					+ lexerClassName + "'!", e);
		} catch (NoSuchMethodException e) {
			throw new GrammarException("Cannot instantiate lexer with class'"
					+ lexerClassName + "'!", e);
		} catch (ClassNotFoundException e) {
			throw new GrammarException("Cannot instantiate lexer with class'"
					+ lexerClassName + "'!", e);
		}
	}

	public Parser createParser(ClassLoader classLoader) throws GrammarException {
		try {
			@SuppressWarnings("unchecked")
			Class<? extends Parser> clazz = (Class<? extends Parser>) classLoader
					.loadClass(parserClassName);
			return clazz.getConstructor(Grammar.class).newInstance(this);
		} catch (InstantiationException e) {
			throw new GrammarException("Cannot instantiate parser with class'"
					+ parserClassName + "'!", e);
		} catch (IllegalAccessException e) {
			throw new GrammarException("Cannot instantiate parser with class'"
					+ parserClassName + "'!", e);
		} catch (IllegalArgumentException e) {
			throw new GrammarException("Cannot instantiate parser with class'"
					+ parserClassName + "'!", e);
		} catch (SecurityException e) {
			throw new GrammarException("Cannot instantiate parser with class'"
					+ parserClassName + "'!", e);
		} catch (InvocationTargetException e) {
			throw new GrammarException("Cannot instantiate parser with class'"
					+ parserClassName + "'!", e);
		} catch (NoSuchMethodException e) {
			throw new GrammarException("Cannot instantiate parser with class'"
					+ parserClassName + "'!", e);
		} catch (ClassNotFoundException e) {
			throw new GrammarException("Cannot instantiate parser with class'"
					+ parserClassName + "'!", e);
		}
	}

	public boolean usesPreProcessor() {
		return usesPreProcessor;
	}

	@Override
	public final String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("=========\n");
		buffer.append(" Grammar\n");
		buffer.append("=========\n");
		buffer.append("\n");
		buffer.append("Options:\n");
		buffer.append("--------\n");
		buffer.append(toOptionsString());
		buffer.append("\n");
		buffer.append("Tokens:\n");
		buffer.append("-------\n");
		buffer.append(toTokenDefinitionsString());
		buffer.append("\n");
		buffer.append("Productions:\n");
		buffer.append("------------\n");
		buffer.append(toProductionsString());
		buffer.append("\n");
		return buffer.toString();
	}

	private StringBuilder toOptionsString() {
		StringBuilder buffer = new StringBuilder();
		for (Object key : options.keySet()) {
			buffer.append(key + " : " + options.getProperty((String) key)
					+ "\n");
		}
		return buffer;
	}

	private StringBuilder toTokenDefinitionsString() {
		StringBuilder buffer = new StringBuilder();
		for (TokenDefinition definition : tokenDefinitions.getDefinitions()) {
			buffer.append(definition + "\n");
		}
		return buffer;
	}

	public final StringBuilder toProductionsString() {
		StringBuilder buffer = new StringBuilder();
		List<Production> productionsList = productions.getList();
		for (Production production : productionsList) {
			buffer.append(production.toShortString(-1) + "\n");
		}
		return buffer;
	}

	public final Grammar createWithNewStartProduction(String string)
			throws GrammarException {
		return new Grammar(getOptions(), getTokenDefinitions(),
				getProductions().setNewStartProduction(string));
	}

	@Override
	public final int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (ignoreCase ? 1231 : 1237);
		result = prime * result + ((options == null) ? 0 : options.hashCode());
		result = prime * result
				+ ((productions == null) ? 0 : productions.hashCode());
		result = prime
				* result
				+ ((tokenDefinitions == null) ? 0 : tokenDefinitions.hashCode());
		return result;
	}

	@Override
	public final boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Grammar other = (Grammar) obj;
		if (ignoreCase != other.ignoreCase)
			return false;
		if (options == null) {
			if (other.options != null)
				return false;
		} else if (!options.equals(other.options))
			return false;
		if (productions == null) {
			if (other.productions != null)
				return false;
		} else if (!productions.equals(other.productions))
			return false;
		if (tokenDefinitions == null) {
			if (other.tokenDefinitions != null)
				return false;
		} else if (!tokenDefinitions.equals(other.tokenDefinitions))
			return false;
		return true;
	}
}
