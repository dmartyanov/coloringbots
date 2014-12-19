name := "colorPro"

version := "1.0"

scalaVersion := "2.11.4"


def akka(v: String) = Seq(
  "com.typesafe.akka" %% "akka-actor"   % v,
  "com.typesafe.akka" %% "akka-remote"  % v,
  "com.typesafe.akka" %% "akka-slf4j"   % v,
  "com.typesafe.akka" %% "akka-testkit" % v % "test"
)

libraryDependencies ++=
  akka("2.3.4") ++
    Seq(
      "ch.qos.logback"              %   "logback-classic"     % "1.1.2",
      "com.typesafe.scala-logging"  %% "scala-logging"        % "3.1.0",
      "org.scalatest"               %%  "scalatest"           % "2.2.0"     % "test"
    )