/*
 The next section is the OPTIONS section where all options are stored which 
 are important for processing file with this grammar.
 */
OPTIONS

	// lexer is the class to be used for lexing
	lexer="com.puresoltechnologies.uhura.lexer.RegExpLexer";
	
	// this options tells the lexer to be case insensitve
	lexer.case_sensitive=true;
	
	// parser is the class to be used for parsing
	parser="com.puresoltechnologies.uhura.parser.lr.LR1Parser";

/*
 The next section is the HELPER section where all tokens are to be defined
  which are later not within the token stream, but which are used to define
  tokens which are found there later on.
 */
HELPER
 
/*
 The next section is the TOKENS section where all tokens are specified which
 will be found within the token stream after lexing.
 */
TOKENS

	I:	"i";

/*
 The next section is the PRODUCTIONS section where all grammar productions are
 specified for the parser.
 */
PRODUCTIONS

	_START_ : i ;

	i:
				i I
		|
	;
