package com.puresoltechnologies.parsers.grammar.token;

import java.io.Serializable;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    public TokenDefinition(String name, String text) {
	this.name = name;
	this.pattern = Pattern.compile("^" + text);
	this.visibility = Visibility.VISIBLE;
	this.text = text;
	this.ignoreCase = false;
	hashCode = calculateHashCode();
    }

    public TokenDefinition(String name, String text, Visibility visibility) {
	this.name = name;
	this.pattern = Pattern.compile("^" + text);
	this.visibility = visibility;
	this.text = text;
	this.ignoreCase = false;
	hashCode = calculateHashCode();
    }

    public TokenDefinition(String name, String text, boolean ignoreCase) {
	this.name = name;
	if (ignoreCase) {
	    this.pattern = Pattern.compile("^" + text, Pattern.CASE_INSENSITIVE);
	} else {
	    this.pattern = Pattern.compile("^" + text);
	}
	this.visibility = Visibility.VISIBLE;
	this.text = text;
	this.ignoreCase = ignoreCase;
	hashCode = calculateHashCode();
    }

    @JsonCreator
    public TokenDefinition(@JsonProperty("name") String name, @JsonProperty("text") String text,
	    @JsonProperty("visibility") Visibility visibility, @JsonProperty("ignoreCase") boolean ignoreCase)
		    throws GrammarException {
	try {
	    this.name = name;
	    if (ignoreCase) {
		this.pattern = Pattern.compile("^" + text, Pattern.CASE_INSENSITIVE);
	    } else {
		this.pattern = Pattern.compile("^" + text);
	    }
	    this.visibility = visibility;
	    this.text = text;
	    this.ignoreCase = ignoreCase;
	    hashCode = calculateHashCode();
	} catch (PatternSyntaxException e) {
	    throw new GrammarException(
		    "Grammar failure in '" + name + "'!\nPattern: '" + text + "'\nRegExp-Message: " + e.getMessage());
	}
    }

    private int calculateHashCode() {
	return Objects.hash(name, pattern.pattern(), text, visibility);
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
    @JsonIgnore
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
	} else if (!pattern.pattern().equals(other.pattern.pattern()))
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
