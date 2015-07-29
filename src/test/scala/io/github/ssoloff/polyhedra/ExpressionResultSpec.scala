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

final class ExpressionResultSpec extends FunSpec with Matchers {
  val three = new ConstantExpressionResult(3.0)
  val four = new ConstantExpressionResult(4.0)

  describe("AdditionExpressionResult") {
    describe("#value") {
      it("should return sum") {
        val sum = 7.0
        val expressionResult = new AdditionExpressionResult(sum, four, three)

        expressionResult.value should equal (sum)
      }
    }
  }

  describe("ConstantExpressionResult") {
    val constant = 42.0

    describe("#value") {
      it("should return constant") {
        val expressionResult = new ConstantExpressionResult(constant)

        expressionResult.value should equal (constant)
      }
    }
  }

  describe("MultiplicationExpressionResult") {
    describe("#value") {
      it("should return product") {
        val product = 12.0
        val expressionResult = new MultiplicationExpressionResult(product, four, three)

        expressionResult.value should equal (product)
      }
    }
  }

  describe("SubtractionExpressionResult") {
    describe("#value") {
      it("should return difference") {
        val difference = 1.0
        val expressionResult = new SubtractionExpressionResult(difference, four, three)

        expressionResult.value should equal (difference)
      }
    }
  }
}

