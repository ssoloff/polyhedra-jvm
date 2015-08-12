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

import io.github.ssoloff.polyhedra.internal.{
  ExpressionBaseVisitor => InternalExpressionBaseVisitor,
  ExpressionLexer => InternalExpressionLexer,
  ExpressionParser => InternalExpressionParser
}
import org.antlr.v4.runtime.{ANTLRInputStream, BaseErrorListener, CommonTokenStream}
import org.antlr.v4.runtime.misc.ParseCancellationException

/** Provides a set of method for parsing dice expressions.
  */
object ExpressionParser {
  // $COVERAGE-OFF$
  private[this] class ExpressionVisitor extends InternalExpressionBaseVisitor[Expression[Double]] {
    override def visitAddition(ctx: InternalExpressionParser.AdditionContext): Expression[Double] =
      new AdditionExpression(visit(ctx.additive_expression()), visit(ctx.literal()))

    override def visitEvaluate(ctx: InternalExpressionParser.EvaluateContext): Expression[Double] =
      visit(ctx.additive_expression())

    override def visitIntegerLiteral(ctx: InternalExpressionParser.IntegerLiteralContext): Expression[Double] =
      new ConstantExpression(ctx.INTEGER_LITERAL().getText().toDouble)

    override def visitSubtraction(ctx: InternalExpressionParser.SubtractionContext): Expression[Double] =
      new SubtractionExpression(visit(ctx.additive_expression()), visit(ctx.literal()))
  }
  // $COVERAGE-ON$

  private[this] object ThrowingErrorListener extends BaseErrorListener {
    import org.antlr.v4.runtime.{RecognitionException, Recognizer}

    override def syntaxError(
        recognizer: Recognizer[_, _],
        offendingSymbol: Any,
        line: Int,
        charPositionInLine: Int,
        msg: String,
        e: RecognitionException): Unit =
      throw new ParseCancellationException("line " + line + ":" + charPositionInLine + " " + msg)
  }

  /** Parses the specified dice expression text.
    *
    * @param source
    *   The dice expression text to parse.
    *
    * @return The parsed expression.
    *
    * @throws java.lang.IllegalArgumentException
    *   If {@code source} is not a valid dice expression.
    */
  def parse(source: String): Expression[Double] = {
    try {
      val input = new ANTLRInputStream(source)

      val lexer = new InternalExpressionLexer(input)
      lexer.removeErrorListeners()
      lexer.addErrorListener(ThrowingErrorListener)
      val tokens = new CommonTokenStream(lexer)

      val parser = new InternalExpressionParser(tokens)
      parser.removeErrorListeners()
      parser.addErrorListener(ThrowingErrorListener)
      val tree = parser.input()

      val visitor = new ExpressionVisitor
      visitor.visit(tree)
    } catch {
      case e: ParseCancellationException => throw new IllegalArgumentException(s"invalid expression '$source'", e)
    }
  }
}

