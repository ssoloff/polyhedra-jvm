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
  describe("Die") {
    describe("#Die") {
      describe("when sides less than one") {
        it("should throw an exception") {
          an [IllegalArgumentException] should be thrownBy { // scalastyle:ignore no.whitespace.before.left.bracket
            new Die(0)
          }
        }
      }
    }

    describe("#apply") {
      it("should return a value in the range [1,sides]") {
        val die = new Die(6) // scalastyle:ignore magic.number

        val roll = die()

        roll should (be >= 1 and be <= 6)
      }
    }

    describe("#roll") {
      it("should return a value in the range [1,sides]") {
        val die = new Die(6) // scalastyle:ignore magic.number

        val roll = die.roll()

        roll should (be >= 1 and be <= 6)
      }
    }

    describe("#sides") {
      it("should return the die sides") {
        val die = new Die(6) // scalastyle:ignore magic.number

        die.sides should equal (6) // scalastyle:ignore magic.number
      }
    }
  }
}

