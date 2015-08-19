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

package io.github.ssoloff.polyhedra

import scala.util.Try

/** Provides a set of operations for using the dice notation library.
  */
object Polyhedra {
  /** Evaluates a dice expression.
    *
    * @param expressionText
    *   The dice expression to evaluate.
    * @param expressionParserContext
    *   The expression parser context.  If not specified, the default
    *   expression parser context will be used.
    *
    * @return The value of the evaluated expression if successful or an
    *   exception if the expression could not be evaluated.
    */
  def evaluate(
      expressionText: String,
      expressionParserContext: ExpressionParser.Context = ExpressionParser.DefaultContext
      ): Try[Double] = {
    val expression = Try(ExpressionParser.parse(expressionText, expressionParserContext))
    val expressionResult = expression map (_.evaluate())
    expressionResult map (_.value.asInstanceOf[Double])
  }
}

