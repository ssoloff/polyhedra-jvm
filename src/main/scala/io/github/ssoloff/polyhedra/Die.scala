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

import java.util.Objects

/** A die.
  *
  * @constructor Creates a new die with the specified number of sides.
  *
  * @param sides
  *   The count of sides the die possesses.
  * @param randomNumberGenerator
  *   A random number generator that returns a number in the half-open range
  *   [0,1).
  *
  * @throws java.lang.IllegalArgumentException
  *   If `sides` is not positive.
  */
final class Die(val sides: Int, randomNumberGenerator: Die.RandomNumberGenerator) {
  require(sides > 0)

  /** Rolls the die.
    *
    * @see [[roll]]
    */
  def apply(): Int = roll()

  override def equals(other: Any): Boolean = other match {
    case that: Die => sides == that.sides
    case _ => false
  }

  override def hashCode(): Int = Objects.hash(Int.box(sides))

  /** Returns the result of rolling the die.
    *
    * @return The result of rolling the die: a value in the range `[1, [[sides]]]`.
    */
  def roll(): Int = (randomNumberGenerator() * sides).toInt + 1

  // $COVERAGE-OFF$
  override def toString: String = "Die(" +
    s"randomNumberGenerator=$randomNumberGenerator" +
    s", sides=$sides" +
    ")"
  // $COVERAGE-ON$
}

/** Provides useful members for working with dice.
  */
object Die {
  /** A random number generator that returns a number in the half-open range
    * [0,1).
    *
    * @return A random number in the half-open range [0,1).
    */
  type RandomNumberGenerator = () => Double
}

