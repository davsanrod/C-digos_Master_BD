name := "SparkRDDSoluciones"

version := "0.1"

scalaVersion := "2.11.10"

//Ejercicio1
//1.A Configurar el fichero build.sbt para incluir las librerias de Spark
val sparkVersion = "2.4.0"

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion,
  "org.apache.spark" %% "spark-hive" % sparkVersion
)

