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

import nl.jqno.equalsverifier.{EqualsVerifier, Warning}
import org.scalatest.{FunSpec, Matchers}

final class ExpressionResultSpec extends FunSpec with Matchers with RandomNumberGenerators with EqualsVerifierSugar {
  val three = new ConstantExpressionResult(3.0)
  val four = new ConstantExpressionResult(4.0)

  describe("AdditionExpressionResult") {
    it("should be equatable") {
      classOf[AdditionExpressionResult] should be (equatable)
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
      EqualsVerifier.forClass(classOf[ArrayExpressionResult[_]])
        .withPrefabValues(classOf[List[ExpressionResult[_]]], List(three, four), List(four, three))
        .suppress(Warning.NULL_FIELDS)
        .verify()
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
      classOf[ConstantExpressionResult] should be (equatable)
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
      EqualsVerifier.forClass(classOf[DieExpressionResult])
        .withPrefabValues(classOf[Die], new Die(1, DefaultRandomNumberGenerator), new Die(2, DefaultRandomNumberGenerator))
        .suppress(Warning.NULL_FIELDS)
        .verify()
    }

    describe("#value") {
      it("should return die") {
        val sides = 6
        val die = new Die(sides, DefaultRandomNumberGenerator)
        val expressionResult = new DieExpressionResult(die)

        expressionResult.value.sides should equal (sides)
      }
    }
  }

  describe("DivisionExpressionResult") {
    it("should be equatable") {
      classOf[DivisionExpressionResult] should be (equatable)
    }

    describe("#value") {
      it("should return quotient") {
        val quotient = 0.75
        val expressionResult = new DivisionExpressionResult(quotient, three, four)

        expressionResult.value should equal (quotient)
      }
    }
  }

  describe("GroupExpressionResult") {
    it("should be equatable") {
      classOf[GroupExpressionResult[_]] should be (equatable)
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
      classOf[ModuloExpressionResult] should be (equatable)
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
      classOf[MultiplicationExpressionResult] should be (equatable)
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
      classOf[NegativeExpressionResult] should be (equatable)
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
      classOf[PositiveExpressionResult] should be (equatable)
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
      classOf[SubtractionExpressionResult] should be (equatable)
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

