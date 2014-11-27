/*
 The next section is the OPTIONS section where all options are stored which 
 are important for processing file with this grammar.
 */
OPTIONS

	// lexer is the class to be used for lexing
	lexer="com.puresoltechnologies.parsers.lexer.RegExpLexer";
	
	// this options tells the lexer to be case insensitve
	lexer.case_sensitive=true;
	
	// parser is the class to be used for parsing
	parser="com.puresoltechnologies.parsers.parser.lr.LR1Parser";
	
	grammar.checks=false;

/*
 The next section is the HELPER section where all tokens are to be defined
  which are later not within the token stream, but which are used to define
  tokens which are found there later on.
 */
HELPER
 
	DIGITS_NONZERO:	"[1-9]";
	DIGITS:			"[0-9]";

/*
 The next section is the TOKENS section where all tokens are specified which
 will be found within the token stream after lexing.
 */
TOKENS

	NEWLINE:			"(\r\n|\n|\r)" [hide];
	WHITESPACE:			"[ \t]" [hide];
	INTEGER_LITERAL:	"(\\+|\\-)?" DIGITS_NONZERO + DIGITS *;
	PLUS:				"\\+";
	MINUS:				"\\-";
	STAR:				"\\*";
	SLASH:				"/";
	COMMA:				",";
	ID:					"[A-Za-z0-9_]";

/*
 The next section is the PRODUCTIONS section where all grammar productions are
 specified for the parser.
 */
PRODUCTIONS

	_START_ : Lists ;

	OptionalList:
		INTEGER_LITERAL *
	;

	List:
		INTEGER_LITERAL +
	;

	OptionalPart:
		INTEGER_LITERAL ?
	;
	