package com.puresoltechnologies.parsers.grammar.token;

import java.io.Serializable;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.puresoltechnologies.commons.types.ObjectUtilities;
import com.puresoltechnologies.parsers.grammar.GrammarException;

/**
 * This class represents a single token definition for a lexer.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class TokenDefinition implements Serializable {

	private static final long serialVersionUID = 7060085634055524815L;

	private final String name;
	private final Pattern pattern;
	private final String text;
	private final Visibility visibility;
	private final int hashCode;
	private final boolean ignoreCase;

	public TokenDefinition(String name, String regex) {
		this.name = name;
		this.pattern = Pattern.compile("^" + regex);
		this.visibility = Visibility.VISIBLE;
		this.text = regex;
		this.ignoreCase = false;
		hashCode = calculateHashCode();
	}

	public TokenDefinition(String name, String regex, Visibility visibility) {
		this.name = name;
		this.pattern = Pattern.compile("^" + regex);
		this.visibility = visibility;
		this.text = regex;
		this.ignoreCase = false;
		hashCode = calculateHashCode();
	}

	public TokenDefinition(String name, String regex, boolean ignoreCase) {
		this.name = name;
		if (ignoreCase) {
			this.pattern = Pattern.compile("^" + regex,
					Pattern.CASE_INSENSITIVE);
		} else {
			this.pattern = Pattern.compile("^" + regex);
		}
		this.visibility = Visibility.VISIBLE;
		this.text = regex;
		this.ignoreCase = ignoreCase;
		hashCode = calculateHashCode();
	}

	public TokenDefinition(String name, String regex, Visibility visibility,
			boolean ignoreCase) throws GrammarException {
		try {
			this.name = name;
			if (ignoreCase) {
				this.pattern = Pattern.compile("^" + regex,
						Pattern.CASE_INSENSITIVE);
			} else {
				this.pattern = Pattern.compile("^" + regex);
			}
			this.visibility = visibility;
			this.text = regex;
			this.ignoreCase = ignoreCase;
			hashCode = calculateHashCode();
		} catch (PatternSyntaxException e) {
			throw new GrammarException("Grammar failure in '" + name
					+ "'!\nPattern: '" + regex + "'\nRegExp-Message: "
					+ e.getMessage());
		}
	}

	private int calculateHashCode() {
		return ObjectUtilities.calculateConstantHashCode(name, pattern, text,
				visibility);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the patterns
	 */
	public Pattern getPattern() {
		return pattern;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * @return the visibility
	 */
	public Visibility getVisibility() {
		return visibility;
	}

	/**
	 * 
	 * @return the ignoreCase flag
	 */
	public boolean isIgnoreCase() {
		return ignoreCase;
	}

	@Override
	public String toString() {
		return getName() + ": '" + getPattern() + "' (" + visibility + ")";
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TokenDefinition other = (TokenDefinition) obj;
		if (this.hashCode != other.hashCode) {
			return false;
		}
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (pattern == null) {
			if (other.pattern != null)
				return false;
		} else if (!pattern.equals(other.pattern))
			return false;
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		if (visibility != other.visibility)
			return false;
		return true;
	}

}
