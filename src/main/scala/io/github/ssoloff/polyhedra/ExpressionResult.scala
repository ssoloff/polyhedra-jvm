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

import org.apache.commons.lang3.builder.HashCodeBuilder

/** A dice expression result.
  *
  * @tparam A
  *   The type of the evaluated expression value.
  */
sealed abstract class ExpressionResult[+A] {
  /** The value of the evaluated expression.
    */
  def value: A
}

/** The result of an expression that adds two expressions.
  *
  * @constructor Creates a new addition expression result.
  *
  * @param sum
  *   The sum of the augend and the addend.
  * @param augendExpressionResult
  *   The augend expression result.
  * @param addendExpressionResult
  *   The addend expression result.
  */
final class AdditionExpressionResult(
    sum: Double,
    val augendExpressionResult: ExpressionResult[Double],
    val addendExpressionResult: ExpressionResult[Double]) extends ExpressionResult[Double] {
  override def equals(other: Any): Boolean = other match {
    case that: AdditionExpressionResult => addendExpressionResult == that.addendExpressionResult &&
      augendExpressionResult == that.augendExpressionResult &&
      value.compareTo(that.value) == 0
    case _ => false
  }

  override def hashCode(): Int = new HashCodeBuilder()
    .append(addendExpressionResult)
    .append(augendExpressionResult)
    .append(value)
    .toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "AdditionExpressionResult(" +
    s"addendExpressionResult=$addendExpressionResult" +
    s", augendExpressionResult=$augendExpressionResult" +
    s", value=$value" +
    ")"
  // $COVERAGE-ON$

  override val value: Double = sum
}

/** The result of an array expression.
  *
  * @tparam A
  *   The type of the evaluated expression value of each array element.
  *
  * @constructor Creates a new array expression result.
  *
  * @param expressionResults
  *   The array of expression results.
  */
final class ArrayExpressionResult[A](val expressionResults: Seq[ExpressionResult[A]]) extends ExpressionResult[Seq[A]] {
  override def equals(other: Any): Boolean = {
    other match {
      case that: ArrayExpressionResult[A] => expressionResults == that.expressionResults
      case _ => false
    }
  }

  override def hashCode(): Int = new HashCodeBuilder().append(expressionResults).toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "ArrayExpressionResult(" +
    s"expressionResults=$expressionResults" +
    s", value=$value" +
    ")"
  // $COVERAGE-ON$

  override val value: Seq[A] = expressionResults.map(_.value)
}

/** An result of a constant expression.
  *
  * @constructor Creates a new constant expression result.
  *
  * @param constant
  *   The constant.
  */
final class ConstantExpressionResult(constant: Double) extends ExpressionResult[Double] {
  override def equals(other: Any): Boolean = other match {
    case that: ConstantExpressionResult => value.compareTo(that.value) == 0
    case _ => false
  }

  override def hashCode(): Int = new HashCodeBuilder().append(value).toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "ConstantExpressionResult(" +
    s"value=$value" +
    ")"
  // $COVERAGE-ON$

  override val value: Double = constant
}

/** The result of a die expression.
  *
  * @constructor Creates a new die expression result.
  *
  * @param die
  *
  */
final class DieExpressionResult(die: Die) extends ExpressionResult[Die] {
  override def equals(other: Any): Boolean = other match {
    case that: DieExpressionResult => value.sides == that.value.sides
    case _ => false
  }

  override def hashCode(): Int = new HashCodeBuilder().append(value.sides).toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "DieExpressionResult(" +
    s"value=$value" +
    ")"
  // $COVERAGE-ON$

  override val value: Die = die
}

/** The result of an expression that divides two expressions.
  *
  * @constructor Creates a new division expression result.
  *
  * @param quotient
  *   The quotient of the dividend and the divisor.
  * @param dividendExpressionResult
  *   The dividend expression result.
  * @param divisorExpressionResult
  *   The divisor expression result.
  */
final class DivisionExpressionResult(
    quotient: Double,
    val dividendExpressionResult: ExpressionResult[Double],
    val divisorExpressionResult: ExpressionResult[Double]) extends ExpressionResult[Double] {
  override def equals(other: Any): Boolean = other match {
    case that: DivisionExpressionResult => dividendExpressionResult == that.dividendExpressionResult &&
      divisorExpressionResult == that.divisorExpressionResult &&
      value.compareTo(that.value) == 0
    case _ => false
  }

  override def hashCode(): Int = new HashCodeBuilder()
    .append(dividendExpressionResult)
    .append(divisorExpressionResult)
    .append(value)
    .toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "DivisionExpressionResult(" +
    s"dividendExpressionResult=$dividendExpressionResult" +
    s", divisorExpressionResult=$divisorExpressionResult" +
    s", value=$value" +
    ")"
  // $COVERAGE-ON$

  override val value: Double = quotient
}

/** The result of an expression that calls a function.
  *
  * @tparam A
  *   The type of the evaluated expression value.
  *
  * @constructor Creates a new function expression result.
  *
  * @param returnValue
  *   The function return value.
  * @param name
  *   The function name.
  * @param argumentListExpressionResults
  *   The expression results that represent the arguments to the function call.
  */
final class FunctionCallExpressionResult[A](
    returnValue: A,
    val name: String,
    val argumentListExpressionResults: Seq[ExpressionResult[_]]) extends ExpressionResult[A] {
  override def equals(other: Any): Boolean = other match {
    case that: FunctionCallExpressionResult[A] => argumentListExpressionResults == that.argumentListExpressionResults &&
      name == that.name &&
      value == that.value
    case _ => false
  }

  override def hashCode(): Int = new HashCodeBuilder()
    .append(argumentListExpressionResults)
    .append(name)
    .append(value)
    .toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "FunctionCallExpressionResult(" +
    s"argumentListExpressionResults=$argumentListExpressionResults" +
    s", name=$name" +
    s", value=$value" +
    ")"
  // $COVERAGE-ON$

  override val value: A = returnValue
}

/** The result of an expression that groups another expression.
  *
  * @tparam A
  *   The type of the evaluated expression value.
  *
  * @constructor Creates a new group expression result.
  *
  * @param childExpressionResult
  *   The result of the expression to be grouped.
  */
final class GroupExpressionResult[A](val childExpressionResult: ExpressionResult[A]) extends ExpressionResult[A] {
  override def equals(other: Any): Boolean = other match {
    case that: GroupExpressionResult[A] => childExpressionResult == that.childExpressionResult
    case _ => false
  }

  override def hashCode(): Int = new HashCodeBuilder().append(childExpressionResult).toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "GroupExpressionResult(" +
    s"childExpressionResult=$childExpressionResult" +
    s", value=$value" +
    ")"
  // $COVERAGE-ON$

  override val value: A = childExpressionResult.value
}

/** The result of an expression that modulos two expressions.
  *
  * @constructor Creates a new modulo expression result.
  *
  * @param remainder
  *   The remainder of the division of the dividend and the divisor.
  * @param dividendExpressionResult
  *   The dividend expression result.
  * @param divisorExpressionResult
  *   The divisor expression result.
  */
final class ModuloExpressionResult(
    remainder: Double,
    val dividendExpressionResult: ExpressionResult[Double],
    val divisorExpressionResult: ExpressionResult[Double]) extends ExpressionResult[Double] {
  override def equals(other: Any): Boolean = other match {
    case that: ModuloExpressionResult => dividendExpressionResult == that.dividendExpressionResult &&
      divisorExpressionResult == that.divisorExpressionResult &&
      value.compareTo(that.value) == 0
    case _ => false
  }

  override def hashCode(): Int = new HashCodeBuilder()
    .append(dividendExpressionResult)
    .append(divisorExpressionResult)
    .append(value)
    .toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "ModuloExpressionResult(" +
    s"dividendExpressionResult=$dividendExpressionResult" +
    s", divisorExpressionResult=$divisorExpressionResult" +
    s", value=$value" +
    ")"
  // $COVERAGE-ON$

  override val value: Double = remainder
}

/** The result of an expression that multiplies two expressions.
  *
  * @constructor Creates a new multiplication expression result.
  *
  * @param product
  *   The product of the multiplicand and the multiplier.
  * @param multiplicandExpressionResult
  *   The multiplicand expression result.
  * @param multiplierExpressionResult
  *   The multiplier expression result.
  */
final class MultiplicationExpressionResult(
    product: Double,
    val multiplicandExpressionResult: ExpressionResult[Double],
    val multiplierExpressionResult: ExpressionResult[Double]) extends ExpressionResult[Double] {
  override def equals(other: Any): Boolean = other match {
    case that: MultiplicationExpressionResult => multiplicandExpressionResult == that.multiplicandExpressionResult &&
      multiplierExpressionResult == that.multiplierExpressionResult &&
      value.compareTo(that.value) == 0
    case _ => false
  }

  override def hashCode(): Int = new HashCodeBuilder()
    .append(multiplicandExpressionResult)
    .append(multiplierExpressionResult)
    .append(value)
    .toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "MultiplicationExpressionResult(" +
    s"multiplicandExpressionResult=$multiplicandExpressionResult" +
    s", multiplierExpressionResult=$multiplierExpressionResult" +
    s", value=$value" +
    ")"
  // $COVERAGE-ON$

  override val value: Double = product
}

/** The result of an expression that negates another expression.
  *
  * @constructor Creates a new negative expression result.
  *
  * @param childExpressionResult
  *   The result of the expression to be negated.
  */
final class NegativeExpressionResult(val childExpressionResult: ExpressionResult[Double]) extends ExpressionResult[Double] {
  override def equals(other: Any): Boolean = other match {
    case that: NegativeExpressionResult => childExpressionResult == that.childExpressionResult
    case _ => false
  }

  override def hashCode(): Int = new HashCodeBuilder().append(childExpressionResult).toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "NegativeExpressionResult(" +
    s"childExpressionResult=$childExpressionResult" +
    s", value=$value" +
    ")"
  // $COVERAGE-ON$

  override val value: Double = -childExpressionResult.value
}

/** The result of an expression that applies another expression.
  *
  * @constructor Creates a new positive expression result.
  *
  * @param childExpressionResult
  *   The result of the expression to be applied.
  */
final class PositiveExpressionResult(val childExpressionResult: ExpressionResult[Double]) extends ExpressionResult[Double] {
  override def equals(other: Any): Boolean = other match {
    case that: PositiveExpressionResult => childExpressionResult == that.childExpressionResult
    case _ => false
  }

  override def hashCode(): Int = new HashCodeBuilder().append(childExpressionResult).toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "PositiveExpressionResult(" +
    s"childExpressionResult=$childExpressionResult" +
    s", value=$value" +
    ")"
  // $COVERAGE-ON$

  override val value: Double = childExpressionResult.value
}

/** The result of an expression that subtracts two expressions.
  *
  * @constructor Creates a new subtraction expression result.
  *
  * @param difference
  *   The difference between the minuend and the subtrahend.
  * @param minuendExpressionResult
  *   The minuend expression result.
  * @param subtrahendExpressionResult
  *   The subtrahend expression result.
  */
final class SubtractionExpressionResult(
    difference: Double,
    val minuendExpressionResult: ExpressionResult[Double],
    val subtrahendExpressionResult: ExpressionResult[Double]) extends ExpressionResult[Double] {
  override def equals(other: Any): Boolean = other match {
    case that: SubtractionExpressionResult => minuendExpressionResult == that.minuendExpressionResult &&
      subtrahendExpressionResult == that.subtrahendExpressionResult &&
      value.compareTo(that.value) == 0
    case _ => false
  }

  override def hashCode(): Int = new HashCodeBuilder()
    .append(minuendExpressionResult)
    .append(subtrahendExpressionResult)
    .append(value)
    .toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "SubtractionExpressionResult(" +
    s"minuendExpressionResult=$minuendExpressionResult" +
    s", subtrahendExpressionResult=$subtrahendExpressionResult" +
    s", value=$value" +
    ")"
  // $COVERAGE-ON$

  override val value: Double = difference
}

