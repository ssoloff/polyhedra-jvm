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

INTEGER_LITERAL : [0-9]+;
WS              : [ \t\r\n]+ -> skip;

MINUS   : '-';
PERCENT : '%';
PLUS    : '+';
SLASH   : '/';
STAR    : '*';

additive_expression
    : additive_expression PLUS multiplicative_expression  # Addition
    | additive_expression MINUS multiplicative_expression # Subtraction
    | multiplicative_expression                           # ToMultiplicativeExpression
    ;

input
    : additive_expression EOF # Evaluate
    ;

literal
    : INTEGER_LITERAL # IntegerLiteral
    ;

multiplicative_expression
    : multiplicative_expression STAR unary_expression    # Multiplication
    | multiplicative_expression SLASH unary_expression   # Division
    | multiplicative_expression PERCENT unary_expression # Modulo
    | unary_expression                                   # ToUnaryExpression
    ;

unary_expression
    : PLUS literal  # Positive
    | MINUS literal # Negative
    | literal       # ToLiteral
    ;

