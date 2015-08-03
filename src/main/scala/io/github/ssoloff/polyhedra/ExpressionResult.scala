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
sealed abstract class ExpressionResult[+A] {
  /** The value of the evaluated expression.
    */
  val value: A
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
  override val value: Double = sum

  override def equals(other: Any): Boolean = other match {
    case that: AdditionExpressionResult => value.compareTo(that.value) == 0 &&
      augendExpressionResult == that.augendExpressionResult &&
      addendExpressionResult == that.addendExpressionResult
    case _ => false
  }

  override def hashCode(): Int = {
    // scalastyle:off magic.number
    var hashCode = 17
    hashCode = 31 * hashCode + value.hashCode
    hashCode = 31 * hashCode + augendExpressionResult.hashCode
    hashCode = 31 * hashCode + addendExpressionResult.hashCode
    hashCode
    // scalastyle:on magic.number
  }

  // $COVERAGE-OFF$
  override def toString: String = "AdditionExpressionResult(" +
    s"value=$value" +
    s", augendExpressionResult=$augendExpressionResult" +
    s", addendExpressionResult=$addendExpressionResult" +
    ")"
  // $COVERAGE-ON$
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
final class ArrayExpressionResult[A](val expressionResults: List[ExpressionResult[A]]) extends ExpressionResult[List[A]] {
  override val value: List[A] = expressionResults.map(_.value)

  override def equals(other: Any): Boolean = {
    other match {
      case that: ArrayExpressionResult[A] => value == that.value &&
        expressionResults == that.expressionResults
      case _ => false
    }
  }

  override def hashCode(): Int = {
    // scalastyle:off magic.number
    var hashCode = 17
    hashCode = 31 * hashCode + value.hashCode
    hashCode = 31 * hashCode + expressionResults.hashCode
    hashCode
    // scalastyle:on magic.number
  }

  // $COVERAGE-OFF$
  override def toString: String = "ArrayExpressionResult(" +
    s"value=$value" +
    s", expressionResults=$expressionResults" +
    ")"
  // $COVERAGE-ON$
}

/** An result of a constant expression.
  *
  * @constructor Creates a new constant expression result.
  *
  * @param constant
  *   The constant.
  */
final class ConstantExpressionResult(constant: Double) extends ExpressionResult[Double] {
  override val value: Double = constant

  override def equals(other: Any): Boolean = other match {
    case that: ConstantExpressionResult => value.compareTo(that.value) == 0
    case _ => false
  }

  override def hashCode(): Int = {
    // scalastyle:off magic.number
    var hashCode = 17
    hashCode = 31 * hashCode + value.hashCode
    hashCode
    // scalastyle:on magic.number
  }

  // $COVERAGE-OFF$
  override def toString: String = "ConstantExpressionResult(" +
    s"value=$value" +
    ")"
  // $COVERAGE-ON$
}

/** The result of a die expression.
  *
  * @constructor Creates a new die expression result.
  *
  * @param die
  *
  */
final class DieExpressionResult(die: Die) extends ExpressionResult[Die] {
  override val value: Die = die

  override def equals(other: Any): Boolean = other match {
    case that: DieExpressionResult => value.sides == that.value.sides
    case _ => false
  }

  override def hashCode(): Int = {
    // scalastyle:off magic.number
    var hashCode = 17
    hashCode = 31 * hashCode + value.sides.hashCode
    hashCode
    // scalastyle:on magic.number
  }

  // $COVERAGE-OFF$
  override def toString: String = "DieExpressionResult(" +
    s"value=$value" +
    ")"
  // $COVERAGE-ON$
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
  override val value: Double = quotient

  override def equals(other: Any): Boolean = other match {
    case that: DivisionExpressionResult => value.compareTo(that.value) == 0 &&
      dividendExpressionResult == that.dividendExpressionResult &&
      divisorExpressionResult == that.divisorExpressionResult
    case _ => false
  }

  override def hashCode(): Int = {
    // scalastyle:off magic.number
    var hashCode = 17
    hashCode = 31 * hashCode + value.hashCode
    hashCode = 31 * hashCode + dividendExpressionResult.hashCode
    hashCode = 31 * hashCode + divisorExpressionResult.hashCode
    hashCode
    // scalastyle:on magic.number
  }

  // $COVERAGE-OFF$
  override def toString: String = "DivisionExpressionResult(" +
    s"value=$value" +
    s", dividendExpressionResult=$dividendExpressionResult" +
    s", divisorExpressionResult=$divisorExpressionResult" +
    ")"
  // $COVERAGE-ON$
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
  override val value: A = childExpressionResult.value

  override def equals(other: Any): Boolean = other match {
    case that: GroupExpressionResult[A] => value == that.value &&
      childExpressionResult == that.childExpressionResult
    case _ => false
  }

  override def hashCode(): Int = {
    // scalastyle:off magic.number
    var hashCode = 17
    hashCode = 31 * hashCode + value.hashCode
    hashCode = 31 * hashCode + childExpressionResult.hashCode
    hashCode
    // scalastyle:on magic.number
  }

  // $COVERAGE-OFF$
  override def toString: String = "GroupExpressionResult(" +
    s"value=$value" +
    s", childExpressionResult=$childExpressionResult" +
    ")"
  // $COVERAGE-ON$
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
  override val value: Double = remainder

  override def equals(other: Any): Boolean = other match {
    case that: ModuloExpressionResult => value.compareTo(that.value) == 0 &&
      dividendExpressionResult == that.dividendExpressionResult &&
      divisorExpressionResult == that.divisorExpressionResult
    case _ => false
  }

  override def hashCode(): Int = {
    // scalastyle:off magic.number
    var hashCode = 17
    hashCode = 31 * hashCode + value.hashCode
    hashCode = 31 * hashCode + dividendExpressionResult.hashCode
    hashCode = 31 * hashCode + divisorExpressionResult.hashCode
    hashCode
    // scalastyle:on magic.number
  }

  // $COVERAGE-OFF$
  override def toString: String = "ModuloExpressionResult(" +
    s"value=$value" +
    s", dividendExpressionResult=$dividendExpressionResult" +
    s", divisorExpressionResult=$divisorExpressionResult" +
    ")"
  // $COVERAGE-ON$
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
  override val value: Double = product

  override def equals(other: Any): Boolean = other match {
    case that: MultiplicationExpressionResult => value.compareTo(that.value) == 0 &&
      multiplicandExpressionResult == that.multiplicandExpressionResult &&
      multiplierExpressionResult == that.multiplierExpressionResult
    case _ => false
  }

  override def hashCode(): Int = {
    // scalastyle:off magic.number
    var hashCode = 17
    hashCode = 31 * hashCode + value.hashCode
    hashCode = 31 * hashCode + multiplicandExpressionResult.hashCode
    hashCode = 31 * hashCode + multiplierExpressionResult.hashCode
    hashCode
    // scalastyle:on magic.number
  }

  // $COVERAGE-OFF$
  override def toString: String = "MultiplicationExpressionResult(" +
    s"value=$value" +
    s", multiplicandExpressionResult=$multiplicandExpressionResult" +
    s", multiplierExpressionResult=$multiplierExpressionResult" +
    ")"
  // $COVERAGE-ON$
}

/** The result of an expression that negates another expression.
  *
  * @constructor Creates a new negative expression result.
  *
  * @param childExpressionResult
  *   The result of the expression to be negated.
  */
final class NegativeExpressionResult(val childExpressionResult: ExpressionResult[Double]) extends ExpressionResult[Double] {
  override val value: Double = -childExpressionResult.value

  override def equals(other: Any): Boolean = other match {
    case that: NegativeExpressionResult => value.compareTo(that.value) == 0 &&
      childExpressionResult == that.childExpressionResult
    case _ => false
  }

  override def hashCode(): Int = {
    // scalastyle:off magic.number
    var hashCode = 17
    hashCode = 31 * hashCode + value.hashCode
    hashCode = 31 * hashCode + childExpressionResult.hashCode
    hashCode
    // scalastyle:on magic.number
  }

  // $COVERAGE-OFF$
  override def toString: String = "NegativeExpressionResult(" +
    s"value=$value" +
    s", childExpressionResult=$childExpressionResult" +
    ")"
  // $COVERAGE-ON$
}

/** The result of an expression that applies another expression.
  *
  * @constructor Creates a new positive expression result.
  *
  * @param childExpressionResult
  *   The result of the expression to be applied.
  */
final class PositiveExpressionResult(val childExpressionResult: ExpressionResult[Double]) extends ExpressionResult[Double] {
  override val value: Double = childExpressionResult.value

  override def equals(other: Any): Boolean = other match {
    case that: PositiveExpressionResult => value.compareTo(that.value) == 0 &&
      childExpressionResult == that.childExpressionResult
    case _ => false
  }

  override def hashCode(): Int = {
    // scalastyle:off magic.number
    var hashCode = 17
    hashCode = 31 * hashCode + value.hashCode
    hashCode = 31 * hashCode + childExpressionResult.hashCode
    hashCode
    // scalastyle:on magic.number
  }

  // $COVERAGE-OFF$
  override def toString: String = "PositiveExpressionResult(" +
    s"value=$value" +
    s", childExpressionResult=$childExpressionResult" +
    ")"
  // $COVERAGE-ON$
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
  override val value: Double = difference

  override def equals(other: Any): Boolean = other match {
    case that: SubtractionExpressionResult => value.compareTo(that.value) == 0 &&
      minuendExpressionResult == that.minuendExpressionResult &&
      subtrahendExpressionResult == that.subtrahendExpressionResult
    case _ => false
  }

  override def hashCode(): Int = {
    // scalastyle:off magic.number
    var hashCode = 17
    hashCode = 31 * hashCode + value.hashCode
    hashCode = 31 * hashCode + minuendExpressionResult.hashCode
    hashCode = 31 * hashCode + subtrahendExpressionResult.hashCode
    hashCode
    // scalastyle:on magic.number
  }

  // $COVERAGE-OFF$
  override def toString: String = "SubtractionExpressionResult(" +
    s"value=$value" +
    s", minuendExpressionResult=$minuendExpressionResult" +
    s", subtrahendExpressionResult=$subtrahendExpressionResult" +
    ")"
  // $COVERAGE-ON$
}

