ThisBuild / scalaVersion     := "2.13.12"
ThisBuild / version          := "0.1.0-SNAPSHOT"
ThisBuild / organization     := "com.example"
ThisBuild / organizationName := "example"

val AkkaVersion = "2.8.0"
val AkkaHttpVersion = "10.5.2" // Ajout de la variable manquante

lazy val root = (project in file("."))
  .settings(
    name := "Akka1",
    version := "0.1",
    libraryDependencies ++= Seq(
            "com.typesafe.akka" %% "akka-actor-typed" % AkkaVersion,
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-core" % AkkaHttpVersion,
      "com.typesafe.akka" %% "akka-http" % AkkaHttpVersion, // Pour Akka HTTP
      "ch.qos.logback" % "logback-classic" % "1.4.11", // Correction de `%%` en `%`
      "ch.megard" %% "akka-http-cors" % "1.2.0", // Pour gérer le CORS
      "org.slf4j" % "slf4j-api" % "1.7.36", // Correction de `%%` en `%`
      "org.slf4j" % "slf4j-simple" % "1.7.36", // Correction de `%%` en `%`
      "com.typesafe.slick" %% "slick" % "3.4.1",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.4.1",
      "org.postgresql" % "postgresql" % "42.5.0",
      "com.softwaremill.sttp.client3" %% "core" % "3.8.3",
      "com.softwaremill.sttp.client3" %% "circe" % "3.8.3",
      "io.circe" %% "circe-parser" % "0.14.1",
      "io.circe" %% "circe-core" % "0.14.1",
      "io.circe" %% "circe-generic" % "0.14.1",
      "io.circe" %% "circe-parser" % "0.14.1"
    )
  )
