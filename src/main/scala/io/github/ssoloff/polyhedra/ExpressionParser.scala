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
  private[this] class ExpressionVisitor(context: Context) extends InternalExpressionBaseVisitor[Expression[_]] {
    /** Returns the function with the specified name.
      *
      * <p>
      * This method first looks up the function in the expression parser
      * context.  If it does not exist, it then looks up the function in the
      * collection of built-in functions.  Otherwise, it throws an exception.
      * </p>
      *
      * @param name
      *   The function name.
      *
      * @return The function.
      *
      * @throws java.lang.IllegalArgumentException
      *   If a function with the specified name does not exist.
      */
    private[this] def lookupFunction(name: String): Seq[_] => _ =
      context.functions.get(name)
        .orElse(ExpressionFunctions(name))
        .getOrElse(throw new IllegalArgumentException(s"unknown function '$name'"))

    override def visitAddition(ctx: InternalExpressionParser.AdditionContext): Expression[Double] =
      new AdditionExpression(
        visit(ctx.additive_expression()).asInstanceOf[Expression[Double]],
        visit(ctx.multiplicative_expression()).asInstanceOf[Expression[Double]]
      )

    override def visitArrayLiteral(ctx: InternalExpressionParser.ArrayLiteralContext): ArrayExpression[_] =
      visit(ctx.expression_list()).asInstanceOf[ArrayExpression[_]]

    override def visitDieLiteral(ctx: InternalExpressionParser.DieLiteralContext): DieExpression = {
      val literal = ctx.DIE_LITERAL().getText()
      val sides = literal.tail match {
        case "%" => 100 // scalastyle:ignore magic.number
        case x => x.toInt
      }
      new DieExpression(context.bag.d(sides))
    }

    override def visitDivision(ctx: InternalExpressionParser.DivisionContext): Expression[Double] =
      new DivisionExpression(
        visit(ctx.multiplicative_expression()).asInstanceOf[Expression[Double]],
        visit(ctx.unary_expression()).asInstanceOf[Expression[Double]]
      )

    override def visitEmptyExpressionList(ctx: InternalExpressionParser.EmptyExpressionListContext): ArrayExpression[_] =
      new ArrayExpression(Nil)

    override def visitFunctionCall(ctx: InternalExpressionParser.FunctionCallContext): Expression[_] = {
      val name = ctx.IDENTIFIER().getText()
      val func = lookupFunction(name)
      val argumentListExpressions = visit(ctx.expression_list()).asInstanceOf[ArrayExpression[_]].expressions
      new FunctionCallExpression(name, func, argumentListExpressions)
    }

    override def visitGroup(ctx: InternalExpressionParser.GroupContext): Expression[_] =
      new GroupExpression(visit(ctx.expression()))

    override def visitIntegerLiteral(ctx: InternalExpressionParser.IntegerLiteralContext): Expression[Double] =
      new ConstantExpression(ctx.INTEGER_LITERAL().getText().toDouble)

    override def visitModulo(ctx: InternalExpressionParser.ModuloContext): Expression[Double] =
      new ModuloExpression(
        visit(ctx.multiplicative_expression()).asInstanceOf[Expression[Double]],
        visit(ctx.unary_expression()).asInstanceOf[Expression[Double]]
      )

    override def visitMultiElementExpressionList(ctx: InternalExpressionParser.MultiElementExpressionListContext): ArrayExpression[_] = {
      val front = visit(ctx.expression_list()).asInstanceOf[ArrayExpression[_]]
      new ArrayExpression(front.expressions :+ visit(ctx.expression()))
    }

    override def visitMultiplication(ctx: InternalExpressionParser.MultiplicationContext): Expression[Double] =
      new MultiplicationExpression(
        visit(ctx.multiplicative_expression()).asInstanceOf[Expression[Double]],
        visit(ctx.unary_expression()).asInstanceOf[Expression[Double]]
      )

    override def visitNegative(ctx: InternalExpressionParser.NegativeContext): Expression[Double] =
      new NegativeExpression(visit(ctx.primary_expression()).asInstanceOf[Expression[Double]])

    override def visitPositive(ctx: InternalExpressionParser.PositiveContext): Expression[Double] =
      new PositiveExpression(visit(ctx.primary_expression()).asInstanceOf[Expression[Double]])

    override def visitProgram(ctx: InternalExpressionParser.ProgramContext): Expression[_] =
      visit(ctx.expression())

    override def visitSingleElementExpressionList(ctx: InternalExpressionParser.SingleElementExpressionListContext): ArrayExpression[_] =
      new ArrayExpression(List(visit(ctx.expression())))

    override def visitSubtraction(ctx: InternalExpressionParser.SubtractionContext): Expression[Double] =
      new SubtractionExpression(
        visit(ctx.additive_expression()).asInstanceOf[Expression[Double]],
        visit(ctx.multiplicative_expression()).asInstanceOf[Expression[Double]]
      )
  }

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

  /** The execution context for an expression parser.
    *
    * @constructor Creates a new expression parser context.
    *
    * @param bag
    *   The dice bag used by the parser whenever a die literal is encountered.
    * @param functions
    *   A map used by the parser to lookup function implementations when a
    *   function call is encountered.  The functions in this object override
    *   any function with the same name in the parser's default function list.
    */
  final class Context(
    val bag: Bag,
    val functions: Map[String, Seq[_] => _]
  )

  /** Parses the specified dice expression text.
    *
    * @param source
    *   The dice expression text to parse.
    * @param context
    *   The expression parser context.  If not specified, uses a default dice
    *   bag and includes no additional function implementations.
    *
    * @return The parsed expression.
    *
    * @throws java.lang.IllegalArgumentException
    *   If {@code source} is not a valid dice expression.
    */
  def parse(
      source: String,
      context: Context = new Context(new Bag, Map())
      ): Expression[_] = {
    try {
      val input = new ANTLRInputStream(source)

      val lexer = new InternalExpressionLexer(input)
      lexer.removeErrorListeners()
      lexer.addErrorListener(ThrowingErrorListener)
      val tokens = new CommonTokenStream(lexer)

      val parser = new InternalExpressionParser(tokens)
      parser.removeErrorListeners()
      parser.addErrorListener(ThrowingErrorListener)
      val tree = parser.program()

      val visitor = new ExpressionVisitor(context)
      visitor.visit(tree)
    } catch {
      case e: ParseCancellationException => throw new IllegalArgumentException(s"invalid expression '$source'", e)
    }
  }
}

