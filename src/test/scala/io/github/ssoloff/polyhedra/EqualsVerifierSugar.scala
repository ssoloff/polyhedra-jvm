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

import nl.jqno.equalsverifier.{EqualsVerifier, Warning}
import org.scalatest.matchers.{BeMatcher, MatchResult}
import scala.util.{Failure, Try}

/** Trait that provides some basic syntax sugar for EqualsVerifier.
  */
trait EqualsVerifierSugar {
  /** This class is part of the EqualsVerifier sugar DSL.
    */
  final class EquatableMatcher extends BeMatcher[Class[_]] {
    override def apply(left: Class[_]): MatchResult = {
      val result = Try(EqualsVerifier.forClass(left).suppress(Warning.NULL_FIELDS).verify())
      val failureDetailMessage = result match {
        case Failure(e) => s" (${e.getMessage})"
        case _ => ""
      }
      MatchResult(
        result.isSuccess,
        s"${left.getName} is not equatable$failureDetailMessage",
        s"${left.getName} is equatable"
      )
    }
  }

  /** This field enables syntax such as the following:
    *
    * <pre>
    * classOf[String] should be (equatable)
    * </pre>
    */
  val equatable = new EquatableMatcher
}

