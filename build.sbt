name := "startup-reserves"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  "org.postgresql" % "postgresql" % "9.4-1201-jdbc41",
  ws,
  "org.jscience" % "jscience" % "4.3.1"
)

libraryDependencies <+= scalaVersion("org.scala-lang" % "scala-compiler" % _ )

resolvers += "http://127.0.0.1:8080 releasessnapshots" at "http://127.0.0.1:8080/repository/snapshots/"

credentials += Credentials("snapshots", "http://127.0.0.1:8080/repository/snapshots/", "admin", "apache2017")

publishTo <<= version { (v: String) =>
  val nexus = "http://localhost:8080"
  if (v.trim.endsWith("SNAPSHOT"))
    Some("snapshots" at nexus + "/repository/snapshots/")
  else
    Some("internal"  at nexus + "/repository/internal/")
}

pomExtra :=
  <scm>
    <url>https://github.com/dimerick/startup-reserves</url>
    <connection>scm:git:git@github.com:ExNexu/akka-actor-locking.git</connection>
  </scm>
    <developers>
      <developer>
        <id>exnexu</id>
        <name>Erick Saenz</name>
        <url>http://elatlas.org</url>
      </developer>
    </developers>

routesGenerator := InjectedRoutesGenerator