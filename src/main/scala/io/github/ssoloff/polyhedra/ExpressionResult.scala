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

/** A dice expression result.
  *
  * @tparam A
  *   The type of the evaluated expression value.
  */
sealed abstract class ExpressionResult[A] {
  /** The value of the evaluated expression.
    */
  val value: A
}

/** The result of an expression that adds two expressions.
  *
  * @constructor Creates a new addition expression result.
  *
  * @param sum
  *   The sum of the augend and addend.
  * @param augendExpressionResult
  *   The augend expression result.
  * @param addendExpressionResult
  *   The addend expression result.
  */
final class AdditionExpressionResult(
    sum: Int,
    val augendExpressionResult: ExpressionResult[Int],
    val addendExpressionResult: ExpressionResult[Int]) extends ExpressionResult[Int] {
  override val value: Int = sum
}

/** An result of a constant expression.
  *
  * @constructor Creates a new constant expression result.
  *
  * @param constant
  *   The constant.
  */
final class ConstantExpressionResult(val constant: Int) extends ExpressionResult[Int] {
  override val value: Int = constant
}

