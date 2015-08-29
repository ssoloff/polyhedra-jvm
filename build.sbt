import ScaladocSettings._

lazy val root = (project in file("."))
  .settings(
    name := "polyhedra-jvm",
    version := "0.1",
    scalaVersion := "2.11.7",
    libraryDependencies ++= Seq(
      "org.apache.commons" % "commons-lang3" % "3.4",
      "nl.jqno.equalsverifier" % "equalsverifier" % "1.7.5" % Test,
      "org.scalatest" %% "scalatest" % "2.2.4" % Test,
      "info.cukes" %% "cucumber-scala" % "1.2.4" % Test,
      "info.cukes" % "cucumber-junit" % "1.2.4" % Test
    ),
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
    ),
    javacOptions ++= Seq(
      "-Xlint:all"
    ),
    fork in Test := true,
    scalastyleFailOnError := true
  )
  .settings(scaladocSettings)
  .settings(antlr4Settings)
  .settings(
    antlr4GenListener in Antlr4 := false,
    antlr4GenVisitor in Antlr4 := true,
    antlr4PackageName in Antlr4 := Some("io.github.ssoloff.polyhedra.internal")
  )

