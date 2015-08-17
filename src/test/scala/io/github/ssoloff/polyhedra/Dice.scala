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

/** Trait that provides common dice used for dice testing.
  */
trait Dice extends RandomNumberGenerators {
  /** Creates a new die with the specified count of sides that uses the
    * default random number generator.
    *
    * @param sides
    *   The count of sides the die possesses.
    *
    * @return The new die.
    *
    * @throws java.lang.IllegalArgumentException
    *   If `sides` is not positive.
    */
  def createDie(sides: Int): Die = new Die(sides, DefaultRandomNumberGenerator)

  /** Creates a new die with the specified count of sides where rolling the die
    * will deterministically and repeatedly result in the sequence [1,`sides`].
    *
    * @param sides
    *   The count of sides the die possesses.
    *
    * @return The new die.
    *
    * @throws java.lang.IllegalArgumentException
    *   If `sides` is not positive.
    */
  def createDieThatRollsEachSideSuccessively(sides: Int): Die = new Die(sides, createSuccessiveStepRandomNumberGenerator(sides))
}

