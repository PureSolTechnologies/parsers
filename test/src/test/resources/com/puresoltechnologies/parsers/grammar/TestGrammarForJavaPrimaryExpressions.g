/*
 * This grammar was taken from the paper 'Packrat Parsers Can Support Left
 * Recursion' by Warth et.al.. The grammar is from figure 10.
 */

/*
 The next section is the OPTIONS section where all options are stored which 
 are important for processing file with this grammar.
 */
OPTIONS
	grammar.name="Test Grammar for Java's Primary Expressions";
	grammar.checks=true;
	grammar.ignore-case=false;
	grammar.ignored-leading=true;
	grammar.normalize_to_bnf=true;					// defines whether the grammar needs to be normalized to BNF

	// lexer is the class to be used for lexing
	lexer="com.puresoltechnologies.parsers.impl.lexer.RegExpLexer";
	
	// this options tells the lexer to be case insensitve
	lexer.case_sensitive=true;
	
	// parser is the class to be used for parsing
	parser="com.puresoltechnologies.parsers.impl.parser.packrat.PackratParser";
	parser.backtracking=false;

	preprocessor.use=false;							// usage of preprocessor required? 
	preprocessor="";								// usage of preprocessor required? 

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

	NEWLINE:			"(\r\n|\n|\r)" [ignore];
	WHITESPACE:			"[ \t]" [hide];
	DOT:				"\\.";
	LRECTANGULAR:		"\\[" ;
	RRECTANGULAR:		"\\]" ;
	LPAREN:				"\\(";
	RPAREN:				"\\)";
	THIS:				"this";
	SUPER:				"super";
	NEW:				"new";
	
	C:					"C";
	D:					"D";
	I:					"I";
	J:					"J";
	I_:					"i";
	J_:					"j";
	M_:					"m";
	N_:					"n";
	X_:					"x";
	Y_:					"y";

/*
 The next section is the PRODUCTIONS section where all grammar productions are
 specified for the parser.
 */
PRODUCTIONS

	_START_ : Primary; // actual Primary!

	Primary:
			PrimaryNoNewArray 
	;

	PrimaryNoNewArray:
			ClassInstanceCreationExpression
		|	MethodInvocation
		|	FieldAccess
		|	ArrayAccess
		|	THIS
	;

	ClassInstanceCreationExpression:
			NEW ClassOrInterfaceType LPAREN RPAREN
		|	Primary DOT NEW Identifier
	;

	MethodInvocation:
			Primary DOT Identifier LPAREN RPAREN
		|	MethodName LPAREN RPAREN
	;
	
	FieldAccess:
			Primary DOT Identifier
		|	SUPER DOT Identifier
	;
	
	ArrayAccess:
			Primary LRECTANGULAR Expression RRECTANGULAR
		|	ExpressionName LRECTANGULAR Expression RRECTANGULAR
	;
	
	ClassOrInterfaceType:
			ClassName
		|	InterfaceTypeName
	;
	
	ClassName:
			C
		|	D
	;
	
	InterfaceTypeName:
			I
		|	J
	;
	
	Identifier:
			X_
		|	Y_
		|	MethodName
		|	ClassOrInterfaceType
	;
	
	MethodName:
			M_
		|	N_
	;
	
	ExpressionName:
			Identifier
	;
	
	Expression:
			I_
		|	J_
	;
