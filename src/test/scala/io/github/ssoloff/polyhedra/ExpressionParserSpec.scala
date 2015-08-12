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

final class ExpressionParserSpec extends FunSpec with Matchers {
  val one = new ConstantExpression(1.0)
  val two = new ConstantExpression(2.0)

  describe("ExpressionParser") {
    describe(".parse") {
      describe("when source is empty") {
        it("should throw an exception") {
          val source = ""

          an [IllegalArgumentException] should be thrownBy ExpressionParser.parse(source) // scalastyle:ignore no.whitespace.before.left.bracket
        }
      }

      describe("literals") {
        it("should parse an integer literal") {
          val source = "42"

          val expression = ExpressionParser.parse(source)

          expression should equal (new ConstantExpression(42.0))
        }
      }

      describe("arithmetic operators") {
        it("should parse the addition of two constants") {
          val source = "1 + 2"

          val expression = ExpressionParser.parse(source)

          expression should equal (new AdditionExpression(one, two))
        }
      }
    }
  }
}
