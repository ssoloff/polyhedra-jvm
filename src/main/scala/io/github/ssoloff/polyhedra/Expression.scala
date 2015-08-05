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

/** A dice expression.
  *
  * @tparam A
  *   The type of the evaluated expression value.
  */
sealed abstract class Expression[+A] {
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
  override def equals(other: Any): Boolean = other match {
    case that: AdditionExpression => addendExpression == that.addendExpression &&
      augendExpression == that.augendExpression
    case _ => false
  }

  override def evaluate(): AdditionExpressionResult = {
    val augendExpressionResult = augendExpression.evaluate()
    val addendExpressionResult = addendExpression.evaluate()
    val sum = augendExpressionResult.value + addendExpressionResult.value
    new AdditionExpressionResult(sum, augendExpressionResult, addendExpressionResult)
  }

  override def hashCode(): Int = new HashCodeBuilder()
    .append(addendExpression)
    .append(augendExpression)
    .toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "AdditionExpression(" +
    s"addendExpression=$addendExpression" +
    s", augendExpression=$augendExpression" +
    ")"
  // $COVERAGE-ON$
}

/** An expression that acts as an array of expressions.
  *
  * @tparam A
  *   The type of the evaluated expression value of each array element.
  *
  * @constructor Creates a new array expression.
  *
  * @param expressions
  *   The expressions that are the array elements.
  */
final class ArrayExpression[A](val expressions: List[Expression[A]]) extends Expression[List[A]] {
  override def equals(other: Any): Boolean = other match {
    case that: ArrayExpression[A] => expressions == that.expressions
    case _ => false
  }

  override def evaluate(): ArrayExpressionResult[A] = {
    new ArrayExpressionResult(expressions.map(_.evaluate()))
  }

  override def hashCode(): Int = new HashCodeBuilder().append(expressions).toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "ArrayExpression(" +
    s"expressions=$expressions" +
    ")"
  // $COVERAGE-ON$
}

/** An expression that represents a constant value.
  *
  * @constructor Creates a new constant expression.
  *
  * @param constant
  *   The constant.
  */
final class ConstantExpression(val constant: Double) extends Expression[Double] {
  override def equals(other: Any): Boolean = other match {
    case that: ConstantExpression => constant.compareTo(that.constant) == 0
    case _ => false
  }

  override def evaluate(): ConstantExpressionResult = new ConstantExpressionResult(constant)

  override def hashCode(): Int = new HashCodeBuilder().append(constant).toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "ConstantExpression(" +
    s"constant=$constant" +
    ")"
  // $COVERAGE-ON$
}

/** An expression that represents a die.
  *
  * @constructor Creates a new die expression.
  *
  * @param die
  *   The die.
  */
final class DieExpression(val die: Die) extends Expression[Die] {
  override def equals(other: Any): Boolean = other match {
    case that: DieExpression => die.sides == that.die.sides
    case _ => false
  }

  override def evaluate(): DieExpressionResult = new DieExpressionResult(die)

  override def hashCode(): Int = new HashCodeBuilder().append(die.sides).toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "DieExpression(" +
    s"die=$die" +
    ")"
  // $COVERAGE-ON$
}

/** An expression that divides two expressions.
  *
  * @constructor Creates a new division expression.
  *
  * @param dividendExpression
  *   The dividend expression.
  * @param divisorExpression
  *   The divisor expression.
  */
final class DivisionExpression(
    val dividendExpression: Expression[Double],
    val divisorExpression: Expression[Double]) extends Expression[Double] {
  override def equals(other: Any): Boolean = other match {
    case that: DivisionExpression => dividendExpression == that.dividendExpression &&
      divisorExpression == that.divisorExpression
    case _ => false
  }

  override def evaluate(): DivisionExpressionResult = {
    val dividendExpressionResult = dividendExpression.evaluate()
    val divisorExpressionResult = divisorExpression.evaluate()
    val quotient = dividendExpressionResult.value / divisorExpressionResult.value
    new DivisionExpressionResult(quotient, dividendExpressionResult, divisorExpressionResult)
  }

  override def hashCode(): Int = new HashCodeBuilder()
    .append(dividendExpression)
    .append(divisorExpression)
    .toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "DivisionExpression(" +
    s"dividendExpression=$dividendExpression" +
    s", divisorExpression=$divisorExpression" +
    ")"
  // $COVERAGE-ON$
}

/** An expression that calls a function.
  *
  * @tparam T
  *   The type of the function arguments.
  * @tparam R
  *   The type of the evaluated expression value.
  *
  * @constructor Creates a new function call expression.
  *
  * @param name
  *   The function name.
  * @param func
  *   The function.
  * @param argumentListExpressions
  *   The expressions that represent the arguments to the function call.
  */
final class FunctionCallExpression[T, R](
    val name: String,
    val func: Seq[T] => R,
    val argumentListExpressions: List[Expression[T]]) extends Expression[R] {
  override def equals(other: Any): Boolean = other match {
    case that: FunctionCallExpression[T, R] => argumentListExpressions == that.argumentListExpressions &&
      func == that.func &&
      name == that.name
    case _ => false
  }

  override def evaluate(): FunctionCallExpressionResult[R] = {
    val argumentListExpressionResults = argumentListExpressions map (_.evaluate())
    val argumentList = argumentListExpressionResults map (_.value)
    val returnValue = func(argumentList)
    new FunctionCallExpressionResult[R](returnValue, name, argumentListExpressionResults)
  }

  override def hashCode(): Int = new HashCodeBuilder()
    .append(argumentListExpressions)
    .append(func)
    .append(name)
    .toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "FunctionCallExpression(" +
    s"argumentListExpressions=$argumentListExpressions" +
    s", func=$func" +
    s", name=$name" +
    ")"
  // $COVERAGE-ON$
}

/** An expression that groups another expression.
  *
  * @tparam A
  *   The type of the evaluated expression value.
  *
  * @constructor Creates a new group expression.
  *
  * @param childExpression
  *   The expression to be grouped.
  */
final class GroupExpression[A](val childExpression: Expression[A]) extends Expression[A] {
  override def equals(other: Any): Boolean = other match {
    case that: GroupExpression[A] => childExpression == that.childExpression
    case _ => false
  }

  override def evaluate(): GroupExpressionResult[A] = {
    new GroupExpressionResult(childExpression.evaluate())
  }

  override def hashCode(): Int = new HashCodeBuilder().append(childExpression).toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "GroupExpression(" +
    s"childExpression=$childExpression" +
    ")"
  // $COVERAGE-ON$
}

/** An expression that modulos two expressions.
  *
  * @constructor Creates a new modulo expression.
  *
  * @param dividendExpression
  *   The dividend expression.
  * @param divisorExpression
  *   The divisor expression.
  */
final class ModuloExpression(
    val dividendExpression: Expression[Double],
    val divisorExpression: Expression[Double]) extends Expression[Double] {
  override def equals(other: Any): Boolean = other match {
    case that: ModuloExpression => dividendExpression == that.dividendExpression &&
      divisorExpression == that.divisorExpression
    case _ => false
  }

  override def evaluate(): ModuloExpressionResult = {
    val dividendExpressionResult = dividendExpression.evaluate()
    val divisorExpressionResult = divisorExpression.evaluate()
    val remainder = dividendExpressionResult.value % divisorExpressionResult.value
    new ModuloExpressionResult(remainder, dividendExpressionResult, divisorExpressionResult)
  }

  override def hashCode(): Int = new HashCodeBuilder()
    .append(dividendExpression)
    .append(divisorExpression)
    .toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "ModuloExpression(" +
    s"dividendExpression=$dividendExpression" +
    s", divisorExpression=$divisorExpression" +
    ")"
  // $COVERAGE-ON$
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
  override def equals(other: Any): Boolean = other match {
    case that: MultiplicationExpression => multiplicandExpression == that.multiplicandExpression &&
      multiplierExpression == that.multiplierExpression
    case _ => false
  }

  override def evaluate(): MultiplicationExpressionResult = {
    val multiplicandExpressionResult = multiplicandExpression.evaluate()
    val multiplierExpressionResult = multiplierExpression.evaluate()
    val product = multiplicandExpressionResult.value * multiplierExpressionResult.value
    new MultiplicationExpressionResult(product, multiplicandExpressionResult, multiplierExpressionResult)
  }

  override def hashCode(): Int = new HashCodeBuilder()
    .append(multiplicandExpression)
    .append(multiplierExpression)
    .toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "MultiplicationExpression(" +
    s"multiplicandExpression=$multiplicandExpression" +
    s", multiplierExpression=$multiplierExpression" +
    ")"
  // $COVERAGE-ON$
}

/** An expression that negates another expression.
  *
  * @constructor Creates a new negative expression.
  *
  * @param childExpression
  *   The expression to be negated.
  */
final class NegativeExpression(val childExpression: Expression[Double]) extends Expression[Double] {
  override def equals(other: Any): Boolean = other match {
    case that: NegativeExpression => childExpression == that.childExpression
    case _ => false
  }

  override def evaluate(): NegativeExpressionResult = {
    new NegativeExpressionResult(childExpression.evaluate())
  }

  override def hashCode(): Int = new HashCodeBuilder().append(childExpression).toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "NegativeExpression(" +
    s"childExpression=$childExpression" +
    ")"
  // $COVERAGE-ON$
}

/** An expression that applies another expression.
  *
  * @constructor Creates a new positive expression.
  *
  * @param childExpression
  *   The expression to be applied.
  */
final class PositiveExpression(val childExpression: Expression[Double]) extends Expression[Double] {
  override def equals(other: Any): Boolean = other match {
    case that: PositiveExpression => childExpression == that.childExpression
    case _ => false
  }

  override def evaluate(): PositiveExpressionResult = {
    new PositiveExpressionResult(childExpression.evaluate())
  }

  override def hashCode(): Int = new HashCodeBuilder().append(childExpression).toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "PositiveExpression(" +
    s"childExpression=$childExpression" +
    ")"
  // $COVERAGE-ON$
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
  override def equals(other: Any): Boolean = other match {
    case that: SubtractionExpression => minuendExpression == that.minuendExpression &&
      subtrahendExpression == that.subtrahendExpression
    case _ => false
  }

  override def evaluate(): SubtractionExpressionResult = {
    val minuendExpressionResult = minuendExpression.evaluate()
    val subtrahendExpressionResult = subtrahendExpression.evaluate()
    val difference = minuendExpressionResult.value - subtrahendExpressionResult.value
    new SubtractionExpressionResult(difference, minuendExpressionResult, subtrahendExpressionResult)
  }

  override def hashCode(): Int = new HashCodeBuilder()
    .append(minuendExpression)
    .append(subtrahendExpression)
    .toHashCode

  // $COVERAGE-OFF$
  override def toString: String = "SubtractionExpression(" +
    s"minuendExpression=$minuendExpression" +
    s", subtrahendExpression=$subtrahendExpression" +
    ")"
  // $COVERAGE-ON$
}

