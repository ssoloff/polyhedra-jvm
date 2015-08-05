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
import scala.reflect.ClassTag
import scala.util.{Failure, Try}

/** Trait that provides some basic syntax sugar for EqualsVerifier.
  */
trait EqualsVerifierSugar {
  /** This class is part of the EqualsVerifier sugar DSL.
    */
  final class EquatableMatcher extends BeMatcher[ResultOfInstancesOfTypeInvocation[_]] {
    override def apply(left: ResultOfInstancesOfTypeInvocation[_]): MatchResult = {
      val result = Try(left.equalsVerifier.verify())
      val failureDetailMessage = result match {
        case Failure(e) => s" (${e.getMessage})"
        case _ => ""
      }
      MatchResult(
        result.isSuccess,
        s"${left.runtimeClass.getName} is not equatable$failureDetailMessage",
        s"${left.runtimeClass.getName} is equatable"
      )
    }
  }

  /** This class is part of the EqualsVerifier sugar DSL.
    */
  final class ResultOfInstancesOfTypeInvocation[T: ClassTag] {
    /** The runtime class under test.
      */
    val runtimeClass = implicitly[ClassTag[T]].runtimeClass.asInstanceOf[Class[T]]

    /** The `EqualsVerifier` instance used to verify implementation of the
      * equatable interface.
      */
    val equalsVerifier = EqualsVerifier.forClass(runtimeClass).suppress(Warning.NULL_FIELDS)

    /** Adds prefabricated values for instance fields of classes that
      * `EqualsVerifier` cannot instantiate by itself.
      *
      * @tparam U
      *   The type of the prefabricated values.
      *
      * @param red
      *   An instance of {@code U}.
      * @param black
      *   Another instance of {@code U}.
      *
      * @return A reference to this invocation.
      *
      * @throws java.lang.IllegalArgumentException
      *   If {@code red} equals {@code black}.
      */
    def withPrefabValues[U: ClassTag](red: U, black: U): ResultOfInstancesOfTypeInvocation[T] = {
      val runtimeClassForU = implicitly[ClassTag[U]].runtimeClass.asInstanceOf[Class[U]]
      equalsVerifier.withPrefabValues(runtimeClassForU, red, black)
      this
    }
  }

  /** This field enables syntax such as the following:
    *
    * <pre>
    * instancesOf [String] should be (equatable)
    * </pre>
    */
  val equatable = new EquatableMatcher

  /** This method enables syntax such as the following:
    *
    * <pre>
    * instancesOf [String] should be (equatable)
    * </pre>
    */
  def instancesOf[T: ClassTag]: ResultOfInstancesOfTypeInvocation[T] = new ResultOfInstancesOfTypeInvocation[T]
}

