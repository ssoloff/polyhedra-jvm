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
  */
sealed abstract class Expression {
  /** Evaluates the expression.
    *
    * @return The result of evaluating the expression.
    */
  def evaluate(): ExpressionResult
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
    val augendExpression: Expression,
    val addendExpression: Expression) extends Expression {
  override def evaluate(): AdditionExpressionResult = {
    val augendExpressionResult = augendExpression.evaluate()
    val addendExpressionResult = addendExpression.evaluate()
    val sum = augendExpressionResult.value.asInstanceOf[Int] + addendExpressionResult.value.asInstanceOf[Int]
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
final class ConstantExpression(val constant: Int) extends Expression {
  override def evaluate(): ConstantExpressionResult = new ConstantExpressionResult(constant)
}

