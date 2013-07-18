import sbt._
import sbt.Keys._

object SlickdataBuild extends Build {

  lazy val slickdata = Project(
    id = "slick-data",
    base = file("."),
    settings = Project.defaultSettings ++ Seq(
      name := "slick-data",
      organization := "fr.viadeo",
      version := "0.1-SNAPSHOT",
      scalaVersion := "2.10.1",
      // add other settings here
      libraryDependencies ++= List(
        // use the right Slick version here:
        "com.typesafe.slick" %% "slick" % "1.0.0",
        "org.slf4j" % "slf4j-nop" % "1.6.4",
        "com.h2database" % "h2" % "1.3.166",
        "mysql" % "mysql-connector-java" % "5.1.23",
        "org.xerial" % "sqlite-jdbc" % "3.7.2"
      )
    )
  )
}
