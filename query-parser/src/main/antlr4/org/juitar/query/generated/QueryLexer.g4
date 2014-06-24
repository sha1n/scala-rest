/*
 * ANTLR Lexer rules declarations for QueryParser.
 */
lexer grammar QueryLexer;
@header {
package org.juitar.query.generated;
}
options {
    language=Java;
}


/* Keywords */
EQUALS          : ('eq'|'=');
NOTEQUAL        : ('ne'|'!=');
LESS            : ('lt'|'<');
GREATER         : ('gt'|'>');
LESSOREQUALS    : ('le'|'<=');
GREATEROREQUALS : ('ge'|'>=');
DOT             : '.';
COMMA           : ',';
COLON           : ':';
RPAREN          : ')';
LPAREN          : '(';
ASC             : 'asc';
DESC            : 'desc';
AND             : 'and';
OR              : 'or';
MINUS           : '-';

STRING
	: '\'' (~'\'')* '\'' ( '\'' (~'\'')* '\'' )*
	;

NUMBER
	: (N? DOT)? N DOT?
	| N DOT? N? ('E'|'e') MINUS? N
	;

WHITESPACE
	: ( '\t' | ' ' | '\r' | '\n'| '\u000C' )+ -> skip
	;

fragment N
	: DIGIT+ ;

fragment DIGIT
	: '0'..'9' ;

fragment IDENTIFIER_CHAR
	: 'a'..'z' | 'A'..'Z' | '_'
	;

IDENTIFIER
    : IDENTIFIER_CHAR (IDENTIFIER_CHAR | DIGIT)*
	;
