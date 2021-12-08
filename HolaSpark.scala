package ejercicios.RDD

import org.apache.spark.sql.SparkSession

object HolaSpark {

  def main(args: Array[String]): Unit = {

    //1.C Generar el SparkSession con nombre de la App y el master
    val spark = SparkSession
      .builder()
      .appName("Ejercicio 1 RDD Spark")
      .master("local[*]") //USAR SOLO EN LOCAL, NUNCA EN CLUSTER
      .getOrCreate()

    //1.D Acceder al SparkContext del SparkSession e imprimir su versión
    val sc = spark.sparkContext
    println("La versión de spark es: " + sc.version)

    spark.stop()

  }

}
