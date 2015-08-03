name := "polyhedra-jvm"
version := "0.1"
scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  "nl.jqno.equalsverifier" % "equalsverifier" % "1.7.3" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)

scalacOptions ++= Seq(
  "-encoding", "UTF-8",
  "-optimise",
  "-deprecation",
  "-unchecked",
  "-feature",
  "-Xlint",
  "-Ywarn-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-inaccessible",
  "-Ywarn-infer-any",
  "-Ywarn-nullary-override",
  "-Ywarn-nullary-unit",
  "-Ywarn-numeric-widen",
  "-Ywarn-unused",
  "-Ywarn-unused-import"
)

javacOptions ++= Seq(
  "-Xlint:all"
)

fork in Test := true
scalastyleFailOnError := true

