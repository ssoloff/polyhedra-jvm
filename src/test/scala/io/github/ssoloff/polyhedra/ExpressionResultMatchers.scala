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

import org.scalatest.matchers.{MatchResult, Matcher}
import scala.util.Try

/** Trait that provides custom matchers for expression results.
  */
trait ExpressionResultMatchers {
  private[this] final class AdditionExpressionResultMatcher(expected: AdditionExpressionResult) extends Matcher[ExpressionResult[_]] {
    override def apply(left: ExpressionResult[_]): MatchResult = {
      MatchResult(
        Try {
          val actual = left.asInstanceOf[AdditionExpressionResult]
          actual.value == expected.value &&
            beExpressionResult(expected.augendExpressionResult).apply(actual.augendExpressionResult).matches &&
            beExpressionResult(expected.addendExpressionResult).apply(actual.addendExpressionResult).matches
        } getOrElse false,
        s"$left is not $expected",
        s"$left is $expected"
      )
    }
  }

  private[this] final class ConstantExpressionResultMatcher(expected: ConstantExpressionResult) extends Matcher[ExpressionResult[_]] {
    override def apply(left: ExpressionResult[_]): MatchResult = {
      MatchResult(
        Try {
          val actual = left.asInstanceOf[ConstantExpressionResult]
          actual.value == expected.value
        } getOrElse false,
        s"$left is not $expected",
        s"$left is $expected"
      )
    }
  }

  private[this] final class DivisionExpressionResultMatcher(expected: DivisionExpressionResult) extends Matcher[ExpressionResult[_]] {
    override def apply(left: ExpressionResult[_]): MatchResult = {
      MatchResult(
        Try {
          val actual = left.asInstanceOf[DivisionExpressionResult]
          actual.value == expected.value &&
            beExpressionResult(expected.dividendExpressionResult).apply(actual.dividendExpressionResult).matches &&
            beExpressionResult(expected.divisorExpressionResult).apply(actual.divisorExpressionResult).matches
        } getOrElse false,
        s"$left is not $expected",
        s"$left is $expected"
      )
    }
  }

  private[this] final class ModuloExpressionResultMatcher(expected: ModuloExpressionResult) extends Matcher[ExpressionResult[_]] {
    override def apply(left: ExpressionResult[_]): MatchResult = {
      MatchResult(
        Try {
          val actual = left.asInstanceOf[ModuloExpressionResult]
          actual.value == expected.value &&
            beExpressionResult(expected.dividendExpressionResult).apply(actual.dividendExpressionResult).matches &&
            beExpressionResult(expected.divisorExpressionResult).apply(actual.divisorExpressionResult).matches
        } getOrElse false,
        s"$left is not $expected",
        s"$left is $expected"
      )
    }
  }

  private[this] final class MultiplicationExpressionResultMatcher(expected: MultiplicationExpressionResult) extends Matcher[ExpressionResult[_]] {
    override def apply(left: ExpressionResult[_]): MatchResult = {
      MatchResult(
        Try {
          val actual = left.asInstanceOf[MultiplicationExpressionResult]
          actual.value == expected.value &&
            beExpressionResult(expected.multiplicandExpressionResult).apply(actual.multiplicandExpressionResult).matches &&
            beExpressionResult(expected.multiplierExpressionResult).apply(actual.multiplierExpressionResult).matches
        } getOrElse false,
        s"$left is not $expected",
        s"$left is $expected"
      )
    }
  }

  private[this] final class SubtractionExpressionResultMatcher(expected: SubtractionExpressionResult) extends Matcher[ExpressionResult[_]] {
    override def apply(left: ExpressionResult[_]): MatchResult = {
      MatchResult(
        Try {
          val actual = left.asInstanceOf[SubtractionExpressionResult]
          actual.value == expected.value &&
            beExpressionResult(expected.minuendExpressionResult).apply(actual.minuendExpressionResult).matches &&
            beExpressionResult(expected.subtrahendExpressionResult).apply(actual.subtrahendExpressionResult).matches
        } getOrElse false,
        s"$left is not $expected",
        s"$left is $expected"
      )
    }
  }

  /** This method enables syntax such as the following:
    *
    * <pre>
    * result should beExpressionResult (new ConstantExpressionResult(42.0))
    * </pre>
    */
  val beExpressionResult: PartialFunction[ExpressionResult[_], Matcher[ExpressionResult[_]]] = {
    case x: AdditionExpressionResult => new AdditionExpressionResultMatcher(x)
    case x: ConstantExpressionResult => new ConstantExpressionResultMatcher(x)
    case x: DivisionExpressionResult => new DivisionExpressionResultMatcher(x)
    case x: ModuloExpressionResult => new ModuloExpressionResultMatcher(x)
    case x: MultiplicationExpressionResult => new MultiplicationExpressionResultMatcher(x)
    case x: SubtractionExpressionResult => new SubtractionExpressionResultMatcher(x)
  }
}

