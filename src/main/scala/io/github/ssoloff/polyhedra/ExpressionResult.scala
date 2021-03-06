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

import java.util.Objects

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
  * @param augendExpressionResult
  *   The augend expression result.
  * @param addendExpressionResult
  *   The addend expression result.
  */
final class AdditionExpressionResult(
    val augendExpressionResult: ExpressionResult[Double],
    val addendExpressionResult: ExpressionResult[Double]) extends ExpressionResult[Double] {
  override def equals(other: Any): Boolean = other match {
    case that: AdditionExpressionResult => addendExpressionResult == that.addendExpressionResult &&
      augendExpressionResult == that.augendExpressionResult
    case _ => false
  }

  override def hashCode(): Int = Objects.hash(
    addendExpressionResult,
    augendExpressionResult
  )

  // $COVERAGE-OFF$
  override def toString: String = "AdditionExpressionResult(" +
    s"addendExpressionResult=$addendExpressionResult" +
    s", augendExpressionResult=$augendExpressionResult" +
    s", value=$value" +
    ")"
  // $COVERAGE-ON$

  lazy val value: Double = augendExpressionResult.value + addendExpressionResult.value
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

  override def hashCode(): Int = Objects.hash(expressionResults)

  // $COVERAGE-OFF$
  override def toString: String = "ArrayExpressionResult(" +
    s"expressionResults=$expressionResults" +
    s", value=$value" +
    ")"
  // $COVERAGE-ON$

  lazy val value: Seq[A] = expressionResults.map(_.value)
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

  override def hashCode(): Int = Objects.hash(Double.box(value))

  // $COVERAGE-OFF$
  override def toString: String = "ConstantExpressionResult(" +
    s"value=$value" +
    ")"
  // $COVERAGE-ON$

  val value: Double = constant
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
    case that: DieExpressionResult => value == that.value
    case _ => false
  }

  override def hashCode(): Int = Objects.hash(value)

  // $COVERAGE-OFF$
  override def toString: String = "DieExpressionResult(" +
    s"value=$value" +
    ")"
  // $COVERAGE-ON$

  val value: Die = die
}

/** The result of an expression that divides two expressions.
  *
  * @constructor Creates a new division expression result.
  *
  * @param dividendExpressionResult
  *   The dividend expression result.
  * @param divisorExpressionResult
  *   The divisor expression result.
  */
final class DivisionExpressionResult(
    val dividendExpressionResult: ExpressionResult[Double],
    val divisorExpressionResult: ExpressionResult[Double]) extends ExpressionResult[Double] {
  override def equals(other: Any): Boolean = other match {
    case that: DivisionExpressionResult => dividendExpressionResult == that.dividendExpressionResult &&
      divisorExpressionResult == that.divisorExpressionResult
    case _ => false
  }

  override def hashCode(): Int = Objects.hash(
    dividendExpressionResult,
    divisorExpressionResult
  )

  // $COVERAGE-OFF$
  override def toString: String = "DivisionExpressionResult(" +
    s"dividendExpressionResult=$dividendExpressionResult" +
    s", divisorExpressionResult=$divisorExpressionResult" +
    s", value=$value" +
    ")"
  // $COVERAGE-ON$

  lazy val value: Double = dividendExpressionResult.value / divisorExpressionResult.value
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

  override def hashCode(): Int = Objects.hash(
    argumentListExpressionResults,
    name,
    value.asInstanceOf[AnyRef]
  )

  // $COVERAGE-OFF$
  override def toString: String = "FunctionCallExpressionResult(" +
    s"argumentListExpressionResults=$argumentListExpressionResults" +
    s", name=$name" +
    s", value=$value" +
    ")"
  // $COVERAGE-ON$

  val value: A = returnValue
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

  override def hashCode(): Int = Objects.hash(childExpressionResult)

  // $COVERAGE-OFF$
  override def toString: String = "GroupExpressionResult(" +
    s"childExpressionResult=$childExpressionResult" +
    s", value=$value" +
    ")"
  // $COVERAGE-ON$

  lazy val value: A = childExpressionResult.value
}

/** The result of an expression that modulos two expressions.
  *
  * @constructor Creates a new modulo expression result.
  *
  * @param dividendExpressionResult
  *   The dividend expression result.
  * @param divisorExpressionResult
  *   The divisor expression result.
  */
final class ModuloExpressionResult(
    val dividendExpressionResult: ExpressionResult[Double],
    val divisorExpressionResult: ExpressionResult[Double]) extends ExpressionResult[Double] {
  override def equals(other: Any): Boolean = other match {
    case that: ModuloExpressionResult => dividendExpressionResult == that.dividendExpressionResult &&
      divisorExpressionResult == that.divisorExpressionResult
    case _ => false
  }

  override def hashCode(): Int = Objects.hash(
    dividendExpressionResult,
    divisorExpressionResult
  )

  // $COVERAGE-OFF$
  override def toString: String = "ModuloExpressionResult(" +
    s"dividendExpressionResult=$dividendExpressionResult" +
    s", divisorExpressionResult=$divisorExpressionResult" +
    s", value=$value" +
    ")"
  // $COVERAGE-ON$

  lazy val value: Double = dividendExpressionResult.value % divisorExpressionResult.value
}

/** The result of an expression that multiplies two expressions.
  *
  * @constructor Creates a new multiplication expression result.
  *
  * @param multiplicandExpressionResult
  *   The multiplicand expression result.
  * @param multiplierExpressionResult
  *   The multiplier expression result.
  */
final class MultiplicationExpressionResult(
    val multiplicandExpressionResult: ExpressionResult[Double],
    val multiplierExpressionResult: ExpressionResult[Double]) extends ExpressionResult[Double] {
  override def equals(other: Any): Boolean = other match {
    case that: MultiplicationExpressionResult => multiplicandExpressionResult == that.multiplicandExpressionResult &&
      multiplierExpressionResult == that.multiplierExpressionResult
    case _ => false
  }

  override def hashCode(): Int = Objects.hash(
    multiplicandExpressionResult,
    multiplierExpressionResult
  )

  // $COVERAGE-OFF$
  override def toString: String = "MultiplicationExpressionResult(" +
    s"multiplicandExpressionResult=$multiplicandExpressionResult" +
    s", multiplierExpressionResult=$multiplierExpressionResult" +
    s", value=$value" +
    ")"
  // $COVERAGE-ON$

  lazy val value: Double = multiplicandExpressionResult.value * multiplierExpressionResult.value
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

  override def hashCode(): Int = Objects.hash(childExpressionResult)

  // $COVERAGE-OFF$
  override def toString: String = "NegativeExpressionResult(" +
    s"childExpressionResult=$childExpressionResult" +
    s", value=$value" +
    ")"
  // $COVERAGE-ON$

  lazy val value: Double = -childExpressionResult.value
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

  override def hashCode(): Int = Objects.hash(childExpressionResult)

  // $COVERAGE-OFF$
  override def toString: String = "PositiveExpressionResult(" +
    s"childExpressionResult=$childExpressionResult" +
    s", value=$value" +
    ")"
  // $COVERAGE-ON$

  lazy val value: Double = childExpressionResult.value
}

/** The result of an expression that subtracts two expressions.
  *
  * @constructor Creates a new subtraction expression result.
  *
  * @param minuendExpressionResult
  *   The minuend expression result.
  * @param subtrahendExpressionResult
  *   The subtrahend expression result.
  */
final class SubtractionExpressionResult(
    val minuendExpressionResult: ExpressionResult[Double],
    val subtrahendExpressionResult: ExpressionResult[Double]) extends ExpressionResult[Double] {
  override def equals(other: Any): Boolean = other match {
    case that: SubtractionExpressionResult => minuendExpressionResult == that.minuendExpressionResult &&
      subtrahendExpressionResult == that.subtrahendExpressionResult
    case _ => false
  }

  override def hashCode(): Int = Objects.hash(
    minuendExpressionResult,
    subtrahendExpressionResult
  )

  // $COVERAGE-OFF$
  override def toString: String = "SubtractionExpressionResult(" +
    s"minuendExpressionResult=$minuendExpressionResult" +
    s", subtrahendExpressionResult=$subtrahendExpressionResult" +
    s", value=$value" +
    ")"
  // $COVERAGE-ON$

  lazy val value: Double = minuendExpressionResult.value - subtrahendExpressionResult.value
}

