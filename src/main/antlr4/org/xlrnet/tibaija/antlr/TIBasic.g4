/*
 *  ANTLR v4 grammar for TI-Basic on TI-83/84-like calculators.
 *
 *  Copyright (c) 2015 J.Hendeß
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

/**
 * General notes:
 *  ALL_CAPITAL     -> Lexer token
 *  FirstCapital    -> Lexer rule
 *  firstNotCapital -> Parser rule
 */


grammar TIBasic;

/** Actual grammar */

program
       : commandList;

commandList
       : (SEPARATOR command)*;

command
       : (statement | expressionParent | );

expressionParent: expression;                           // Pseudo-expression for handling the ANS variable


/* BEGIN Code for operator precendece          */
/* See http://tibasicdev.wikidot.com/operators */

expression
       : expression_conv;

expression_conv returns [ boolean isToDec, boolean isToFrac ]
       : expression_xor ( TO_DEC { $isToDec = true; } | TO_FRAC { $isToFrac = true; } )?;

expression_xor returns [ List<String> operators ]
@init { _localctx.operators = new ArrayList<String>(); }
       : expression_or ( XOR expression_or { $operators.add("xor"); })*;

expression_or returns [ List<String> operators ]
@init { _localctx.operators = new ArrayList<String>(); }
       : expression_and ( OR expression_and { $operators.add("or"); })*;

expression_and returns [ List<String> operators ]
@init { _localctx.operators = new ArrayList<String>(); }
       : expression_compare ( AND expression_compare { $operators.add("and"); } )*;

expression_compare returns [ List<String> operators ]
@init { _localctx.operators = new ArrayList<String>(); }
       : expression_plus_minus (
         ( EQUALS { $operators.add($EQUALS.text); }
         | NOT_EQUALS { $operators.add($NOT_EQUALS.text); }
         | LESS_THAN { $operators.add($LESS_THAN.text); }
         | GREATER_THAN { $operators.add($GREATER_THAN.text); }
         | LESS_OR_EQUAL { $operators.add($LESS_OR_EQUAL.text); }
         | GREATER_OR_EQUAL { $operators.add($GREATER_OR_EQUAL.text); }
         )
         expression_plus_minus )*;

expression_plus_minus returns [ List<String> operators ]
@init { _localctx.operators = new ArrayList<String>(); }
       : expression_mul_div (
         ( PLUS { $operators.add($PLUS.text); }
         | MINUS { $operators.add($MINUS.text); }
         )
         expression_mul_div )*;

expression_mul_div returns [ List<String> operators ]
@init { _localctx.operators = new ArrayList<String>(); }
       : expression_infix (
         ( MULTIPLY { $operators.add($MULTIPLY.text); }
         | DIVIDE { $operators.add($DIVIDE.text); }
         | { $operators.add("*"); }              // Implicit multiplication
         )
         expression_infix )*;

expression_infix returns [ List<String> operators ]
@init { _localctx.operators = new ArrayList<String>(); }
       : expression_negation (
         ( NPR { $operators.add($NPR.text); }
         | NCR { $operators.add($NCR.text); }
         )
         expression_negation )*;

expression_negation
       : (NEGATIVE_MINUS)? expression_power_root;

expression_power_root returns [ List<String> operators ]
@init { _localctx.operators = new ArrayList<String>(); }
       : expression_postfix (
         ( POWER { $operators.add($POWER.text); }
         | NROOT { $operators.add($NROOT.text); }
         )
         expression_postfix )*;

expression_postfix returns [ List<String> operators ]
@init { _localctx.operators = new ArrayList<String>(); }
       : ( expression_preeval
         | IMAGINARY+)
         ( SQUARED   { $operators.add($SQUARED.text); }
         | FACTORIAL { $operators.add($FACTORIAL.text); }
         | CUBED     { $operators.add($CUBED.text); }
           // TODO: Add other postfix operators
         )*
       ;

expression_imaginary                 // Imaginary part must be treated as a separate postfix operator -> otherwise associativity won't be correct
       : IMAGINARY* expression_postfix
       | IMAGINARY+
       ;

expression_preeval              // Helper rule to simplify decisions
       : expression_prefix
       | expression_value
     //  | expression_imaginary
       ;

expression_prefix returns [ String operator ]
       : op = ( SQUARE_ROOT
              | CUBIC_ROOT
              | NOT
            // TODO: Add more prefix operators
            // TODO: Add identifier functions
            // TODO: Make sure that closing parentheses are only allowed when there is an opening parenthesis
          ) { $operator = $op.text; }
          expression_xor (RIGHT_PARENTHESIS)?     // Allow only at last XOR, since conversion operators cannot be combined in another expression
          ;

expression_value
       : LEFT_PARENTHESIS expression RIGHT_PARENTHESIS?
       | numericalValue
         // TODO: Implement other data types
       ;


/* END Code for operator precedence */

statement
       : controlFlowStatement
       | callStatement
       | storeStatement
       ;

controlFlowStatement                      // Separated controlFlowStatement to allowing blocking of control flow statements in visitor
       : ifStatement
       | thenStatement
       | elseStatement
       | whileStatement
       | repeatStatement
       | endStatement
       | forStatement
       | labelStatement
       | gotoStatement;

ifStatement
       : IF expression;

thenStatement
       : THEN;

elseStatement
       : ELSE;

endStatement
       : END;

whileStatement
       : WHILE expression;

repeatStatement
       : REPEAT expression;

forStatement
       : FOR LEFT_PARENTHESIS numericalVariable COMMA expression COMMA expression (COMMA expression)? (RIGHT_PARENTHESIS)?;

labelStatement
       : LABEL labelIdentifier;

gotoStatement
       : GOTO labelIdentifier;

callStatement
       : DISP expression;

storeStatement
       : expression STORE numericalVariable;

numericalValue
       : numericalVariable
       | number
       | lastResult;


/* Main Token */

SEPARATOR: ':';

// Arithmetic infix operators:
PLUS: '+';
MINUS: '-';
MULTIPLY: '*';
DIVIDE: '/';
NPR: 'nPr';
NCR: 'nCr';
POWER: '^';
NROOT: '×√';
// Relational operators:
GREATER_OR_EQUAL: '≥';
GREATER_THAN: '>';
LESS_THAN: '<';
LESS_OR_EQUAL: '≤';
EQUALS: '=';
NOT_EQUALS: '≠';
// Boolean Operators:
AND: 'and';
OR: 'or';
XOR: 'xor';
NOT: 'not(';            // Prefix!
// Prefix operators
NEGATIVE_MINUS: '‾';                      // TI-Basic forces its own minus symbol - the regular MINUS is not allowed!
SQUARE_ROOT: '√(';
CUBIC_ROOT: '∛(';

// Postfix operators
FACTORIAL: '!';
SQUARED: '²';
CUBED: '³';
// Conversion operators
TO_FRAC: '►Frac';
TO_DEC: '►Dec';


LEFT_PARENTHESIS: '(';
RIGHT_PARENTHESIS: ')';
COMMA: ',';
STORE: '->' | '→';
IMAGINARY: 'i';
DIGIT: '0' .. '9';
DOT: '.';
THETA: 'θ';
CAPITAL_LETTER: 'A' .. 'Z';

labelIdentifier: (CAPITAL_LETTER | THETA | DIGIT) (CAPITAL_LETTER | THETA | DIGIT)?;
numericalVariable: CAPITAL_LETTER | THETA;
lastResult: 'Ans';

/* Parser rule for detecting numbers */
digits: DIGIT+;     // Helper rule to get the token

number returns [
    String preDecimal, String decimal
] :  NEGATIVE_MINUS?
     digits? { $preDecimal = $digits.text; }
     DOT?
     digits { $decimal = $digits.text; }
     ;

/* Skip whitespace */

WS : [ \t\r\n ]+ -> skip;

/* Control commands */

IF: 'If';
THEN: 'Then';
ELSE: 'Else';
FOR: 'For';
WHILE: 'While';
REPEAT: 'Repeat';
END: 'End';
PAUSE: 'Pause';
LABEL: 'Lbl';
GOTO: 'Goto';
IS: 'IS>(';
DS: 'DS<(';
MENU: 'Menu(';
PRGM: 'prgm';
RETURN: 'Return';
STOP: 'Stop';
DELVAR: 'DelVar';
GRAPHSTYLE: 'GraphStyle(';

/* I/O commands */

INPUT: 'Input';
PROMPT: 'Prompt';
DISP: 'Disp';
DISPGRAPH: 'DispGraph';
DISPTABLE: 'DispTable';
OUTPUT: 'Output';
GETKEY: 'getKey';
CLRHOME: 'ClrHome';
CLRTABLE: 'ClrTable';
GETCALC: 'GetCalc(';
GET: 'Get(';
SEND: 'Send(';