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

import scala.collection.mutable.Buffer

/** A set of built-in functions for use in a dice expression.
  */
object ExpressionFunctions {
  /** Returns a new collection of rolls with one or more of the target rolls
    * in the specified collection cloned and appended to the original
    * collection of rolls.
    *
    * @param rolls
    *   The collection of rolls.
    * @param count
    *   The number of rolls to clone.
    * @param getTargetRollValue
    *   The function that defines the target value to clone.
    *
    * @return The collection of rolls with the `count` target rolls cloned.
    *
    * @throws java.lang.IllegalArgumentException
    *   If `count` is less than zero.
    */
  private[this] def cloneRolls(rolls: Seq[Double], count: Int, getTargetRollValue: Buffer[Double] => Double): Seq[Double] = {
    require(count >= 0)

    val newRolls = Buffer(rolls: _*)
    val oldRolls = Buffer(rolls: _*)
    for (pass <- 1 to count if !oldRolls.isEmpty) {
      val index = oldRolls.indexOf(getTargetRollValue(oldRolls))
      newRolls.append(oldRolls(index))
      oldRolls.remove(index, 1)
    }

    scala.collection.immutable.Seq(newRolls: _*)
  }

  /** Returns a new collection of rolls with one or more of the target rolls
    * in the specified collection dropped.
    *
    * @param rolls
    *   The collection of rolls.
    * @param count
    *   The number of rolls to drop.
    * @param getTargetRollValue
    *   The function that defines the target value to drop.
    *
    * @return The collection of rolls with the `count` target rolls dropped.
    *
    * @throws java.lang.IllegalArgumentException
    *   If `count` is less than zero.
    */
  private[this] def dropRolls(rolls: Seq[Double], count: Int, getTargetRollValue: Buffer[Double] => Double): Seq[Double] = {
    require(count >= 0)

    val newRolls = Buffer(rolls: _*)
    for (pass <- 1 to count if !newRolls.isEmpty) {
      val index = newRolls.indexOf(getTargetRollValue(newRolls))
      newRolls.remove(index, 1)
    }

    scala.collection.immutable.Seq(newRolls: _*)
  }

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
    * <p>
    * The function arguments are
    * </p>
    *
    * <ul>
    * <li>{@code x} - A number. (`Double`)</li>
    * </ul>
    *
    * @param args
    *   The function arguments.
    *
    * @return The smallest integer greater than or equal to the specified
    *   number.
    */
  val ceil = (args: Seq[_]) => {
    require(args.size == 1)
    Math.ceil(args(0).asInstanceOf[Double])
  }

  /** Returns a new collection of rolls with one or more of the highest rolls
    * in the specified collection cloned and appended to the original
    * collection of rolls.
    *
    * <p>
    * The function arguments are
    * </p>
    *
    * <ul>
    * <li>{@code rolls} - The collection of rolls. (`Seq[Double]`)</li>
    * <li>{@code count} - The number of rolls to clone. (`Double`)</li>
    * </ul>
    *
    * @param args
    *   The function arguments.
    *
    * @return The collection of rolls with the `count` highest rolls cloned.
    *
    * @throws java.lang.IllegalArgumentException
    *   If `count` is less than zero.
    */
  val cloneHighestRolls = (args: Seq[_]) => {
    require(args.size == 2)
    val rolls = args(0).asInstanceOf[Seq[Double]]
    val count = args(1).asInstanceOf[Double].toInt
    cloneRolls(rolls, count, _.max)
  }

  /** Returns a new collection of rolls with one or more of the lowest rolls
    * in the specified collection cloned and appended to the original
    * collection of rolls.
    *
    * <p>
    * The function arguments are
    * </p>
    *
    * <ul>
    * <li>{@code rolls} - The collection of rolls. (`Seq[Double]`)</li>
    * <li>{@code count} - The number of rolls to clone. (`Double`)</li>
    * </ul>
    *
    * @param args
    *   The function arguments.
    *
    * @return The collection of rolls with the `count` lowest rolls cloned.
    *
    * @throws java.lang.IllegalArgumentException
    *   If `count` is less than zero.
    */
  val cloneLowestRolls = (args: Seq[_]) => {
    require(args.size == 2)
    val rolls = args(0).asInstanceOf[Seq[Double]]
    val count = args(1).asInstanceOf[Double].toInt
    cloneRolls(rolls, count, _.min)
  }

  /** Returns a new collection of rolls with one or more of the highest rolls
    * in the specified collection dropped.
    *
    * <p>
    * The function arguments are
    * </p>
    *
    * <ul>
    * <li>{@code rolls} - The collection of rolls. (`Seq[Double]`)</li>
    * <li>{@code count} - The number of rolls to drop. (`Double`)</li>
    * </ul>
    *
    * @param args
    *   The function arguments.
    *
    * @return The collection of rolls with the `count` highest rolls dropped.
    *
    * @throws java.lang.IllegalArgumentException
    *   If `count` is less than zero.
    */
  val dropHighestRolls = (args: Seq[_]) => {
    require(args.size == 2)
    val rolls = args(0).asInstanceOf[Seq[Double]]
    val count = args(1).asInstanceOf[Double].toInt
    dropRolls(rolls, count, _.max)
  }

  /** Returns a new collection of rolls with one or more of the lowest rolls
    * in the specified collection dropped.
    *
    * <p>
    * The function arguments are
    * </p>
    *
    * <ul>
    * <li>{@code rolls} - The collection of rolls. (`Seq[Double]`)</li>
    * <li>{@code count} - The number of rolls to drop. (`Double`)</li>
    * </ul>
    *
    * @param args
    *   The function arguments.
    *
    * @return The collection of rolls with the `count` lowest rolls dropped.
    *
    * @throws java.lang.IllegalArgumentException
    *   If `count` is less than zero.
    */
  val dropLowestRolls = (args: Seq[_]) => {
    require(args.size == 2)
    val rolls = args(0).asInstanceOf[Seq[Double]]
    val count = args(1).asInstanceOf[Double].toInt
    dropRolls(rolls, count, _.min)
  }

  /** Returns the largest integer less than or equal to the specified number.
    *
    * <p>
    * The function arguments are
    * </p>
    *
    * <ul>
    * <li>{@code x} - A number. (`Double`)</li>
    * </ul>
    *
    * @param args
    *   The function arguments.
    *
    * @return The largest integer less than or equal to the specified number.
    */
  val floor = (args: Seq[_]) => {
    require(args.size == 1)
    Math.floor(args(0).asInstanceOf[Double])
  }

  private[this] lazy val functions = Map(
    "ceil" -> ceil,
    "cloneHighestRolls" -> cloneHighestRolls,
    "cloneLowestRolls" -> cloneLowestRolls,
    "dropHighestRolls" -> dropHighestRolls,
    "dropLowestRolls" -> dropLowestRolls,
    "floor" -> floor,
    "roll" -> roll,
    "round" -> round,
    "sum" -> sum,
    "trunc" -> trunc
  )

  /** Rolls one or more instances of the specified die.
    *
    * <p>
    * The function arguments are
    * </p>
    *
    * <ul>
    * <li>{@code count} - The number of dice to roll. (`Double`)</li>
    * <li>{@code die} - The die to roll. (`Die`)</li>
    * </ul>
    *
    * @param args
    *   The function arguments.
    *
    * @return The collection of rolls.
    *
    * @throws java.lang.IllegalArgumentException
    *   If `count` is less than one.
    */
  val roll = (args: Seq[_]) => {
    require(args.size == 2)
    val sides = args(0).asInstanceOf[Double].toInt
    val die = args(1).asInstanceOf[Die]
    (1 to sides) map (_ => die.roll().toDouble)
  }

  /** Returns the value of the specified number rounded to the nearest integer.
    *
    * <p>
    * If the fractional portion of number is 0.5 or greater, the argument is
    * rounded to the next higher integer.  If the fractional portion of number
    * is less than 0.5, the argument is rounded to the next lower integer.
    * </p>
    *
    * <p>
    * The function arguments are
    * </p>
    *
    * <ul>
    * <li>{@code x} - A number. (`Double`)</li>
    * </ul>
    *
    * @param args
    *   The function arguments.
    *
    * @return The value of the specified number rounded to the nearest integer.
    */
  val round = (args: Seq[_]) => {
    require(args.size == 1)
    Math.round(args(0).asInstanceOf[Double]).toDouble
  }

  /**
    * Returns the sum of the specified collection of numbers.
    *
    * <p>
    * The function arguments are
    * </p>
    *
    * <ul>
    * <li>{@code values} - A collection of numbers. (`Seq[Double]`)</li>
    * </ul>
    *
    * @param args
    *   The function arguments.
    *
    * @return The sum of the specified collection of numbers.
    *
    * @throws java.lang.IllegalArgumentException
    *   If `values` contains less than one element.
    */
  val sum = (args: Seq[_]) => {
    require(args.size == 1)
    val values = args(0).asInstanceOf[Seq[Double]]
    require(!values.isEmpty)
    values reduce (_ + _)
  }

  /** Returns the integral part of the specified number by removing any
    * fractional digits.
    *
    * <p>
    * If the argument is a positive number, `trunc` is equivalent to `floor`,
    * otherwise `trunc` is equivalent to `ceil`.
    * </p>
    *
    * <p>
    * The function arguments are
    * </p>
    *
    * <ul>
    * <li>{@code x} - A number. (`Double`)</li>
    * </ul>
    *
    * @param args
    *   The function arguments.
    *
    * @return The integral part of the specified number.
    */
  val trunc = (args: Seq[_]) => {
    require(args.size == 1)
    args(0).asInstanceOf[Double] match {
      case x if x < 0.0 => Math.ceil(x)
      case x => Math.floor(x)
    }
  }
}

