import Dependencies._

ThisBuild / scalaVersion     := "2.13.8"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

lazy val root = (project in file("."))
  .settings(
    name := "Scala Vector Examples",
    javacOptions ++= Seq("--add-modules=jdk.incubator.vector"),
    //scalacOptions ++= Seq("-target:12", "-optimize"),
    libraryDependencies += scalaTest % Test
  ).enablePlugins(JmhPlugin)
