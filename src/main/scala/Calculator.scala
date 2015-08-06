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

import org.antlr.v4.runtime.{ANTLRInputStream, BaseErrorListener, CommonTokenStream}

object Calculator {
  // $COVERAGE-OFF$
  private[this] class CalculatorBaseVisitorImpl extends CalculatorBaseVisitor[Double] {
    import scala.collection.mutable.HashMap

    private[this] val variables = new HashMap[String, Double]

    override def visitPlus(ctx: CalculatorParser.PlusContext): Double =
      visit(ctx.plusOrMinus()) + visit(ctx.multOrDiv())

    override def visitMinus(ctx: CalculatorParser.MinusContext): Double =
      visit(ctx.plusOrMinus()) - visit(ctx.multOrDiv())

    override def visitMultiplication(ctx: CalculatorParser.MultiplicationContext): Double =
      visit(ctx.multOrDiv()) * visit(ctx.pow())

    override def visitDivision(ctx: CalculatorParser.DivisionContext): Double =
      visit(ctx.multOrDiv()) / visit(ctx.pow())

    override def visitSetVariable(ctx: CalculatorParser.SetVariableContext): Double = {
      val value = visit(ctx.plusOrMinus())
      variables.put(ctx.ID().getText(), value)
      value
    }

    override def visitPower(ctx: CalculatorParser.PowerContext): Double = ctx.pow() match {
      case pow: CalculatorParser.PowContext => Math.pow(visit(ctx.unaryMinus()), visit(pow))
      case _ => visit(ctx.unaryMinus())
    }

    override def visitChangeSign(ctx: CalculatorParser.ChangeSignContext): Double =
      -1.0 * visit(ctx.unaryMinus())

    override def visitBraces(ctx: CalculatorParser.BracesContext): Double =
      visit(ctx.plusOrMinus())

    override def visitConstantPI(ctx: CalculatorParser.ConstantPIContext): Double =
      Math.PI

    override def visitConstantE(ctx: CalculatorParser.ConstantEContext): Double =
      Math.E

    override def visitInt(ctx: CalculatorParser.IntContext): Double =
      ctx.INT().getText().toDouble

    override def visitVariable(ctx: CalculatorParser.VariableContext): Double =
      variables.get(ctx.ID().getText()) match {
        case Some(x) => x
        case _ => Double.NaN
      }

    override def visitDouble(ctx: CalculatorParser.DoubleContext): Double =
      ctx.DOUBLE().getText().toDouble

    override def visitCalculate(ctx: CalculatorParser.CalculateContext): Double =
      visit(ctx.plusOrMinus())
  }
  // $COVERAGE-ON$

  private[this] object ThrowingErrorListener extends BaseErrorListener {
    import org.antlr.v4.runtime.{RecognitionException, Recognizer}
    import org.antlr.v4.runtime.misc.ParseCancellationException

    override def syntaxError(
        recognizer: Recognizer[_, _],
        offendingSymbol: Any,
        line: Int,
        charPositionInLine: Int,
        msg: String,
        e: RecognitionException): Unit =
      throw new ParseCancellationException("line " + line + ":" + charPositionInLine + " " + msg)
  }

  def evaluate(text: String): Double = {
    val input = new ANTLRInputStream(text)

    val lexer = new CalculatorLexer(input)
    lexer.removeErrorListeners()
    lexer.addErrorListener(ThrowingErrorListener)
    val tokens = new CommonTokenStream(lexer)

    val parser = new CalculatorParser(tokens)
    parser.removeErrorListeners()
    parser.addErrorListener(ThrowingErrorListener)
    val tree = parser.input()

    val visitor = new CalculatorBaseVisitorImpl
    visitor.visit(tree)
  }
}

