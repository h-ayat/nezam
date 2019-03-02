lazy val akkaHttpVer = "10.1.5"
lazy val akkaVer = "2.5.17"
lazy val scalaLibVersion = "2.12.8"

lazy val commonSettings = Seq(
  organization := "io.skul",
  scalaVersion := scalaLibVersion,
  scalacOptions ++= Seq("-feature")
)

lazy val `nezam-core-api` = project.
  settings(commonSettings: _*).
  settings(
    version := "1.0.0",
    name := "nezam-core-api",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "3.0.1" % "test"
    )
  )

lazy val `nezam-core-model` = project.
  settings(commonSettings: _*).
  settings(
    version := "1.0.0",
    name := "nezam-core-model",
    libraryDependencies ++= Seq(  
      "org.scalatest" %% "scalatest" % "3.0.1" % "test",
      "com.typesafe.akka" %% "akka-actor" % "2.5.21"
    )
  )


