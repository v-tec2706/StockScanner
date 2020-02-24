name := "StockScanner"

version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies += "net.ruippeixotog" %% "scala-scraper" % "2.2.0"
libraryDependencies += "net.sourceforge.htmlcleaner" % "htmlcleaner" % "2.6.1"
libraryDependencies += "org.apache.kafka" %% "kafka" % "2.1.0"
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.4.4"
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.4"
libraryDependencies += "com.fasterxml.jackson.module" % "jackson-module-scala" % "2.10.0"
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.10.0"

dependencyOverrides ++= {
  Seq(
    "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.6.7.1",
    "com.fasterxml.jackson.core" % "jackson-databind" % "2.6.7.1",
  )
}