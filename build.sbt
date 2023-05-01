name := "cats-practices"

version := "2.5.3"

scalaVersion := "2.12.8"

libraryDependencies += "org.typelevel" %% "cats-effect" % "2.5.3" withSources() withJavadoc()

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:postfixOps",
  "-language:higherKinds",
  "-Ypartial-unification")
