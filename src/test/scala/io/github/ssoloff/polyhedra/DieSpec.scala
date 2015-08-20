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

final class DieSpec extends FunSpec with Matchers with RandomNumberGenerators with EqualsVerifierSugar {
  private[this] final val Sides = 6

  private[this] final def createDie(randomNumberGenerator: Die.RandomNumberGenerator = DefaultRandomNumberGenerator): Die =
    new Die(Sides, randomNumberGenerator)

  describe("Die") {
    it("should be equatable") {
      instancesOf [Die] should be (equatable) // scalastyle:ignore no.whitespace.before.left.bracket
    }

    describe("#Die") {
      describe("when sides less than one") {
        it("should throw an exception") {
          an [IllegalArgumentException] should be thrownBy new Die(0, DefaultRandomNumberGenerator) // scalastyle:ignore no.whitespace.before.left.bracket
        }
      }
    }

    describe("#apply") {
      describe("when random number generator returns minimum value") {
        it("should return 1") {
          val die = createDie(AlwaysMinimumRandomNumberGenerator)

          val roll = die()

          roll should equal (1)
        }
      }

      describe("when random number generator returns maximum value") {
        it("should return sides") {
          val die = createDie(AlwaysMaximumRandomNumberGenerator)

          val roll = die()

          roll should equal (Sides)
        }
      }
    }

    describe("#roll") {
      describe("when random number generator returns minimum value") {
        it("should return 1") {
          val die = createDie(AlwaysMinimumRandomNumberGenerator)

          val roll = die.roll()

          roll should equal (1)
        }
      }

      describe("when random number generator returns maximum value") {
        it("should return sides") {
          val die = createDie(AlwaysMaximumRandomNumberGenerator)

          val roll = die.roll()

          roll should equal (Sides)
        }
      }
    }

    describe("#sides") {
      it("should return the die sides") {
        val die = createDie()

        die.sides should equal (Sides)
      }
    }
  }
}

