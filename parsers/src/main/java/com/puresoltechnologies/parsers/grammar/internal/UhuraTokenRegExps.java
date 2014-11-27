package com.puresoltechnologies.parsers.grammar.internal;

/**
 * This class is a collection of public final strings which specified regular
 * expressions for Uhura grammar files. These definitions are taken out of Java
 * Language specification due to its clean representation.
 * 
 * @author Rick-Rainer Ludwig
 * 
 */
public class UhuraTokenRegExps {

    /********************************
     * 3.3 Unicode Escapes
     ********************************/
    public static final String HEX_DIGIT = "[0-9a-fA-F]";
    public static final String RAW_INPUT_CHARACTER = "\\w"; /*
							     * TODO check for
							     * UNITCODE!!
							     */
    public static final String UNICODE_MARKER = "u+";
    public static final String UNICODE_ESCAPE = "\\\\" + UNICODE_MARKER
	    + HEX_DIGIT + "{4}";
    public static final String UNICODE_INPUT_CHARACTER = "(" + UNICODE_ESCAPE
	    + "|" + RAW_INPUT_CHARACTER + ")";

    /********************************
     * 3.4 Line Terminators
     ********************************/
    public static final String INPUT_CHARACTER = "[^\\r\\n]";
    public static final String LINE_TERMINATOR = "(\\r\\n|\\r|\\n)";

    /********************************
     * 3.5 Input Elements and Tokens
     ********************************/
    public static final String SUB = "\\x1A";
    /*
     * All other things are used and separated into the lexer engine.
     */

    /********************************
     * 3.6 White Space
     ********************************/
    public static final String WHITE_SPACE = "( |\\t|\\f|" + LINE_TERMINATOR
	    + ")";

    /********************************
     * 3.7 Comments
     ********************************/
    public static final String CHARACTERS_IN_LINE = INPUT_CHARACTER + "+";

    public static final String NOT_STAR = "[^*]";

    public static final String COMMENT_TAIL = "([^\\*]|\\*(?!/))*\\*/";

    public static final String END_OF_LINE_COMMENT = "//" + INPUT_CHARACTER
	    + "*" + LINE_TERMINATOR;
    public static final String TRADITIONAL_COMMENT = "/\\*" + COMMENT_TAIL;
    public static final String COMMENT = "(" + TRADITIONAL_COMMENT + "|"
	    + END_OF_LINE_COMMENT + ")";

    /********************************
     * 3.8 Identifiers
     ********************************/
    public static final String JAVA_LETTER_OR_DIGIT = "[-a-zA-Z_$0-9]";
    public static final String JAVA_LETTER = "[a-zA-Z_$]";
    public static final String IDENTIFIER_CHARS = JAVA_LETTER
	    + JAVA_LETTER_OR_DIGIT + "*";
    /*
     * The absence of a keyword, boolean oder null literal has to be assured
     * during lexical scan!
     */
    public static final String IDENTIFIER = IDENTIFIER_CHARS;

    /********************************
     * 3.10 Literals
     ********************************/

    /*
     * 3.10.1 Integer Literals
     */
    public static final String INTEGER_TYPE_SUFFIX = "[lL]";

    public static final String NON_ZERO_DIGIT = "[1-9]";
    public static final String DIGIT = "[0-9]";
    public static final String DIGITS = DIGIT + "+";
    public static final String DECIMAL_NUMERAL = "(0|" + NON_ZERO_DIGIT + DIGIT
	    + "*)";

    public static final String HEX_DIGITS = HEX_DIGIT + "+";
    public static final String HEX_NUMERAL = "0[xX]" + HEX_DIGITS;

    public static final String OCTAL_DIGIT = "[0-7]";
    public static final String OCTAL_DIGITS = OCTAL_DIGIT + "+";
    public static final String OCTAL_NUMERAL = "0" + OCTAL_DIGITS;

    public static final String DECIMAL_INTEGER_LITERAL = DECIMAL_NUMERAL
	    + INTEGER_TYPE_SUFFIX + "?";
    public static final String HEX_INTEGER_LITERAL = HEX_NUMERAL
	    + INTEGER_TYPE_SUFFIX + "?";
    public static final String OCTAL_INTEGER_LITERAL = OCTAL_NUMERAL
	    + INTEGER_TYPE_SUFFIX + "?";

    public static final String INTEGER_LITERAL = "(" + HEX_INTEGER_LITERAL
	    + "|" + OCTAL_INTEGER_LITERAL + "|" + DECIMAL_INTEGER_LITERAL + ")";

    /*
     * 3.10.2 Floating-Point Literals
     */
    public static final String FLOAT_TYPE_SUFFIX = "[fFdD]";
    public static final String SIGN = "[+-]";
    public static final String SIGNED_INTEGER = SIGN + "?" + DIGITS;

    public static final String BINARY_EXPONENT_INDICATOR = "[pP]";
    public static final String BINARY_EXPONENT = BINARY_EXPONENT_INDICATOR
	    + SIGNED_INTEGER;
    public static final String HEX_SIGNIFICAND = "(" + HEX_NUMERAL
	    + "\\.?|0[xX](" + HEX_DIGITS + ")?\\." + HEX_DIGITS + ")";
    public static final String HEXADECIMAL_FLOATING_POINT_LITERAL = HEX_SIGNIFICAND
	    + BINARY_EXPONENT + FLOAT_TYPE_SUFFIX + "?";

    public static final String EXPONENT_INDICATOR = "[eE]";
    public static final String EXPONENT_PART = EXPONENT_INDICATOR
	    + SIGNED_INTEGER;
    public static final String DECIMAL_FLOATING_POINT_LITERAL = "(" + DIGITS
	    + "\\.(" + DIGITS + ")?(" + EXPONENT_PART + ")?"
	    + FLOAT_TYPE_SUFFIX + "?|\\." + DIGITS + "(" + EXPONENT_PART + ")?"
	    + FLOAT_TYPE_SUFFIX + "?|" + DIGITS + EXPONENT_PART
	    + FLOAT_TYPE_SUFFIX + "?|" + DIGITS + "(" + EXPONENT_PART + ")?"
	    + FLOAT_TYPE_SUFFIX + ")";

    public static final String FLOATING_POINT_LITERAL = "("
	    + DECIMAL_FLOATING_POINT_LITERAL + "|"
	    + HEXADECIMAL_FLOATING_POINT_LITERAL + ")";

    /*
     * 3.10.3 Boolean Literals
     */
    public static final String BOOLEAN_LITERAL = "(true|false)";

    /*
     * 3.10.6 Escape Sequences for Character and String Literals (This was moved
     * forward due to a need for these definitions in Character and String
     * Literals)
     */
    public static final String ZERO_TO_THREE = "[0-3]";
    public static final String OCTAL_ESCAPE = "(\\\\" + OCTAL_DIGIT
	    + "{1,2}|\\\\" + ZERO_TO_THREE + OCTAL_DIGIT + "{2})";
    /*
     * The UNICODE_ESCAPE was introduced to handle them in a general approach.
     * Normally all the unicode escapes are translated in the first step of
     * source processing.
     */
    public static final String ESCAPE_SEQUENCE = "(\\\\b|\\\\t|\\\\n|\\\\f|\\\\r|\\\\\"|\\\\'|\\\\\\\\|"
	    + OCTAL_ESCAPE + "|" + UNICODE_ESCAPE + ")";

    /*
     * 3.10.4 Character Literals
     */
    public static final String SINGLE_CHARACTER = "[^'\\\\]";
    /*
     * TODO Check for UNICODE raws ^^^!
     */
    public static final String CHARACTER_LITERAL = "('" + SINGLE_CHARACTER
	    + "'|'" + ESCAPE_SEQUENCE + "')";

    /**
     * 3.10.5 String Literals
     */
    public static final String STRING_CHARACTER = "([^\"\\\\]|"
	    + ESCAPE_SEQUENCE + ")";
    public static final String STRING_CHARACTER_SINGLE_QUOTED = "([^'\\\\]|"
	    + ESCAPE_SEQUENCE + ")";
    public static final String STRING_CHARACTERS = STRING_CHARACTER + "+";
    public static final String STRING_CHARACTERS_SINGLE_QUOTED = STRING_CHARACTER_SINGLE_QUOTED
	    + "+";
    public static final String STRING_LITERAL = "(\"(" + STRING_CHARACTERS
	    + ")?\"|'(" + STRING_CHARACTERS_SINGLE_QUOTED + ")?')";

}
