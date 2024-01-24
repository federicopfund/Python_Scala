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

      "org.apache.spark" %% "spark-sql" % "3.3.0" % "provided",

      "org.scalatest" %% "scalatest" % "3.2.2" % "test",
      //"org.scalatest" %% "scalatest" % "3.2.10" % "test"
     
    ),
   
  )
