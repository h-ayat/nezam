lazy val akkaHttpVer = "10.1.5"
lazy val akkaVer = "2.5.17"
lazy val scalaLibVersion = "2.12.7"

lazy val commonSettings = Seq(
  organization := "io.skul",
  scalaVersion := scalaLibVersion,
  scalacOptions ++= Seq("-feature")
)

lazy val core = project.
  settings(commonSettings: _*).
  settings(
    version := "1.0.0",
    name := "nezam-core",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.1" % "test"
    )
  )

lazy val gen = project.
  settings(commonSettings: _*).
  settings(
    version := "1.0.0",
    name := "nezam-gen",
    libraryDependencies ++= Seq(
      "io.skul" %% "proximo-model" % "0.0.1",
      "org.scalatest" %% "scalatest" % "3.0.1" % "test"
    )
  )


lazy val engine = project.
  settings(commonSettings: _*).
  settings(
    version := "1.0.0",
    name := "nezam-engine",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-actor" % akkaVer,
      "com.typesafe.akka" %% "akka-http" % akkaHttpVer,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVer,
      "org.scalatest" %% "scalatest" % "3.0.1" % "test"
    )
  ).
  dependsOn(core)

