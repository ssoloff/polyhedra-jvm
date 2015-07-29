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

/** A dice expression.
  *
  * @tparam A
  *   The type of the evaluated expression value.
  */
sealed abstract class Expression[A] {
  /** Evaluates the expression.
    *
    * @return The result of evaluating the expression.
    */
  def evaluate(): ExpressionResult[A]
}

/** An expression that adds two expressions.
  *
  * @constructor Creates a new addition expression.
  *
  * @param augendExpression
  *   The augend expression.
  * @param addendExpression
  *   The addend expression.
  */
final class AdditionExpression(
    val augendExpression: Expression[Double],
    val addendExpression: Expression[Double]) extends Expression[Double] {
  override def evaluate(): AdditionExpressionResult = {
    val augendExpressionResult = augendExpression.evaluate()
    val addendExpressionResult = addendExpression.evaluate()
    val sum = augendExpressionResult.value + addendExpressionResult.value
    new AdditionExpressionResult(sum, augendExpressionResult, addendExpressionResult)
  }
}

/** An expression that represents a constant value.
  *
  * @constructor Creates a new constant expression.
  *
  * @param constant
  *   The constant.
  */
final class ConstantExpression(val constant: Double) extends Expression[Double] {
  override def evaluate(): ConstantExpressionResult = new ConstantExpressionResult(constant)
}

/** An expression that multiplies two expressions.
  *
  * @constructor Creates a new multiplication expression.
  *
  * @param multiplicandExpression
  *   The multiplicand expression.
  * @param multiplierExpression
  *   The multiplier expression.
  */
final class MultiplicationExpression(
    val multiplicandExpression: Expression[Double],
    val multiplierExpression: Expression[Double]) extends Expression[Double] {
  override def evaluate(): MultiplicationExpressionResult = {
    val multiplicandExpressionResult = multiplicandExpression.evaluate()
    val multiplierExpressionResult = multiplierExpression.evaluate()
    val product = multiplicandExpressionResult.value * multiplierExpressionResult.value
    new MultiplicationExpressionResult(product, multiplicandExpressionResult, multiplierExpressionResult)
  }
}

/** An expression that subtracts two expressions.
  *
  * @constructor Creates a new subtraction expression.
  *
  * @param minuendExpression
  *   The minuend expression.
  * @param subtrahendExpression
  *   The subtrahend expression.
  */
final class SubtractionExpression(
    val minuendExpression: Expression[Double],
    val subtrahendExpression: Expression[Double]) extends Expression[Double] {
  override def evaluate(): SubtractionExpressionResult = {
    val minuendExpressionResult = minuendExpression.evaluate()
    val subtrahendExpressionResult = subtrahendExpression.evaluate()
    val difference = minuendExpressionResult.value - subtrahendExpressionResult.value
    new SubtractionExpressionResult(difference, minuendExpressionResult, subtrahendExpressionResult)
  }
}

