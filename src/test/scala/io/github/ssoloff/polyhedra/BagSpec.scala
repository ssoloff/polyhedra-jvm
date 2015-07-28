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
import util.Random

final class BagSpec extends FunSpec with Matchers {
  private final val AlwaysMinimumRandomNumberGenerator = () => 0.0
  private final val DefaultRandomNumberGenerator = Random.nextDouble _
  private final val Sides = 6

  private def createBag(randomNumberGenerator: Die.RandomNumberGenerator = DefaultRandomNumberGenerator) = new Bag(randomNumberGenerator)

  describe("Bag") {
    describe("#Bag") {
      describe("when random number generator is not specified") {
        it("should use a default random number generator") {
          noException should be thrownBy new Bag()
        }
      }
    }

    describe("#d") {
      it("should return a die with the specified number of sides") {
        val bag = createBag()

        val die = bag.d(Sides)

        die.sides should equal (Sides)
      }

      it("should return a die that uses the configured random number generator") {
        val bag = createBag(AlwaysMinimumRandomNumberGenerator)

        val die = bag.d(Sides)

        die.roll() should equal (1)
      }
    }
  }
}

