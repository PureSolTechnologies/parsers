package com.puresoltechnologies.parsers.grammar.production;

import com.puresoltechnologies.commons.types.ObjectUtilities;
import com.puresoltechnologies.parsers.lexer.Token;

/**
 * This class represents a single terminal.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public final class Terminal extends AbstractConstruction {

	private static final long serialVersionUID = 9050440704073872898L;

	private final String text;
	private final int hashcode;

	public Terminal(String name, String text) {
		super(name, true);
		this.text = text;
		hashcode = ObjectUtilities.calculateConstantHashCode(text);
	}

	public String getText() {
		return text;
	}

	public boolean matches(Token token) {
		return matches(token.getName(), token.getText());
	}

	public boolean matches(Terminal terminal) {
		return matches(terminal.getName(), terminal.getText());
	}

	public boolean matches(String name, String text) {
		if (!getName().equals(name)) {
			return false;
		}
		if ((this.text == null) || (this.text.isEmpty())) {
			return true;
		}
		return this.text.equals(text);
	}

	@Override
	public String toString() {
		if ((text == null) || (text.isEmpty())) {
			return super.toString();
		}
		return super.toString() + " '" + text + "'";
	}

	@Override
	public String toShortString() {
		if ((text == null) || (text.isEmpty())) {
			return super.toShortString();
		}
		return super.toShortString() + " '" + text + "'";
	}

	@Override
	public int hashCode() {
		return hashcode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Terminal other = (Terminal) obj;
		if (hashcode != other.hashcode) {
			return false;
		}
		if (text == null) {
			if (other.text != null)
				return false;
		} else if (!text.equals(other.text))
			return false;
		return true;
	}

}
