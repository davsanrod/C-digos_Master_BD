package ejercicios.RDD

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object Logs {

  def main(args: Array[String]): Unit = {

    //Utilidad: Cambiar el log para que solo muestre errores y no saque tantas trazas
    val logger = Logger.getLogger("org.apache.spark")
    logger.setLevel(Level.ERROR)

    val spark = SparkSession
      .builder()
      .appName("Ejercicio 3 RDD Spark")
      .master("local[*]") //USAR SOLO EN LOCAL, NUNCA EN CLUSTER
      .getOrCreate()

    val sc = spark.sparkContext

    //Lectura del fichero de logs
    val dataPath: String = "src/main/scala/resources/example.log"
    val data: RDD[String] = sc.textFile(dataPath,1)
    val logsLines: RDD[Array[String]] = data.map(line => line.split("\\|"))

    //Cacheado del RDD para optimizar el rendimiento
    logsLines.cache()

    //3.A - Número de paises diferentes
    println(logsLines.map(line => line(2)).distinct().count())
    //Alternativa
    val countryLogs: RDD[Tuple2[String,Int]] = logsLines.map(line => (line(2),1)).reduceByKey((x, y) => x + y)
    println(countryLogs.collect().length)

    //3.B - Número de logs por tipo
    logsLines.map(line => (line(3),1)).reduceByKey(_ + _).foreach(println)

     //3.C - Top 10 paises
    countryLogs.map(x=> (x.country, 1)).reduceByKey((x,y) =>x + y).sortBy(x=> x._2, false).take(10).foreach(println)

    //3.D - Cuándo comenzaron los errores
    println(logsLines.filter(line => line(3) == "ERROR").map(line => line(0)).min())

    spark.stop()


  }

}
