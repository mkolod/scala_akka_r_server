import AssemblyKeys._

Seq(assemblySettings: _*)

mergeStrategy in assembly <<= (mergeStrategy in assembly) { (old) =>
  {
    case PathList("META-INF", xs @ _*) =>
	    (xs map {_.toLowerCase}) match {
	      case ("manifest.mf" :: Nil) | ("index.list" :: Nil) | ("dependencies" :: Nil) => MergeStrategy.discard
	      case _ => MergeStrategy.discard
	  }
    case _ => MergeStrategy.first 
  }
}

jarName in assembly := "scalding-job-assembly.jar"

test in assembly := {}

mainClass in assembly := Some("com.visiblemeasures.dactar.scalding.useragent.Main")

name := "demo"

version := "1.0"

organization := "us_marek"

scalaVersion := "2.10.2"

scalaSource in Compile <<= baseDirectory(_ / "src/main/scala")

scalaSource in Test <<= baseDirectory(_ / "test/main/scala")

resourceDirectory in Compile := baseDirectory.value / "resources"

resourceDirectory in Test := baseDirectory.value / "test-resources"

resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
                  "Maven Central" at "http://repo1.maven.org")

{
  val akkaVersion = "2.3.0-RC2"
  libraryDependencies ++= Seq(
    "com.typesafe.akka"    %    "akka-actor_2.10"                %    akkaVersion,
    "com.typesafe.akka"    %    "akka-remote_2.10"               %    akkaVersion,
    "com.typesafe.akka"    %    "akka-slf4j_2.10"                %    akkaVersion,
    "com.typesafe.akka"    %    "akka-testkit_2.10"              %    akkaVersion,
    "com.typesafe.akka"    %    "akka-kernel_2.10"               %    akkaVersion,
    "org.scalatest"        %    "scalatest_2.10"                 %    "1.9.1"         %    "test",
    "com.github.axel22"    %    "scalameter_2.10"                %    "0.4"
  )
}

EclipseKeys.useProjectId := false

EclipseKeys.withSource := true

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

scalacOptions += "-deprecation"

EclipseKeys.projectFlavor := EclipseProjectFlavor.Scala

EclipseKeys.createSrc := EclipseCreateSrc.Default

pollInterval := 1000

shellPrompt <<= name(name => { state: State =>
	object devnull extends ProcessLogger {
		def info(s: => String) {}
		def error(s: => String) { }
		def buffer[T](f: => T): T = f
	}
	val current = """\*\s+(\w+)""".r
	def gitBranches = ("git branch --no-color" lines_! devnull mkString)
	"%s:%s>" format (
		name,
		current findFirstMatchIn gitBranches map (_.group(1)) getOrElse "-"
	)
})

fork := true

fork in Test := true

javaOptions += "-Xmx2G"

parallelExecution := false

parallelExecution in Test := false