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

/** A set of built-in functions for use in a dice expression.
  */
object ExpressionFunctions {
  /** Optionally returns the built-in function with the specified name.
    *
    * @param name
    *   The built-in function name.
    *
    * @return An option value containing the built-in function associated with
    *   the specified name, or {@code None} if none exists.
    */
  def apply(name: String): Option[Seq[_] => _] = functions.get(name)

  /** Returns the smallest integer greater than or equal to the specified
    * number.
    *
    * @param args
    *   The function arguments; must be a single {@code Double}.
    *
    * @return The smallest integer greater than or equal to the specified
    *   number.
    */
  val ceil = (args: Seq[_]) => Math.ceil(args.head.asInstanceOf[Double])

  /** Returns the largest integer less than or equal to the specified number.
    *
    * @param args
    *   The function arguments; must be a single {@code Double}.
    *
    * @return The largest integer less than or equal to the specified number.
    */
  val floor = (args: Seq[_]) => Math.floor(args.head.asInstanceOf[Double])

  private[this] lazy val functions = Map(
    "ceil" -> ceil,
    "floor" -> floor,
    "round" -> round,
    "trunc" -> trunc
  )

  /** Returns the value of the specified number rounded to the nearest integer.
    *
    * <p>
    * If the fractional portion of number is 0.5 or greater, the argument is
    * rounded to the next higher integer.  If the fractional portion of number
    * is less than 0.5, the argument is rounded to the next lower integer.
    * </p>
    *
    * @param args
    *   The function arguments; must be a single {@code Double}.
    *
    * @return The value of the specified number rounded to the nearest integer.
    */
  val round = (args: Seq[_]) => Math.round(args.head.asInstanceOf[Double]).toDouble

  /** Returns the integral part of the specified number by removing any
    * fractional digits.
    *
    * <p>
    * If the argument is a positive number, `trunc` is equivalent to `floor`,
    * otherwise `trunc` is equivalent to `ceil`.
    * </p>
    *
    * @param args
    *   The function arguments; must be a single {@code Double}.
    *
    * @return The integral part of the specified number.
    */
  val trunc = (args: Seq[_]) => args.head.asInstanceOf[Double] match {
    case x if x < 0.0 => Math.ceil(x)
    case x => Math.floor(x)
  }
}

