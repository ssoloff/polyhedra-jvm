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

final class ExpressionFunctionsSpec extends FunSpec with Matchers with Dice {
  describe("#ceil") {
    describe("when arguments is empty") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.ceil(Nil)) // scalastyle:ignore no.whitespace.before.left.bracket
      }
    }

    describe("when arguments contains a single non-Double value") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.ceil(List(1))) // scalastyle:ignore no.whitespace.before.left.bracket
      }
    }

    describe("when arguments contains a single Double value") {
      describe("when value is negative") {
        it("should round up") {
          ExpressionFunctions.ceil(List(-1.75)) should be (-1.0)
          ExpressionFunctions.ceil(List(-1.5)) should be (-1.0)
          ExpressionFunctions.ceil(List(-1.25)) should be (-1.0)
          ExpressionFunctions.ceil(List(-1.0)) should be (-1.0)
        }
      }

      describe("when value is positive") {
        it("should round up") {
          ExpressionFunctions.ceil(List(1.0)) should be (1.0)
          ExpressionFunctions.ceil(List(1.25)) should be (2.0)
          ExpressionFunctions.ceil(List(1.5)) should be (2.0)
          ExpressionFunctions.ceil(List(1.75)) should be (2.0)
        }
      }
    }
  }

  describe("#cloneHighestRolls") {
    describe("when arguments contains less than two elements") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.cloneHighestRolls(List( // scalastyle:ignore no.whitespace.before.left.bracket
          Nil
        )))
      }
    }

    describe("when values is not a Seq[Double]") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.cloneHighestRolls(List( // scalastyle:ignore no.whitespace.before.left.bracket
          List(1),
          1.0
        )))
      }
    }

    describe("when count is not a Double") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.cloneHighestRolls(List( // scalastyle:ignore no.whitespace.before.left.bracket
          Nil,
          1
        )))
      }
    }

    describe("when count less than zero") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.cloneHighestRolls(List( // scalastyle:ignore no.whitespace.before.left.bracket
          Nil,
          -1.0
        )))
      }
    }

    describe("when count equals zero") {
      it("should return the collection unmodified") {
        ExpressionFunctions.cloneHighestRolls(List(Nil, 0.0)) should equal (Nil)
        ExpressionFunctions.cloneHighestRolls(List(List(1.0), 0.0)) should equal (List(1.0))
        ExpressionFunctions.cloneHighestRolls(List(List(1.0, 2.0), 0.0)) should equal (List(1.0, 2.0))
      }
    }

    describe("when count equals one") {
      it("should return the collection with the highest roll appended") {
        ExpressionFunctions.cloneHighestRolls(List(Nil, 1.0)) should equal (Nil)
        ExpressionFunctions.cloneHighestRolls(List(List(1.0), 1.0)) should equal (List(1.0, 1.0))
        ExpressionFunctions.cloneHighestRolls(List(List(1.0, 2.0), 1.0)) should equal (List(1.0, 2.0, 2.0))
        ExpressionFunctions.cloneHighestRolls(List(List(2.0, 1.0, 3.0), 1.0)) should equal (List(2.0, 1.0, 3.0, 3.0))
      }
    }

    describe("when count equals two") {
      it("should return the collection with the highest two rolls appended") {
        ExpressionFunctions.cloneHighestRolls(List(Nil, 2.0)) should equal (Nil)
        ExpressionFunctions.cloneHighestRolls(List(List(1.0), 2.0)) should equal (List(1.0, 1.0))
        ExpressionFunctions.cloneHighestRolls(List(List(1.0, 2.0), 2.0)) should equal (List(1.0, 2.0, 2.0, 1.0))
        ExpressionFunctions.cloneHighestRolls(List(List(2.0, 1.0, 3.0), 2.0)) should equal (List(2.0, 1.0, 3.0, 3.0, 2.0))
        ExpressionFunctions.cloneHighestRolls(List(List(4.0, 2.0, 1.0, 3.0), 2.0)) should equal (List(4.0, 2.0, 1.0, 3.0, 4.0, 3.0))
      }
    }

    describe("when rolls contains duplicates") {
      it("should return the collection with the highest rolls appended") {
        ExpressionFunctions.cloneHighestRolls(List(List(1.0, 1.0), 2.0)) should equal (List(1.0, 1.0, 1.0, 1.0))
        ExpressionFunctions.cloneHighestRolls(List(List(3.0, 2.0, 1.0, 3.0), 2.0)) should equal (List(3.0, 2.0, 1.0, 3.0, 3.0, 3.0))
      }
    }
  }

  describe("#cloneLowestRolls") {
    describe("when arguments contains less than two elements") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.cloneLowestRolls(List( // scalastyle:ignore no.whitespace.before.left.bracket
          Nil
        )))
      }
    }

    describe("when values is not a Seq[Double]") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.cloneLowestRolls(List( // scalastyle:ignore no.whitespace.before.left.bracket
          List(1),
          1.0
        )))
      }
    }

    describe("when count is not a Double") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.cloneLowestRolls(List( // scalastyle:ignore no.whitespace.before.left.bracket
          Nil,
          1
        )))
      }
    }

    describe("when count less than zero") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.cloneLowestRolls(List( // scalastyle:ignore no.whitespace.before.left.bracket
          Nil,
          -1.0
        )))
      }
    }

    describe("when count equals zero") {
      it("should return the collection unmodified") {
        ExpressionFunctions.cloneLowestRolls(List(Nil, 0.0)) should equal (Nil)
        ExpressionFunctions.cloneLowestRolls(List(List(1.0), 0.0)) should equal (List(1.0))
        ExpressionFunctions.cloneLowestRolls(List(List(1.0, 2.0), 0.0)) should equal (List(1.0, 2.0))
      }
    }

    describe("when count equals one") {
      it("should return the collection with the lowest roll appended") {
        ExpressionFunctions.cloneLowestRolls(List(Nil, 1.0)) should equal (Nil)
        ExpressionFunctions.cloneLowestRolls(List(List(1.0), 1.0)) should equal (List(1.0, 1.0))
        ExpressionFunctions.cloneLowestRolls(List(List(1.0, 2.0), 1.0)) should equal (List(1.0, 2.0, 1.0))
        ExpressionFunctions.cloneLowestRolls(List(List(2.0, 1.0, 3.0), 1.0)) should equal (List(2.0, 1.0, 3.0, 1.0))
      }
    }

    describe("when count equals two") {
      it("should return the collection with the lowest two rolls appended") {
        ExpressionFunctions.cloneLowestRolls(List(Nil, 2.0)) should equal (Nil)
        ExpressionFunctions.cloneLowestRolls(List(List(1.0), 2.0)) should equal (List(1.0, 1.0))
        ExpressionFunctions.cloneLowestRolls(List(List(1.0, 2.0), 2.0)) should equal (List(1.0, 2.0, 1.0, 2.0))
        ExpressionFunctions.cloneLowestRolls(List(List(2.0, 1.0, 3.0), 2.0)) should equal (List(2.0, 1.0, 3.0, 1.0, 2.0))
        ExpressionFunctions.cloneLowestRolls(List(List(4.0, 2.0, 1.0, 3.0), 2.0)) should equal (List(4.0, 2.0, 1.0, 3.0, 1.0, 2.0))
      }
    }

    describe("when rolls contains duplicates") {
      it("should return the collection with the lowest rolls appended") {
        ExpressionFunctions.cloneLowestRolls(List(List(1.0, 1.0), 2.0)) should equal (List(1.0, 1.0, 1.0, 1.0))
        ExpressionFunctions.cloneLowestRolls(List(List(1.0, 2.0, 3.0, 1.0), 2.0)) should equal (List(1.0, 2.0, 3.0, 1.0, 1.0, 1.0))
      }
    }
  }

  describe("#dropHighestRolls") {
    describe("when arguments contains less than two elements") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.dropHighestRolls(List( // scalastyle:ignore no.whitespace.before.left.bracket
          Nil
        )))
      }
    }

    describe("when values is not a Seq[Double]") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.dropHighestRolls(List( // scalastyle:ignore no.whitespace.before.left.bracket
          List(1),
          1.0
        )))
      }
    }

    describe("when count is not a Double") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.dropHighestRolls(List( // scalastyle:ignore no.whitespace.before.left.bracket
          Nil,
          1
        )))
      }
    }

    describe("when count less than zero") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.dropHighestRolls(List( // scalastyle:ignore no.whitespace.before.left.bracket
          Nil,
          -1.0
        )))
      }
    }

    describe("when count equals zero") {
      it("should return the collection unmodified") {
        ExpressionFunctions.dropHighestRolls(List(Nil, 0.0)) should equal (Nil)
        ExpressionFunctions.dropHighestRolls(List(List(1.0), 0.0)) should equal (List(1.0))
        ExpressionFunctions.dropHighestRolls(List(List(1.0, 2.0), 0.0)) should equal (List(1.0, 2.0))
      }
    }

    describe("when count equals one") {
      it("should return the collection without the highest roll") {
        ExpressionFunctions.dropHighestRolls(List(Nil, 1.0)) should equal (Nil)
        ExpressionFunctions.dropHighestRolls(List(List(1.0), 1.0)) should equal (Nil)
        ExpressionFunctions.dropHighestRolls(List(List(1.0, 2.0), 1.0)) should equal (List(1.0))
        ExpressionFunctions.dropHighestRolls(List(List(2.0, 1.0, 3.0), 1.0)) should equal (List(2.0, 1.0))
      }
    }

    describe("when count equals two") {
      it("should return the collection without the highest two rolls") {
        ExpressionFunctions.dropHighestRolls(List(Nil, 2.0)) should equal (Nil)
        ExpressionFunctions.dropHighestRolls(List(List(1.0), 2.0)) should equal (Nil)
        ExpressionFunctions.dropHighestRolls(List(List(1.0, 2.0), 2.0)) should equal (Nil)
        ExpressionFunctions.dropHighestRolls(List(List(2.0, 1.0, 3.0), 2.0)) should equal (List(1.0))
        ExpressionFunctions.dropHighestRolls(List(List(4.0, 2.0, 1.0, 3.0), 2.0)) should equal (List(2.0, 1.0))
      }
    }

    describe("when rolls contains duplicates") {
      it("should return the collection without the earliest occurrences of the highest rolls") {
        ExpressionFunctions.dropHighestRolls(List(List(1.0, 1.0), 2.0)) should equal (Nil)
        ExpressionFunctions.dropHighestRolls(List(List(3.0, 3.0, 1.0, 3.0), 2.0)) should equal (List(1.0, 3.0))
        ExpressionFunctions.dropHighestRolls(List(List(1.0, 2.0, 2.0), 2.0)) should equal (List(1.0))
        ExpressionFunctions.dropHighestRolls(List(List(2.0, 3.0, 1.0, 2.0), 2.0)) should equal (List(1.0, 2.0))
      }
    }
  }

  describe("#dropLowestRolls") {
    describe("when arguments contains less than two elements") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.dropLowestRolls(List( // scalastyle:ignore no.whitespace.before.left.bracket
          Nil
        )))
      }
    }

    describe("when values is not a Seq[Double]") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.dropLowestRolls(List( // scalastyle:ignore no.whitespace.before.left.bracket
          List(1),
          1.0
        )))
      }
    }

    describe("when count is not a Double") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.dropLowestRolls(List( // scalastyle:ignore no.whitespace.before.left.bracket
          Nil,
          1
        )))
      }
    }

    describe("when count less than zero") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.dropLowestRolls(List( // scalastyle:ignore no.whitespace.before.left.bracket
          Nil,
          -1.0
        )))
      }
    }

    describe("when count equals zero") {
      it("should return the collection unmodified") {
        ExpressionFunctions.dropLowestRolls(List(Nil, 0.0)) should equal (Nil)
        ExpressionFunctions.dropLowestRolls(List(List(1.0), 0.0)) should equal (List(1.0))
        ExpressionFunctions.dropLowestRolls(List(List(1.0, 2.0), 0.0)) should equal (List(1.0, 2.0))
      }
    }

    describe("when count equals one") {
      it("should return the collection without the lowest roll") {
        ExpressionFunctions.dropLowestRolls(List(Nil, 1.0)) should equal (Nil)
        ExpressionFunctions.dropLowestRolls(List(List(1.0), 1.0)) should equal (Nil)
        ExpressionFunctions.dropLowestRolls(List(List(1.0, 2.0), 1.0)) should equal (List(2.0))
        ExpressionFunctions.dropLowestRolls(List(List(2.0, 1.0, 3.0), 1.0)) should equal (List(2.0, 3.0))
      }
    }

    describe("when count equals two") {
      it("should return the collection without the lowest two rolls") {
        ExpressionFunctions.dropLowestRolls(List(Nil, 2.0)) should equal (Nil)
        ExpressionFunctions.dropLowestRolls(List(List(1.0), 2.0)) should equal (Nil)
        ExpressionFunctions.dropLowestRolls(List(List(1.0, 2.0), 2.0)) should equal (Nil)
        ExpressionFunctions.dropLowestRolls(List(List(2.0, 1.0, 3.0), 2.0)) should equal (List(3.0))
        ExpressionFunctions.dropLowestRolls(List(List(4.0, 2.0, 1.0, 3.0), 2.0)) should equal (List(4.0, 3.0))
      }
    }

    describe("when rolls contains duplicates") {
      it("should return the collection without the earliest occurrences of the lowest rolls") {
        ExpressionFunctions.dropLowestRolls(List(List(1.0, 1.0), 2.0)) should equal (Nil)
        ExpressionFunctions.dropLowestRolls(List(List(1.0, 1.0, 3.0, 1.0), 2.0)) should equal (List(3.0, 1.0))
        ExpressionFunctions.dropLowestRolls(List(List(2.0, 1.0, 1.0), 2.0)) should equal (List(2.0))
        ExpressionFunctions.dropLowestRolls(List(List(2.0, 1.0, 3.0, 2.0), 2.0)) should equal (List(3.0, 2.0))
      }
    }
  }

  describe("#floor") {
    describe("when arguments is empty") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.floor(Nil)) // scalastyle:ignore no.whitespace.before.left.bracket
      }
    }

    describe("when arguments contains a single non-Double value") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.floor(List(1))) // scalastyle:ignore no.whitespace.before.left.bracket
      }
    }

    describe("when arguments contains a single Double value") {
      describe("when value is negative") {
        it("should round down") {
          ExpressionFunctions.floor(List(-1.75)) should be (-2.0)
          ExpressionFunctions.floor(List(-1.5)) should be (-2.0)
          ExpressionFunctions.floor(List(-1.25)) should be (-2.0)
          ExpressionFunctions.floor(List(-1.0)) should be (-1.0)
        }
      }

      describe("when value is positive") {
        it("should round down") {
          ExpressionFunctions.floor(List(1.0)) should be (1.0)
          ExpressionFunctions.floor(List(1.25)) should be (1.0)
          ExpressionFunctions.floor(List(1.5)) should be (1.0)
          ExpressionFunctions.floor(List(1.75)) should be (1.0)
        }
      }
    }
  }

  describe("#roll") {
    describe("when arguments contains less than two elements") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.roll(List(1.0))) // scalastyle:ignore no.whitespace.before.left.bracket
      }
    }

    describe("when count is not a Double") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.roll(List(1, createDie(3)))) // scalastyle:ignore no.whitespace.before.left.bracket
      }
    }

    describe("when die is not a Die") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.roll(List(1.0, "d3"))) // scalastyle:ignore no.whitespace.before.left.bracket
      }
    }

    describe("when arguments contains count and die") {
      it("should return collection of individual rolls") {
        val d3 = createDieThatRollsEachSideSuccessively(3)

        ExpressionFunctions.roll(List(1.0, d3)) should equal (List(1.0))
        ExpressionFunctions.roll(List(2.0, d3)) should equal (List(2.0, 3.0))
        ExpressionFunctions.roll(List(3.0, d3)) should equal (List(1.0, 2.0, 3.0))
      }
    }
  }

  describe("#round") {
    describe("when arguments is empty") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.round(Nil)) // scalastyle:ignore no.whitespace.before.left.bracket
      }
    }

    describe("when arguments contains a single non-Double value") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.round(List(1))) // scalastyle:ignore no.whitespace.before.left.bracket
      }
    }

    describe("when arguments contains a single Double value") {
      describe("when value is negative") {
        it("should round to nearest") {
          ExpressionFunctions.round(List(-1.75)) should be (-2.0)
          ExpressionFunctions.round(List(-1.5)) should be (-1.0)
          ExpressionFunctions.round(List(-1.25)) should be (-1.0)
          ExpressionFunctions.round(List(-1.0)) should be (-1.0)
        }
      }

      describe("when value is positive") {
        it("should round to nearest") {
          ExpressionFunctions.round(List(1.0)) should be (1.0)
          ExpressionFunctions.round(List(1.25)) should be (1.0)
          ExpressionFunctions.round(List(1.5)) should be (2.0)
          ExpressionFunctions.round(List(1.75)) should be (2.0)
        }
      }
    }
  }

  describe("#sum") {
    describe("when arguments is empty") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.sum(Nil)) // scalastyle:ignore no.whitespace.before.left.bracket
      }
    }

    describe("when arguments contains a non-Seq[Double] value") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.sum(List(1.0))) // scalastyle:ignore no.whitespace.before.left.bracket
      }
    }

    describe("when arguments contains a Seq[Double] value that is empty") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.sum(List(Nil))) // scalastyle:ignore no.whitespace.before.left.bracket
      }
    }

    describe("when arguments contains a Seq[Double] value with one or elements") {
      it("should return sum of values") {
        ExpressionFunctions.sum(List(List(1.0))) should be (1.0)
        ExpressionFunctions.sum(List(List(1.0, 2.0))) should be (3.0)
        ExpressionFunctions.sum(List(List(1.0, 2.0, 3.0))) should be (6.0)
      }
    }
  }

  describe("#trunc") {
    describe("when arguments is empty") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.trunc(Nil)) // scalastyle:ignore no.whitespace.before.left.bracket
      }
    }

    describe("when arguments contains a single non-Double value") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.trunc(List(1))) // scalastyle:ignore no.whitespace.before.left.bracket
      }
    }

    describe("when arguments contains a single Double value") {
      describe("when value is negative") {
        it("should round towards zero") {
          ExpressionFunctions.trunc(List(-1.75)) should be (-1.0)
          ExpressionFunctions.trunc(List(-1.5)) should be (-1.0)
          ExpressionFunctions.trunc(List(-1.25)) should be (-1.0)
          ExpressionFunctions.trunc(List(-1.0)) should be (-1.0)
        }
      }

      describe("when value is positive") {
        it("should round towards zero") {
          ExpressionFunctions.trunc(List(1.0)) should be (1.0)
          ExpressionFunctions.trunc(List(1.25)) should be (1.0)
          ExpressionFunctions.trunc(List(1.5)) should be (1.0)
          ExpressionFunctions.trunc(List(1.75)) should be (1.0)
        }
      }
    }
  }
}

