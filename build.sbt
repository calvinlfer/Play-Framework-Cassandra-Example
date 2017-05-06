name := """play-cassandra-example"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies ++= {
  import Dependencies._
  Seq(filters, scalaTestPlus, cats, quillCassandra, scalaGuice)
}
