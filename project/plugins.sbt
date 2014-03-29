addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.9.2")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")

addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.2.0")

addSbtPlugin("org.scalastyle" %% "scalastyle-sbt-plugin" % "0.4.0")

addSbtPlugin("de.johoop" % "findbugs4sbt" % "1.3.0")

libraryDependencies ++= Seq("com.puppycrawl.tools"  %     "checkstyle"                %    "5.6",
                            "net.sourceforge.pmd"   %     "pmd"                       %    "5.0.5")