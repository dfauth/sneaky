name := "sneakyProxy"

version := "0.1-SNAPSHOT"

scalaVersion := "2.12.6"

val akkaVersion = "2.5.23"
val akkaHttpVersion = "10.1.10"


val scalactic = "org.scalactic" %% "scalactic" % "3.0.5"
val scalatest = "org.scalatest" %% "scalatest" % "3.0.5" % "test"
val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
val scalaLogging = "com.typesafe.scala-logging" %% "scala-logging" % "3.9.0"
val akkaHttp = "com.typesafe.akka" %% "akka-http"   % akkaHttpVersion
val akkaStream = "com.typesafe.akka" %% "akka-stream" % akkaVersion
val akkaHttpSprayJson = "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion
val testNg = "org.testng" % "testng" % "6.14.3" % Test
val slf4jApi = "org.slf4j" % "slf4j-api" % "1.7.30"
val guice = "com.google.inject" % "guice" % "4.2.2"
val ioSpray = "io.spray" %% "spray-json" % "1.3.5"


val commonScalaDeps = Seq(scalactic, scalatest, scalaLogging, logback)


lazy val root = (project in file("."))
  .settings(
    libraryDependencies ++= commonScalaDeps ++ Seq(
      testNg,
      guice,
      slf4jApi,
      akkaHttp,
      akkaStream,
      akkaHttpSprayJson,
      ioSpray
    )
  ).enablePlugins(JavaAppPackaging)
   
  
  .enablePlugins(DockerPlugin)
mainClass in Compile := Some("com.github.dfauth.sneaky.Main")
dockerExposedPorts += 8080

