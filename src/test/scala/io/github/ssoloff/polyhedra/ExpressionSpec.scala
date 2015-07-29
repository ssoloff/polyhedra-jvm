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

final class ExpressionSpec extends FunSpec with Matchers {
  val three = new ConstantExpression(3.0)
  val four = new ConstantExpression(4.0) // scalastyle:ignore magic.number

  describe("AdditionExpression") {
    describe("#evaluate") {
      it("should return result with value equal to sum of augend and addend") {
        val expression = new AdditionExpression(four, three)

        val expressionResult = expression.evaluate()

        expressionResult.value should equal (7.0) // scalastyle:ignore magic.number
      }

      it("should evaluate subexpressions") {
        val expression = new AdditionExpression(new AdditionExpression(four, three), three)

        val expressionResult = expression.evaluate()

        expressionResult.value should equal (10.0) // scalastyle:ignore magic.number
      }
    }
  }

  describe("ConstantExpression") {
    val constant = 42.0

    describe("#evaluate") {
      it("should return result with value equal to constant") {
        val expression = new ConstantExpression(constant)

        val expressionResult = expression.evaluate()

        expressionResult.value should equal (constant)
      }
    }
  }

  describe("SubtractionExpression") {
    describe("#evaluate") {
      it("should return result with value equal to difference between minuend and subtrahend") {
        val expression = new SubtractionExpression(four, three)

        val expressionResult = expression.evaluate()

        expressionResult.value should equal (1.0)
      }

      it("should evaluate subexpressions") {
        val expression = new SubtractionExpression(new SubtractionExpression(four, three), three)

        val expressionResult = expression.evaluate()

        expressionResult.value should equal (-2.0) // scalastyle:ignore magic.number
      }
    }
  }
}

