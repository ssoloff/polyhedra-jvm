/*
 * Copyright (c) 2015 Steven Soloff
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

grammar Expression;

DIE_LITERAL     : 'd'([1-9][0-9]*|'%');
IDENTIFIER      : [_A-Za-z][_0-9A-Za-z]*;
INTEGER_LITERAL : [0-9]+;
WS              : [ \t\r\n]+ -> skip;

COMMA        : ',';
LPAREN       : '(';
LSQUAREBRACE : '[';
MINUS        : '-';
PERCENT      : '%';
PLUS         : '+';
RPAREN       : ')';
RSQUAREBRACE : ']';
SLASH        : '/';
STAR         : '*';

additive_expression
    : additive_expression PLUS multiplicative_expression  # Addition
    | additive_expression MINUS multiplicative_expression # Subtraction
    | multiplicative_expression                           # ToMultiplicativeExpression
    ;

array_literal
    : LSQUAREBRACE expression_list RSQUAREBRACE # ArrayLiteral
    ;

expression
    : additive_expression # ToAdditiveExpression
    ;

expression_list
    : /* empty */                      # EmptyExpressionList
    | expression                       # SingleElementExpressionList
    | expression_list COMMA expression # MultiElementExpressionList
    ;

function_call
    : IDENTIFIER LPAREN expression_list RPAREN # FunctionCall
    ;

literal
    : DIE_LITERAL     # DieLiteral
    | INTEGER_LITERAL # IntegerLiteral
    ;

multiplicative_expression
    : multiplicative_expression STAR unary_expression    # Multiplication
    | multiplicative_expression SLASH unary_expression   # Division
    | multiplicative_expression PERCENT unary_expression # Modulo
    | unary_expression                                   # ToUnaryExpression
    ;

primary_expression
    : literal                  # ToLiteral
    | array_literal            # ToArrayLiteral
    | function_call            # ToFunctionCall
    | LPAREN expression RPAREN # Group
    ;

program
    : expression EOF
    ;

unary_expression
    : PLUS primary_expression  # Positive
    | MINUS primary_expression # Negative
    | primary_expression       # ToPrimaryExpression
    ;

