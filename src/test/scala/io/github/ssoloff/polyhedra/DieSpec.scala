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

final class DieSpec extends FunSpec with Matchers {
  final val Sides = 6

  describe("Die") {
    describe("#Die") {
      describe("when random number generator is not specified") {
        it("should use a default random number generator") {
          noException should be thrownBy new Die(Sides)
        }
      }

      describe("when sides less than one") {
        it("should throw an exception") {
          an [IllegalArgumentException] should be thrownBy new Die(0) // scalastyle:ignore no.whitespace.before.left.bracket
        }
      }
    }

    describe("#apply") {
      describe("when random number generator returns minimum value") {
        it("should return 1") {
          val die = new Die(Sides, () => 0.0)

          val roll = die()

          roll should equal (1)
        }
      }

      describe("when random number generator returns maximum value") {
        it("should return sides") {
          val die = new Die(Sides, () => Math.nextAfter(1.0, Double.NegativeInfinity))

          val roll = die()

          roll should equal (Sides)
        }
      }
    }

    describe("#roll") {
      describe("when random number generator returns minimum value") {
        it("should return 1") {
          val die = new Die(Sides, () => 0.0)

          val roll = die.roll()

          roll should equal (1)
        }
      }

      describe("when random number generator returns maximum value") {
        it("should return sides") {
          val die = new Die(Sides, () => Math.nextAfter(1.0, Double.NegativeInfinity))

          val roll = die.roll()

          roll should equal (Sides)
        }
      }
    }

    describe("#sides") {
      it("should return the die sides") {
        val die = new Die(Sides)

        die.sides should equal (Sides)
      }
    }
  }
}

