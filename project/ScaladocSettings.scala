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

import sbt._
import sbt.Keys._
import scala.util.matching.Regex
import scala.util.matching.Regex.Match

// adapted from http://stackoverflow.com/questions/16934488/how-to-link-classes-from-jdk-into-scaladoc-generated-doc
object ScaladocSettings {
  private[this] val externalJavadocMap = Map(
    //"owlapi" -> "http://owlcs.github.io/owlapi/apidocs_4_0_2/index.html"
  )

  /*
   * The rt.jar file is located in the path stored in the sun.boot.class.path system property.
   * See the Oracle documentation at http://docs.oracle.com/javase/6/docs/technotes/tools/findingclasses.html.
   */
  private[this] val rtJar: String = System.getProperty("sun.boot.class.path").split(java.io.File.pathSeparator).collectFirst {
    case str: String if str.endsWith(java.io.File.separator + "rt.jar") => str
  }.get // fail hard if not found

  private[this] val javaApiUrl: String = "http://docs.oracle.com/javase/8/docs/api/index.html"

  private[this] val allExternalJavadocLinks: Seq[String] = javaApiUrl +: externalJavadocMap.values.toSeq

  private[this] def javadocLinkRegex(javadocURL: String): Regex = ("""\"(\Q""" + javadocURL + """\E)#([^"]*)\"""").r

  private[this] def hasJavadocLink(f: File): Boolean = allExternalJavadocLinks exists {
    javadocURL: String =>
      (javadocLinkRegex(javadocURL) findFirstIn IO.read(f)).nonEmpty
  }

  private[this] val fixJavaLinks: Match => String = m =>
    m.group(1) + "?" + m.group(2).replace(".", "/") + ".html"

  private[this] val mapJavaLink = { target: File =>
    (target ** "*.html").get.filter(hasJavadocLink).foreach { f =>
      //println(s"Fixing $f.")
      val newContent: String = allExternalJavadocLinks.foldLeft(IO.read(f)) {
        case (oldContent: String, javadocURL: String) =>
          javadocLinkRegex(javadocURL).replaceAllIn(oldContent, fixJavaLinks)
      }
      IO.write(f, newContent)
    }
    target
  }

  /* You can print the classpath with `show compile:fullClasspath` in the SBT REPL.
   * From that list you can find the name of the jar for the managed dependency.
   */
  lazy val scaladocSettings = Seq(
    apiMappings ++= {
      // Lookup the path to jar from the classpath
      val classpath = (fullClasspath in Compile).value
      def findJar(nameBeginsWith: String): File = {
        classpath.find { attributed: Attributed[File] => (attributed.data ** s"$nameBeginsWith*.jar").get.nonEmpty }.get.data // fail hard if not found
      }
      // Define external documentation paths
      (externalJavadocMap map {
        case (name, javadocURL) => findJar(name) -> url(javadocURL)
      }) + (file(rtJar) -> url(javaApiUrl))
    },
    // Override the task to fix the links to JavaDoc
    doc in Compile <<= (doc in Compile) map mapJavaLink,
    doc in Test <<= (doc in Test) map mapJavaLink
  )
}

