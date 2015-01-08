package com.puresoltechnologies.parsers.lexer;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Objects;

import com.puresoltechnologies.parsers.grammar.token.Visibility;

/**
 * This class represents a single token which was identified by a lexer.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class Token implements Serializable, Cloneable {

    private static final long serialVersionUID = -9005444686111333074L;

    /**
     * This is the type id of the token. The true name is stored within the
     * token name reference table. Integers are better to handle and names
     * should be unique, too.
     */
    private final String name;
    /**
     * This is the text which was found in the source input which matched the
     * token definition with the name defined in name.
     */
    private final String text;
    private final Visibility visibility;
    private final TokenMetaData metaData;
    private final int hashcode;

    public Token(String name, String text, Visibility visibility,
	    TokenMetaData metaData) {
	super();
	this.name = name;
	if (text == null) {
	    this.text = "";
	} else {
	    this.text = text;
	}
	this.visibility = visibility;
	this.metaData = metaData;
	hashcode = Objects.hash(name, text, visibility, metaData);
    }

    /**
     * @return the name
     */
    public String getName() {
	return name;
    }

    /**
     * This is the text which was found in the source input which matched the
     * token definition with the name defined in name.
     * 
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

    public TokenMetaData getMetaData() {
	return metaData;
    }

    @Override
    public String toString() {
	return "\"" + text + "\" (" + name + ")";
    }

    @Override
    public int hashCode() {
	return hashcode;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	Token other = (Token) obj;
	if (hashCode() != other.hashCode())
	    return false;
	if (metaData == null) {
	    if (other.metaData != null)
		return false;
	} else if (!metaData.equals(other.metaData))
	    return false;
	if (name == null) {
	    if (other.name != null)
		return false;
	} else if (!name.equals(other.name))
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

    @Override
    public Token clone() {
	try {
	    Token cloned = (Token) super.clone();

	    Field name = cloned.getClass().getDeclaredField("name");
	    name.setAccessible(true);
	    name.set(cloned, this.name);

	    Field text = cloned.getClass().getDeclaredField("text");
	    text.setAccessible(true);
	    text.set(cloned, this.text);

	    Field visibility = cloned.getClass().getDeclaredField("visibility");
	    visibility.setAccessible(true);
	    visibility.set(cloned, this.visibility);

	    Field metaData = cloned.getClass().getDeclaredField("metaData");
	    metaData.setAccessible(true);
	    metaData.set(cloned, this.metaData.clone());

	    return cloned;
	} catch (CloneNotSupportedException e) {
	    throw new RuntimeException(e.getMessage());
	} catch (SecurityException e) {
	    throw new RuntimeException(e.getMessage());
	} catch (NoSuchFieldException e) {
	    throw new RuntimeException(e.getMessage());
	} catch (IllegalArgumentException e) {
	    throw new RuntimeException(e.getMessage());
	} catch (IllegalAccessException e) {
	    throw new RuntimeException(e.getMessage());
	}
    }
}
