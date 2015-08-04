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

final class ExpressionSpec extends FunSpec with Matchers with RandomNumberGenerators with EqualsVerifierSugar {
  val three = new ConstantExpression(3.0)
  val four = new ConstantExpression(4.0)

  describe("AdditionExpression") {
    it("should be equatable") {
      instancesOf [AdditionExpression] should be (equatable) // scalastyle:ignore no.whitespace.before.left.bracket
    }

    describe("#evaluate") {
      it("should return result with value equal to sum of augend and addend") {
        val expression = new AdditionExpression(four, three)

        val expressionResult = expression.evaluate()

        expressionResult should equal (new AdditionExpressionResult(
          7.0,
          new ConstantExpressionResult(4.0),
          new ConstantExpressionResult(3.0)
        ))
      }

      it("should evaluate subexpressions") {
        val expression = new AdditionExpression(new AdditionExpression(four, three), three)

        val expressionResult = expression.evaluate()

        expressionResult.value should equal (10.0)
      }
    }
  }

  describe("ArrayExpression") {
    it("should be equatable") {
      (
        instancesOf [ArrayExpression[_]] // scalastyle:ignore no.whitespace.before.left.bracket
          withPrefabValues(
            List[Expression[_]](three, four),
            List[Expression[_]](four, three)
          )
          should be (equatable)
      )
    }

    describe("#evaluate") {
      it("should return result with value equal to array of expression result values") {
        val expression = new ArrayExpression(List(three, four))

        val expressionResult = expression.evaluate()

        expressionResult should equal (new ArrayExpressionResult(List(
          new ConstantExpressionResult(three.constant),
          new ConstantExpressionResult(four.constant)
        )))
      }
    }
  }

  describe("ConstantExpression") {
    it("should be equatable") {
      instancesOf [ConstantExpression] should be (equatable) // scalastyle:ignore no.whitespace.before.left.bracket
    }

    describe("#evaluate") {
      it("should return result with value equal to constant") {
        val constant = 42.0
        val expression = new ConstantExpression(constant)

        val expressionResult = expression.evaluate()

        expressionResult should equal (new ConstantExpressionResult(constant))
      }
    }
  }

  describe("DieExpression") {
    it("should be equatable") {
      (
        instancesOf [DieExpression] // scalastyle:ignore no.whitespace.before.left.bracket
          withPrefabValues(
            new Die(1, DefaultRandomNumberGenerator),
            new Die(2, DefaultRandomNumberGenerator)
          )
          should be (equatable)
      )
    }

    describe("#evaluate") {
      it("should return result with value equal to die") {
        val die = new Die(2, DefaultRandomNumberGenerator)
        val expression = new DieExpression(die)

        val expressionResult = expression.evaluate()

        expressionResult should equal (new DieExpressionResult(die))
      }
    }
  }

  describe("DivisionExpression") {
    it("should be equatable") {
      instancesOf [DivisionExpression] should be (equatable) // scalastyle:ignore no.whitespace.before.left.bracket
    }

    describe("#evaluate") {
      it("should return result with value equal to quotient of dividend and divisor") {
        val expression = new DivisionExpression(three, four)

        val expressionResult = expression.evaluate()

        expressionResult should equal (new DivisionExpressionResult(
          0.75,
          new ConstantExpressionResult(3.0),
          new ConstantExpressionResult(4.0)
        ))
      }

      it("should evaluate subexpressions") {
        val expression = new DivisionExpression(new DivisionExpression(three, four), four)

        val expressionResult = expression.evaluate()

        expressionResult.value should equal (0.1875)
      }
    }
  }

  describe("GroupExpression") {
    it("should be equatable") {
      instancesOf [GroupExpression[_]] should be (equatable) // scalastyle:ignore no.whitespace.before.left.bracket
    }

    describe("#evaluate") {
      it("should return result with value equal to child expression result value") {
        val expression = new GroupExpression(three)

        val expressionResult = expression.evaluate()

        expressionResult should equal (new GroupExpressionResult(new ConstantExpressionResult(three.constant)))
      }
    }
  }

  describe("ModuloExpression") {
    it("should be equatable") {
      instancesOf [ModuloExpression] should be (equatable) // scalastyle:ignore no.whitespace.before.left.bracket
    }

    describe("#evaluate") {
      it("should return result with value equal to remainder of division of dividend and divisor") {
        val expression = new ModuloExpression(four, three)

        val expressionResult = expression.evaluate()

        expressionResult should equal (new ModuloExpressionResult(
          1.0,
          new ConstantExpressionResult(4.0),
          new ConstantExpressionResult(3.0)
        ))
      }

      it("should evaluate subexpressions") {
        val expression = new ModuloExpression(new ModuloExpression(three, four), three)

        val expressionResult = expression.evaluate()

        expressionResult.value should equal (0.0)
      }
    }
  }

  describe("MultiplicationExpression") {
    it("should be equatable") {
      instancesOf [MultiplicationExpression] should be (equatable) // scalastyle:ignore no.whitespace.before.left.bracket
    }

    describe("#evaluate") {
      it("should return result with value equal to product of multiplicand and multiplier") {
        val expression = new MultiplicationExpression(four, three)

        val expressionResult = expression.evaluate()

        expressionResult should equal (new MultiplicationExpressionResult(
          12.0,
          new ConstantExpressionResult(4.0),
          new ConstantExpressionResult(3.0)
        ))
      }

      it("should evaluate subexpressions") {
        val expression = new MultiplicationExpression(new MultiplicationExpression(four, three), three)

        val expressionResult = expression.evaluate()

        expressionResult.value should equal (36.0)
      }
    }
  }

  describe("NegativeExpression") {
    it("should be equatable") {
      instancesOf [NegativeExpression] should be (equatable) // scalastyle:ignore no.whitespace.before.left.bracket
    }

    describe("#evaluate") {
      it("should return result with value equal to negative of child expression result value") {
        val expression = new NegativeExpression(three)

        val expressionResult = expression.evaluate()

        expressionResult should equal (new NegativeExpressionResult(new ConstantExpressionResult(three.constant)))
      }
    }
  }

  describe("PositiveExpression") {
    it("should be equatable") {
      instancesOf [PositiveExpression] should be (equatable) // scalastyle:ignore no.whitespace.before.left.bracket
    }

    describe("#evaluate") {
      it("should return result with value equal to child expression result value") {
        val expression = new PositiveExpression(three)

        val expressionResult = expression.evaluate()

        expressionResult should equal (new PositiveExpressionResult(new ConstantExpressionResult(three.constant)))
      }
    }
  }

  describe("SubtractionExpression") {
    it("should be equatable") {
      instancesOf [SubtractionExpression] should be (equatable) // scalastyle:ignore no.whitespace.before.left.bracket
    }

    describe("#evaluate") {
      it("should return result with value equal to difference between minuend and subtrahend") {
        val expression = new SubtractionExpression(four, three)

        val expressionResult = expression.evaluate()

        expressionResult should equal (new SubtractionExpressionResult(
          1.0,
          new ConstantExpressionResult(4.0),
          new ConstantExpressionResult(3.0)
        ))
      }

      it("should evaluate subexpressions") {
        val expression = new SubtractionExpression(new SubtractionExpression(four, three), three)

        val expressionResult = expression.evaluate()

        expressionResult.value should equal (-2.0)
      }
    }
  }
}

