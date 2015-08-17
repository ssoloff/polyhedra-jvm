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
        an [Exception] should be thrownBy (ExpressionFunctions.roll(List(1))) // scalastyle:ignore no.whitespace.before.left.bracket
      }
    }

    describe("when count is not an Int") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.roll(List(1.0, createDie(3)))) // scalastyle:ignore no.whitespace.before.left.bracket
      }
    }

    describe("when die is not a Die") {
      it("should throw an exception") {
        an [Exception] should be thrownBy (ExpressionFunctions.roll(List(1, "d3"))) // scalastyle:ignore no.whitespace.before.left.bracket
      }
    }

    it("should return collection of individual rolls") {
      val d3 = createDieThatRollsEachSideSuccessively(3)

      ExpressionFunctions.roll(List(1, d3)) should equal (List(1))
      ExpressionFunctions.roll(List(2, d3)) should equal (List(2, 3))
      ExpressionFunctions.roll(List(3, d3)) should equal (List(1, 2, 3))
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

