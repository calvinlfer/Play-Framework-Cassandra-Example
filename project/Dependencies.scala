import sbt._

object Dependencies {
  val scalaTestPlus: ModuleID = "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test
  val cats: ModuleID = "org.typelevel" %% "cats" % "0.9.0"
  val quillCassandra: ModuleID = "io.getquill" %% "quill-cassandra" % "1.1.1"
  val scalaGuice: ModuleID = "net.codingwell" %% "scala-guice" % "4.1.0"
}