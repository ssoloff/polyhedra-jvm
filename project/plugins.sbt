addSbtPlugin("com.simplytyped" % "sbt-antlr4" % "0.7.6")
addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.7.0")
addSbtPlugin("org.scoverage" % "sbt-coveralls" % "1.0.0")
addSbtPlugin("org.scoverage" % "sbt-scoverage" % "1.0.4")

resolvers ++= Seq(
  "simplytyped" at "http://simplytyped.github.io/repo/releases",
  "sonatype-releases" at "https://oss.sonatype.org/content/repositories/releases/"
)

