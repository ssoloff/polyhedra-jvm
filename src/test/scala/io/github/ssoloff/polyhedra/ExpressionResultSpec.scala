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

import org.scalatest.{FunSpec, Matchers}

final class ExpressionResultSpec extends FunSpec with Matchers with Dice with EqualsVerifierSugar {
  val three = new ConstantExpressionResult(3.0)
  val four = new ConstantExpressionResult(4.0)

  describe("AdditionExpressionResult") {
    it("should be equatable") {
      instancesOf [AdditionExpressionResult] should be (equatable) // scalastyle:ignore no.whitespace.before.left.bracket
    }

    describe("#value") {
      it("should return sum") {
        val sum = 7.0
        val expressionResult = new AdditionExpressionResult(sum, four, three)

        expressionResult.value should equal (sum)
      }
    }
  }

  describe("ArrayExpressionResult") {
    it("should be equatable") {
      (
        instancesOf [ArrayExpressionResult[_]] // scalastyle:ignore no.whitespace.before.left.bracket
          withPrefabValues(
            List[ExpressionResult[_]](three, four),
            List[ExpressionResult[_]](four, three)
          )
          should be (equatable)
      )
    }

    describe("#value") {
      it("should return array of expression result values") {
        val expressionResult = new ArrayExpressionResult(List(three, four))

        expressionResult.value should equal (List(three.value, four.value))
      }
    }
  }

  describe("ConstantExpressionResult") {
    val constant = 42.0

    it("should be equatable") {
      instancesOf [ConstantExpressionResult] should be (equatable) // scalastyle:ignore no.whitespace.before.left.bracket
    }

    describe("#value") {
      it("should return constant") {
        val expressionResult = new ConstantExpressionResult(constant)

        expressionResult.value should equal (constant)
      }
    }
  }

  describe("DieExpressionResult") {
    it("should be equatable") {
      (
        instancesOf [DieExpressionResult] // scalastyle:ignore no.whitespace.before.left.bracket
          withPrefabValues(createDie(1), createDie(2))
          should be (equatable)
      )
    }

    describe("#value") {
      it("should return die") {
        val sides = 6
        val die = createDie(sides)
        val expressionResult = new DieExpressionResult(die)

        expressionResult.value.sides should equal (sides)
      }
    }
  }

  describe("DivisionExpressionResult") {
    it("should be equatable") {
      instancesOf [DivisionExpressionResult] should be (equatable) // scalastyle:ignore no.whitespace.before.left.bracket
    }

    describe("#value") {
      it("should return quotient") {
        val quotient = 0.75
        val expressionResult = new DivisionExpressionResult(quotient, three, four)

        expressionResult.value should equal (quotient)
      }
    }
  }

  describe("FunctionCallExpressionResult") {
    it("should be equatable") {
      (
        instancesOf [FunctionCallExpressionResult[_]] // scalastyle:ignore no.whitespace.before.left.bracket
          withPrefabValues(
            List(three, four),
            List(four, three)
          )
          should be (equatable)
      )
    }

    describe("#value") {
      it("should return function return value") {
        val returnValue = 2.0
        val expressionResult = new FunctionCallExpressionResult(returnValue, "name", List(three, four))

        expressionResult.value should equal (returnValue)
      }
    }
  }

  describe("GroupExpressionResult") {
    it("should be equatable") {
      instancesOf [GroupExpressionResult[_]] should be (equatable) // scalastyle:ignore no.whitespace.before.left.bracket
    }

    describe("#value") {
      it("should return child expression value") {
        val expressionResult = new GroupExpressionResult(three)

        expressionResult.value should equal (three.value)
      }
    }
  }

  describe("ModuloExpressionResult") {
    it("should be equatable") {
      instancesOf [ModuloExpressionResult] should be (equatable) // scalastyle:ignore no.whitespace.before.left.bracket
    }

    describe("#value") {
      it("should return remainder") {
        val remainder = 1.0
        val expressionResult = new ModuloExpressionResult(remainder, four, three)

        expressionResult.value should equal (remainder)
      }
    }
  }

  describe("MultiplicationExpressionResult") {
    it("should be equatable") {
      instancesOf [MultiplicationExpressionResult] should be (equatable) // scalastyle:ignore no.whitespace.before.left.bracket
    }

    describe("#value") {
      it("should return product") {
        val product = 12.0
        val expressionResult = new MultiplicationExpressionResult(product, four, three)

        expressionResult.value should equal (product)
      }
    }
  }

  describe("NegativeExpressionResult") {
    it("should be equatable") {
      instancesOf [NegativeExpressionResult] should be (equatable) // scalastyle:ignore no.whitespace.before.left.bracket
    }

    describe("#value") {
      it("should return negative of child expression value") {
        val expressionResult = new NegativeExpressionResult(three)

        expressionResult.value should equal (-three.value)
      }
    }
  }

  describe("PositiveExpressionResult") {
    it("should be equatable") {
      instancesOf [PositiveExpressionResult] should be (equatable) // scalastyle:ignore no.whitespace.before.left.bracket
    }

    describe("#value") {
      it("should return child expression value") {
        val expressionResult = new PositiveExpressionResult(three)

        expressionResult.value should equal (three.value)
      }
    }
  }

  describe("SubtractionExpressionResult") {
    it("should be equatable") {
      instancesOf [SubtractionExpressionResult] should be (equatable) // scalastyle:ignore no.whitespace.before.left.bracket
    }

    describe("#value") {
      it("should return difference") {
        val difference = 1.0
        val expressionResult = new SubtractionExpressionResult(difference, four, three)

        expressionResult.value should equal (difference)
      }
    }
  }
}

