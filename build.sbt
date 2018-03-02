name := "play-scala-cassandra"
organization := "com.experiments.calvin"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.4"

scalacOptions += "-Ypartial-unification"

resolvers += Resolver.jcenterRepo

libraryDependencies ++= {
  val phantom  = "com.outworkers"
  val phantomV = "2.20.2"
  Seq(
    guice,
    phantom                  %% "phantom-dsl"        % phantomV,
    phantom                  %% "phantom-jdk8"       % phantomV,
    "org.typelevel"          %% "cats-core"          % "1.0.1",
    "org.scalatestplus.play" %% "scalatestplus-play" % "3.1.2" % Test
  )
}

// Enable dection of YAML in the views folder and treat them like plain text templates
TwirlKeys.templateFormats += ("yaml" -> "play.twirl.api.TxtFormat")

scalafmtOnCompile in ThisBuild := true
