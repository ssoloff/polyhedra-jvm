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
  val three = new ConstantExpression(3.0)
  val bag = new Bag

  describe("ExpressionParser") {
    describe("#parse") {
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

        it("should parse an array literal with zero elements") {
          val source = "[]"

          val expression = ExpressionParser.parse(source)

          expression should equal (new ArrayExpression(Nil))
        }

        it("should parse an array literal with one element") {
          val source = "[1]"

          val expression = ExpressionParser.parse(source)

          expression should equal (new ArrayExpression(List(one)))
        }

        it("should parse an array literal with two elements") {
          val source = "[1, 2]"

          val expression = ExpressionParser.parse(source)

          expression should equal (new ArrayExpression(List(one, two)))
        }

        it("should parse a die literal") {
          val source = "d6"

          val expression = ExpressionParser.parse(source)

          expression should equal (new DieExpression(bag.d(6))) // scalastyle:ignore magic.number
        }

        it("should parse a percentile die literal") {
          val source = "d%"

          val expression = ExpressionParser.parse(source)

          expression should equal (new DieExpression(bag.d(100))) // scalastyle:ignore magic.number
        }
      }

      describe("arithmetic operators") {
        it("should parse the addition of two constants") {
          val source = "1 + 2"

          val expression = ExpressionParser.parse(source)

          expression should equal (new AdditionExpression(one, two))
        }

        it("should parse the subtraction of two constants") {
          val source = "1 - 2"

          val expression = ExpressionParser.parse(source)

          expression should equal (new SubtractionExpression(one, two))
        }

        it("should parse the multiplication of two constants") {
          val source = "1 * 2"

          val expression = ExpressionParser.parse(source)

          expression should equal (new MultiplicationExpression(one, two))
        }

        it("should parse the division of two constants") {
          val source = "1 / 2"

          val expression = ExpressionParser.parse(source)

          expression should equal (new DivisionExpression(one, two))
        }

        it("should parse the modulo of two constants") {
          val source = "3 % 2"

          val expression = ExpressionParser.parse(source)

          expression should equal (new ModuloExpression(three, two))
        }
      }

      describe("unary operators") {
        it("should parse negative") {
          val source = "-1"

          val expression = ExpressionParser.parse(source)

          expression should equal (new NegativeExpression(one))
        }

        it("should parse positive") {
          val source = "+1"

          val expression = ExpressionParser.parse(source)

          expression should equal (new PositiveExpression(one))
        }
      }

      describe("operator precedence") {
        it("should give precedence to multiplication over addition") {
          val source = "3 * 1 + 1 * 3"

          val expression = ExpressionParser.parse(source)

          expression should equal (
            new AdditionExpression(
              new MultiplicationExpression(three, one),
              new MultiplicationExpression(one, three)
            )
          )
        }

        it("should allow grouping to override operator precedence") {
          ExpressionParser.parse("(3 * 1) + (1 * 3)") should equal (
            new AdditionExpression(
              new GroupExpression(new MultiplicationExpression(three, one)),
              new GroupExpression(new MultiplicationExpression(one, three))
            )
          )
          ExpressionParser.parse("3 * (1 + 1) * 3") should equal (
            new MultiplicationExpression(
              new MultiplicationExpression(
                three,
                new GroupExpression(new AdditionExpression(one, one))
              ),
              three
            )
          )
          ExpressionParser.parse("3 * ((1 + 1) * 3)") should equal (
            new MultiplicationExpression(
              three,
              new GroupExpression(
                new MultiplicationExpression(
                  new GroupExpression(new AdditionExpression(one, one)),
                  three
                )
              )
            )
          )
          ExpressionParser.parse("(3 * (1 + 1)) * 3") should equal (
            new MultiplicationExpression(
              new GroupExpression(
                new MultiplicationExpression(
                  three,
                  new GroupExpression(new AdditionExpression(one, one))
                )
              ),
              three
            )
          )
        }
      }

      describe("function calls") {
        val f = (args: Seq[_]) => args.asInstanceOf[Seq[Double]].sum

        describe("when function is unknown") {
          it("should throw an exception") {
            val source = "f()"

            an [IllegalArgumentException] should be thrownBy (ExpressionParser.parse(source)) // scalastyle:ignore no.whitespace.before.left.bracket
          }
        }

        it("should parse a function call with zero arguments") {
          val source = "f()"
          val context = new ExpressionParser.Context(bag, Map("f" -> f))

          val expression = ExpressionParser.parse(source, context)

          expression should equal (new FunctionCallExpression("f", f, Nil))
        }

        it("should parse a function call with one argument") {
          val source = "f(1)"
          val context = new ExpressionParser.Context(bag, Map("f" -> f))

          val expression = ExpressionParser.parse(source, context)

          expression should equal (new FunctionCallExpression("f", f, List(one)))
        }

        it("should parse a function call with two arguments") {
          val source = "f(1, 2)"
          val context = new ExpressionParser.Context(bag, Map("f" -> f))

          val expression = ExpressionParser.parse(source, context)

          expression should equal (new FunctionCallExpression("f", f, List(one, two)))
        }
      }

      describe("built-in functions") {
        describe("when context contains a function with the same name as a built-in function") {
          it("should use the function from the context") {
            val source = "ceil(1)"
            val ceil = (args: Seq[_]) => 42.0
            val context = new ExpressionParser.Context(bag, Map("ceil" -> ceil))

            val expression = ExpressionParser.parse(source, context)

            expression.evaluate().value should equal (42.0)
          }
        }

        it("should parse the built-in ceil() function") {
          val source = "ceil(1)"

          val expression = ExpressionParser.parse(source)

          expression should equal (new FunctionCallExpression("ceil", ExpressionFunctions.ceil, List(one)))
        }

        it("should parse the built-in floor() function") {
          val source = "floor(1)"

          val expression = ExpressionParser.parse(source)

          expression should equal (new FunctionCallExpression("floor", ExpressionFunctions.floor, List(one)))
        }

        it("should parse the built-in roll() function") {
          val source = "roll(3, d6)"

          val expression = ExpressionParser.parse(source)

          expression should equal (new FunctionCallExpression("roll", ExpressionFunctions.roll, List(
            three,
            new DieExpression(bag.d(6)) // scalastyle:ignore magic.number
          )))
        }

        it("should parse the built-in round() function") {
          val source = "round(1)"

          val expression = ExpressionParser.parse(source)

          expression should equal (new FunctionCallExpression("round", ExpressionFunctions.round, List(one)))
        }

        it("should parse the built-in trunc() function") {
          val source = "trunc(1)"

          val expression = ExpressionParser.parse(source)

          expression should equal (new FunctionCallExpression("trunc", ExpressionFunctions.trunc, List(one)))
        }
      }
    }
  }
}

