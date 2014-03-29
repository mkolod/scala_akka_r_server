import AssemblyKeys._
import de.johoop.findbugs4sbt.FindBugs._

Seq(assemblySettings: _*)

addCompilerPlugin("org.brianmckenna" %% "wartremover" % "0.8")

scalacOptions in (Compile, compile) += "-P:wartremover:only-warn-traverser:org.brianmckenna.wartremover.warts.Unsafe"

//scalacOptions in (Compile, compile) += "-P:wartremover:traverser:org.brianmckenna.wartremover.warts.Unsafe"

org.scalastyle.sbt.ScalastylePlugin.Settings

CodeQualityBuild.Settings

//PmdSettings

findbugsSettings

findbugsReportType := Some(de.johoop.findbugs4sbt.ReportType.FancyHtml)

findbugsReportPath := Some(crossTarget.value / "findbugs.html")

//codequality.CodeQualityPlugin.Settings

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

jarName in assembly := "spray-akka-r-server-job-assembly.jar"

test in assembly := {}

mainClass in assembly := Some("us.marek.akka.Boot")

name := "demo"

version := "1.0"

organization := "us_marek"

scalaVersion := "2.10.3"

scalaSource in Compile <<= baseDirectory(_ / "src/main/scala")

scalaSource in Test <<= baseDirectory(_ / "src/test/scala")

javaSource in Compile <<= baseDirectory(_ / "src/main/java")

javaSource in Test <<= baseDirectory(_ / "src/test/java")

resourceDirectory in Compile := baseDirectory.value / "resources"

resourceDirectory in Test := baseDirectory.value / "test-resources"

resolvers ++= Seq("Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
                  "Maven Central" at "http://repo1.maven.org",
                  "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/",
                  "Sonarype releases" at "https://oss.sonatype.org/content/repositories/releases/",
                  "Spray Repo" at "http://repo.spray.io")

  libraryDependencies ++= {
    val akkaVersion = "2.3.0"
    val sprayV = "1.3.0"
    Seq(
      "io.spray"             %     "spray-can"                 %    sprayV,
      "io.spray"             %     "spray-routing"             %    sprayV,
      "io.spray"             %     "spray-testkit"             %    sprayV          %   "test",
      "com.typesafe.akka"    %%    "akka-actor"                %    akkaVersion,
      "com.typesafe.akka"    %%    "akka-remote"               %    akkaVersion,
      "com.typesafe.akka"    %%    "akka-slf4j"                %    akkaVersion,
      "com.typesafe.akka"    %%    "akka-testkit"              %    akkaVersion,
      "com.typesafe.akka"    %%    "akka-kernel"               %    akkaVersion,
      "org.json4s"           %%    "json4s-native"             %    "3.2.4",
      "org.scalatest"        %%    "scalatest"                 %    "1.9.1"         %    "test",
      "com.github.axel22"    %%    "scalameter"                %    "0.4",
      "net.sourceforge.findbugs"             %     "annotations"               %    "1.3.2"         %    "compile"
    )
  }
  

EclipseKeys.useProjectId := false

EclipseKeys.withSource := true

javacOptions ++= Seq("-source", "1.7", "-target", "1.7")

scalacOptions ++= Seq("-unchecked", "-deprecation")

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

fork in Test := false

javaOptions += "-Xmx2G"

parallelExecution := true

parallelExecution in Test := false