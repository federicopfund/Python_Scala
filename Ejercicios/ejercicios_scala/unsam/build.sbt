val sparkVersion = settingKey[String]("Unsam Consorcio data")

lazy val root = (project in file(".")).settings(
  inThisBuild(List(
    organization := "data",
    scalaVersion := "2.12.13"
  )),
  name := "Unsam",
  version := "0.0.1",

  sparkVersion := "3.3.0",

  libraryDependencies ++= Seq(
    "org.apache.spark" %% "spark-core" % "3.3.0",
    "org.apache.spark" %% "spark-sql" % "3.3.0",
    "org.scalatest" %% "scalatest" % "3.2.2" % "test",
    "org.scalanlp" %% "breeze" % "1.2",
    "org.scalanlp" %% "breeze-natives" % "1.2",
    "org.scalanlp" %% "breeze-viz" % "1.2",
    "org.apache.hadoop" % "hadoop-common" % "2.10.1" % "provided",
    "org.apache.hadoop" % "hadoop-hdfs" % "2.10.1" % "provided",
    "org.apache.spark" %% "spark-mllib" % "3.2.0",
   
  ),

  Global / excludeLintKeys += sparkVersion,

  
  // Fork the JVM process to apply javaOptions to the application
  fork := true,

  // Configure Java options for the forked process
  javaOptions ++= Seq(
    "-server",
    "-Xms2g",
    "-Xmx4g",
    "-XX:MaxMetaspaceSize=512m",
    "-XX:+HeapDumpOnOutOfMemoryError",
    "-XX:HeapDumpPath=/path/to/dump"
    // Add other Java options as needed
  )
)