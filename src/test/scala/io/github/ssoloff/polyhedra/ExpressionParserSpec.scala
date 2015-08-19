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

import org.scalatest.{FunSpec, Matchers, TryValues}

final class ExpressionParserSpec extends FunSpec with Matchers with TryValues {
  val one = new ConstantExpression(1.0)
  val two = new ConstantExpression(2.0)
  val three = new ConstantExpression(3.0)
  val bag = new Bag

  describe("ExpressionParser") {
    describe("#parse") {
      describe("when source is empty") {
        it("should return an exception") {
          val source = ""

          ExpressionParser.parse(source).failure.exception shouldBe an [IllegalArgumentException] // scalastyle:ignore no.whitespace.before.left.bracket
        }
      }

      describe("literals") {
        it("should parse an integer literal") {
          val source = "42"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new ConstantExpression(42.0))
        }

        it("should parse an array literal with zero elements") {
          val source = "[]"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new ArrayExpression(Nil))
        }

        it("should parse an array literal with one element") {
          val source = "[1]"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new ArrayExpression(List(one)))
        }

        it("should parse an array literal with two elements") {
          val source = "[1, 2]"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new ArrayExpression(List(one, two)))
        }

        it("should parse a die literal") {
          val source = "d6"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new DieExpression(bag.d(6))) // scalastyle:ignore magic.number
        }

        it("should parse a percentile die literal") {
          val source = "d%"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new DieExpression(bag.d(100))) // scalastyle:ignore magic.number
        }

        it("should parse a dice roll literal") {
          val source = "3d6"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (
            new FunctionCallExpression("sum", ExpressionFunctions.sum, List(
              new FunctionCallExpression("roll", ExpressionFunctions.roll, List(
                new ConstantExpression(3.0),
                new DieExpression(bag.d(6)) // scalastyle:ignore magic.number
              ))
            ))
          )
        }

        it("should parse a percentile dice roll literal") {
          val source = "2d%"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (
            new FunctionCallExpression("sum", ExpressionFunctions.sum, List(
              new FunctionCallExpression("roll", ExpressionFunctions.roll, List(
                new ConstantExpression(2.0),
                new DieExpression(bag.d(100)) // scalastyle:ignore magic.number
              ))
            ))
          )
        }

        it("should parse a dice roll and clone highest literal") {
          ExpressionParser.parse("4d6+H").success.value should equal (
            new FunctionCallExpression("sum", ExpressionFunctions.sum, List(
              new FunctionCallExpression("cloneHighestRolls", ExpressionFunctions.cloneHighestRolls, List(
                new FunctionCallExpression("roll", ExpressionFunctions.roll, List(
                  new ConstantExpression(4.0),
                  new DieExpression(bag.d(6)) // scalastyle:ignore magic.number
                )),
                new ConstantExpression(1.0)
              ))
            ))
          )
          ExpressionParser.parse("4d6+2H").success.value should equal (
            new FunctionCallExpression("sum", ExpressionFunctions.sum, List(
              new FunctionCallExpression("cloneHighestRolls", ExpressionFunctions.cloneHighestRolls, List(
                new FunctionCallExpression("roll", ExpressionFunctions.roll, List(
                  new ConstantExpression(4.0),
                  new DieExpression(bag.d(6)) // scalastyle:ignore magic.number
                )),
                new ConstantExpression(2.0)
              ))
            ))
          )
        }

        it("should parse a dice roll and clone lowest literal") {
          ExpressionParser.parse("4d6+L").success.value should equal (
            new FunctionCallExpression("sum", ExpressionFunctions.sum, List(
              new FunctionCallExpression("cloneLowestRolls", ExpressionFunctions.cloneLowestRolls, List(
                new FunctionCallExpression("roll", ExpressionFunctions.roll, List(
                  new ConstantExpression(4.0),
                  new DieExpression(bag.d(6)) // scalastyle:ignore magic.number
                )),
                new ConstantExpression(1.0)
              ))
            ))
          )
          ExpressionParser.parse("4d6+2L").success.value should equal (
            new FunctionCallExpression("sum", ExpressionFunctions.sum, List(
              new FunctionCallExpression("cloneLowestRolls", ExpressionFunctions.cloneLowestRolls, List(
                new FunctionCallExpression("roll", ExpressionFunctions.roll, List(
                  new ConstantExpression(4.0),
                  new DieExpression(bag.d(6)) // scalastyle:ignore magic.number
                )),
                new ConstantExpression(2.0)
              ))
            ))
          )
        }

        it("should parse a dice roll and drop highest literal") {
          ExpressionParser.parse("4d6-H").success.value should equal (
            new FunctionCallExpression("sum", ExpressionFunctions.sum, List(
              new FunctionCallExpression("dropHighestRolls", ExpressionFunctions.dropHighestRolls, List(
                new FunctionCallExpression("roll", ExpressionFunctions.roll, List(
                  new ConstantExpression(4.0),
                  new DieExpression(bag.d(6)) // scalastyle:ignore magic.number
                )),
                new ConstantExpression(1.0)
              ))
            ))
          )
          ExpressionParser.parse("4d6-2H").success.value should equal (
            new FunctionCallExpression("sum", ExpressionFunctions.sum, List(
              new FunctionCallExpression("dropHighestRolls", ExpressionFunctions.dropHighestRolls, List(
                new FunctionCallExpression("roll", ExpressionFunctions.roll, List(
                  new ConstantExpression(4.0),
                  new DieExpression(bag.d(6)) // scalastyle:ignore magic.number
                )),
                new ConstantExpression(2.0)
              ))
            ))
          )
        }

        it("should parse a dice roll and drop lowest literal") {
          ExpressionParser.parse("4d6-L").success.value should equal (
            new FunctionCallExpression("sum", ExpressionFunctions.sum, List(
              new FunctionCallExpression("dropLowestRolls", ExpressionFunctions.dropLowestRolls, List(
                new FunctionCallExpression("roll", ExpressionFunctions.roll, List(
                  new ConstantExpression(4.0),
                  new DieExpression(bag.d(6)) // scalastyle:ignore magic.number
                )),
                new ConstantExpression(1.0)
              ))
            ))
          )
          ExpressionParser.parse("4d6-2L").success.value should equal (
            new FunctionCallExpression("sum", ExpressionFunctions.sum, List(
              new FunctionCallExpression("dropLowestRolls", ExpressionFunctions.dropLowestRolls, List(
                new FunctionCallExpression("roll", ExpressionFunctions.roll, List(
                  new ConstantExpression(4.0),
                  new DieExpression(bag.d(6)) // scalastyle:ignore magic.number
                )),
                new ConstantExpression(2.0)
              ))
            ))
          )
        }
      }

      describe("arithmetic operators") {
        it("should parse the addition of two constants") {
          val source = "1 + 2"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new AdditionExpression(one, two))
        }

        it("should parse the subtraction of two constants") {
          val source = "1 - 2"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new SubtractionExpression(one, two))
        }

        it("should parse the multiplication of two constants") {
          val source = "1 * 2"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new MultiplicationExpression(one, two))
        }

        it("should parse the division of two constants") {
          val source = "1 / 2"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new DivisionExpression(one, two))
        }

        it("should parse the modulo of two constants") {
          val source = "3 % 2"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new ModuloExpression(three, two))
        }
      }

      describe("extended divide and round operators") {
        it("should parse divide and round towards zero") {
          val source = "1 // 2"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new FunctionCallExpression("trunc", ExpressionFunctions.trunc, List(
            new DivisionExpression(one, two)
          )))
        }

        it("should parse divide and round to nearest") {
          val source = "1 /~ 2"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new FunctionCallExpression("round", ExpressionFunctions.round, List(
            new DivisionExpression(one, two)
          )))
        }

        it("should parse divide and round up") {
          val source = "1 /+ 2"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new FunctionCallExpression("ceil", ExpressionFunctions.ceil, List(
            new DivisionExpression(one, two)
          )))
        }

        it("should parse divide and round down") {
          val source = "1 /- 2"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new FunctionCallExpression("floor", ExpressionFunctions.floor, List(
            new DivisionExpression(one, two)
          )))
        }
      }

      describe("unary operators") {
        it("should parse negative") {
          val source = "-1"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new NegativeExpression(one))
        }

        it("should parse positive") {
          val source = "+1"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new PositiveExpression(one))
        }
      }

      describe("operator precedence") {
        it("should give precedence to multiplication over addition") {
          val source = "3 * 1 + 1 * 3"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (
            new AdditionExpression(
              new MultiplicationExpression(three, one),
              new MultiplicationExpression(one, three)
            )
          )
        }

        it("should allow grouping to override operator precedence") {
          ExpressionParser.parse("(3 * 1) + (1 * 3)").success.value should equal (
            new AdditionExpression(
              new GroupExpression(new MultiplicationExpression(three, one)),
              new GroupExpression(new MultiplicationExpression(one, three))
            )
          )
          ExpressionParser.parse("3 * (1 + 1) * 3").success.value should equal (
            new MultiplicationExpression(
              new MultiplicationExpression(
                three,
                new GroupExpression(new AdditionExpression(one, one))
              ),
              three
            )
          )
          ExpressionParser.parse("3 * ((1 + 1) * 3)").success.value should equal (
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
          ExpressionParser.parse("(3 * (1 + 1)) * 3").success.value should equal (
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

        it("should give precedence to divide and round down operator over unary negative operator") {
          ExpressionParser.parse("1/-2").success.value should equal (new FunctionCallExpression("floor", ExpressionFunctions.floor, List(
            new DivisionExpression(one, two)
          )))
          ExpressionParser.parse("1/ -2").success.value should equal (new DivisionExpression(
            one,
            new NegativeExpression(two)
          ))
        }

        it("should give precedence to divide and round up operator over unary positive operator") {
          ExpressionParser.parse("1/+2").success.value should equal (new FunctionCallExpression("ceil", ExpressionFunctions.ceil, List(
            new DivisionExpression(one, two)
          )))
          ExpressionParser.parse("1/ +2").success.value should equal (new DivisionExpression(
            one,
            new PositiveExpression(two)
          ))
        }
      }

      describe("function calls") {
        val f = (args: Seq[_]) => args.asInstanceOf[Seq[Double]].sum

        describe("when function is unknown") {
          it("should return an exception") {
            val source = "f()"

            ExpressionParser.parse(source).failure.exception shouldBe an [IllegalArgumentException] // scalastyle:ignore no.whitespace.before.left.bracket
          }
        }

        it("should parse a function call with zero arguments") {
          val source = "f()"
          val context = new ExpressionParser.Context(bag, Map("f" -> f))

          val expression = ExpressionParser.parse(source, context)

          expression.success.value should equal (new FunctionCallExpression("f", f, Nil))
        }

        it("should parse a function call with one argument") {
          val source = "f(1)"
          val context = new ExpressionParser.Context(bag, Map("f" -> f))

          val expression = ExpressionParser.parse(source, context)

          expression.success.value should equal (new FunctionCallExpression("f", f, List(one)))
        }

        it("should parse a function call with two arguments") {
          val source = "f(1, 2)"
          val context = new ExpressionParser.Context(bag, Map("f" -> f))

          val expression = ExpressionParser.parse(source, context)

          expression.success.value should equal (new FunctionCallExpression("f", f, List(one, two)))
        }
      }

      describe("built-in functions") {
        describe("when context contains a function with the same name as a built-in function") {
          it("should use the function from the context") {
            val source = "ceil(1)"
            val ceil = (args: Seq[_]) => 42.0
            val context = new ExpressionParser.Context(bag, Map("ceil" -> ceil))

            val expression = ExpressionParser.parse(source, context)

            val expressionResultValue = expression map (_.evaluate()) map (_.value)
            expressionResultValue.success.value should equal (42.0)
          }
        }

        it("should parse the built-in ceil() function") {
          val source = "ceil(1)"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new FunctionCallExpression("ceil", ExpressionFunctions.ceil, List(one)))
        }

        it("should parse the built-in cloneHighestRolls() function") {
          val source = "cloneHighestRolls(roll(3, d6), 2)"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (
            new FunctionCallExpression("cloneHighestRolls", ExpressionFunctions.cloneHighestRolls, List(
              new FunctionCallExpression("roll", ExpressionFunctions.roll, List(
                new ConstantExpression(3.0),
                new DieExpression(bag.d(6)) // scalastyle:ignore magic.number
              )),
              new ConstantExpression(2.0)
            ))
          )
        }

        it("should parse the built-in cloneLowestRolls() function") {
          val source = "cloneLowestRolls(roll(3, d6), 2)"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (
            new FunctionCallExpression("cloneLowestRolls", ExpressionFunctions.cloneLowestRolls, List(
              new FunctionCallExpression("roll", ExpressionFunctions.roll, List(
                new ConstantExpression(3.0),
                new DieExpression(bag.d(6)) // scalastyle:ignore magic.number
              )),
              new ConstantExpression(2.0)
            ))
          )
        }

        it("should parse the built-in dropHighestRolls() function") {
          val source = "dropHighestRolls(roll(3, d6), 2)"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (
            new FunctionCallExpression("dropHighestRolls", ExpressionFunctions.dropHighestRolls, List(
              new FunctionCallExpression("roll", ExpressionFunctions.roll, List(
                new ConstantExpression(3.0),
                new DieExpression(bag.d(6)) // scalastyle:ignore magic.number
              )),
              new ConstantExpression(2.0)
            ))
          )
        }

        it("should parse the built-in dropLowestRolls() function") {
          val source = "dropLowestRolls(roll(3, d6), 2)"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (
            new FunctionCallExpression("dropLowestRolls", ExpressionFunctions.dropLowestRolls, List(
              new FunctionCallExpression("roll", ExpressionFunctions.roll, List(
                new ConstantExpression(3.0),
                new DieExpression(bag.d(6)) // scalastyle:ignore magic.number
              )),
              new ConstantExpression(2.0)
            ))
          )
        }

        it("should parse the built-in floor() function") {
          val source = "floor(1)"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new FunctionCallExpression("floor", ExpressionFunctions.floor, List(one)))
        }

        it("should parse the built-in roll() function") {
          val source = "roll(3, d6)"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new FunctionCallExpression("roll", ExpressionFunctions.roll, List(
            three,
            new DieExpression(bag.d(6)) // scalastyle:ignore magic.number
          )))
        }

        it("should parse the built-in round() function") {
          val source = "round(1)"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new FunctionCallExpression("round", ExpressionFunctions.round, List(one)))
        }

        it("should parse the built-in sum() function") {
          val source = "sum(roll(2, d8))"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new FunctionCallExpression("sum", ExpressionFunctions.sum, List(
            new FunctionCallExpression("roll", ExpressionFunctions.roll, List(
              two,
              new DieExpression(bag.d(8)) // scalastyle:ignore magic.number
            ))
          )))
        }

        it("should parse the built-in trunc() function") {
          val source = "trunc(1)"

          val expression = ExpressionParser.parse(source)

          expression.success.value should equal (new FunctionCallExpression("trunc", ExpressionFunctions.trunc, List(one)))
        }
      }
    }
  }
}

