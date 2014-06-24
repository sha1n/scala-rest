/*
 * ANTLR Parsing rules for QueryParser.
 */
parser grammar QueryParser;

options {
    language=Java;
    tokenVocab=QueryLexer;
}
   
@header {
package org.juitar.query.generated;

import org.juitar.query.api.ParserError;

}
@parser::members {

/**
 * This method overrides the default exitRule implementation of ANTLR in order to immediately exit with a
 * QueryParserException when the context contains errors.
 */
@Override
public void exitRule() {

    RecognitionException e = getContext().exception;
    if (e != null) {
        final String devMessagePattern = "Unexpected token: %s at %s";
        String tokenErrorDisplay = getTokenErrorDisplay(e.getOffendingToken());
        String errorHeader = getErrorHeader(e);
        throw new ParserError(String.format(devMessagePattern, tokenErrorDisplay, errorHeader), e);
    }

    super.exitRule();
}
}

// *** Root Rules

querySelect
	: expressionList EOF
	;

queryFilter
	: condition EOF
	;

queryOrder
    : orderList EOF
    ;

// *** Expression Rules

expr
	: NUMBER                #factorNumberExpression
	| stringExpression      #factorStringExpression
	| propertyExpression    #factorPropertyExpression
    ;

stringExpression
    : STRING
    ;

propertyExpression
    : IDENTIFIER (DOT IDENTIFIER)*
    ;

expressionList
	: expr (COMMA expr)*
	;


// *** Condition Rules

condition
	: conditionAnd (OR conditionAnd)*
	;

conditionAnd
	: simpleCondition (AND simpleCondition)*
	;

simpleCondition
	: (LPAREN condition RPAREN)
	| simpleComparisonCondition
	;

simpleComparisonCondition
	: expr comparisonOperator expr
	;

comparisonOperator
	: EQUALS | NOTEQUAL | LESS | GREATER | LESSOREQUALS | GREATEROREQUALS
	;

// *** Order Rules
orderList
	: orderItem (COMMA orderItem)*
	;

orderItem
	: expr (ASC | DESC)?
	;