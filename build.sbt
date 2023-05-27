name := "cats-practices"
version := "2.5.3"
scalaVersion := "2.13.10"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-unchecked",
  "-language:postfixOps",
  "-language:higherKinds",
)
val prometheusVersion = "0.16.0"
val http4sVersion = "1.0.0-M32"
val circeVersion = "0.14.5"


val catsCore = "org.typelevel" %% "cats-core" % "2.9.0"
val catsEffect = "org.typelevel" %% "cats-effect" % "3.5.0"
val http4sCore = "org.http4s" %% "http4s-core" % http4sVersion
val http4sDsl = "org.http4s" %% "http4s-dsl" % http4sVersion
val http4sBlazeServer = "org.http4s" %% "http4s-blaze-server" % http4sVersion
val http4sCirce = "org.http4s" %% "http4s-circe" % http4sVersion
val circeGeneric = "io.circe" %% "circe-generic" % circeVersion
val circeParser = "io.circe" %% "circe-parser" % circeVersion
val prometheusSimpleClient = "io.prometheus" % "simpleclient" % prometheusVersion
val prometheusSimpleClientHotspot = "io.prometheus" % "simpleclient_hotspot" % prometheusVersion
val prometheusSimpleClientHttpserver = "io.prometheus" % "simpleclient_httpserver" % prometheusVersion
val prometheusSimpleClientPushGateway = "io.prometheus" % "simpleclient_pushgateway" % prometheusVersion

libraryDependencies ++= Seq(
  catsCore, catsEffect,
  http4sCore, http4sDsl, http4sBlazeServer, http4sCirce,
  circeGeneric, circeParser,
  prometheusSimpleClient, prometheusSimpleClientHotspot, prometheusSimpleClientHttpserver,
)