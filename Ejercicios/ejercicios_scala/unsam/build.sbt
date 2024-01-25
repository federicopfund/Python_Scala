// give the user a nice default project!

val sparkVersion = settingKey[String]("Unsam Consorcio data")

lazy val root = (project in file(".")).

  settings(
    inThisBuild(List(
      organization := "data",
      scalaVersion := "2.12.13"
    )),
    name := "Unsam",
    version := "0.0.1",

    sparkVersion := "3.3.0",

    libraryDependencies ++= Seq(

       "org.apache.spark" %% "spark-core" %  "3.3.0",
       "org.apache.spark" %% "spark-sql" %  "3.3.0",

      "org.scalatest" %% "scalatest" % "3.2.2" % "test",
   
    ),
   
    Global / excludeLintKeys += sparkVersion
  )
