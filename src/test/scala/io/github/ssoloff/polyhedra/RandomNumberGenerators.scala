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

import scala.util.Random

/** Trait that provides common random number generators used for dice testing.
  */
trait RandomNumberGenerators {
  /** A random number generator that always returns the maximum value
    * (1 - ULP).
    */
  final val AlwaysMaximumRandomNumberGenerator = () => Math.nextAfter(1.0, Double.NegativeInfinity)

  /** A random number generator that always returns the minimum value (0).
    */
  final val AlwaysMinimumRandomNumberGenerator = () => 0.0

  /** The default random number generator that samples from a uniform
    * distribution.
    */
  final val DefaultRandomNumberGenerator = Random.nextDouble _
}

