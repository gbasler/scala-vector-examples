scalacOptions in ThisBuild += "-deprecation"

// SBT plugin for running OpenJDK JMH benchmarks.
// https://github.com/ktoso/sbt-jmh
addSbtPlugin("pl.project13.scala" % "sbt-jmh" % "0.3.3")
