package ejercicios.RDD

import org.apache.log4j.{Level, Logger}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

object CuentaPalabras {

  def main(args: Array[String]): Unit = {

    //Utilidad: Cambiar el log para que solo muestre errores y no saque tantas trazas
    val logger = Logger.getLogger("org.apache.spark")
    logger.setLevel(Level.ERROR)

    //2.A Crear el SparkSession y obtener de el su SparkContext
    val spark = SparkSession
      .builder()
      .appName("Ejercicio 2 RDD Spark")
      .master("local[*]")  //USAR SOLO EN LOCAL, NUNCA EN CLUSTER
      .getOrCreate()

    val sc = spark.sparkContext

    //2.B Leer los datos de tweets e imprimir la primera línea
    val data = "src/main/scala/resources/Apache Spark Introduccion"
    val intro: RDD[String] = sc.textFile(data)
    println(intro.first())

    //2.C Contar el número de veces que aparece la palabra "Spark" en el artículo
    val sparkWord = intro.flatMap(x => x.split(" ")).filter(x => x.contains("Spark") )
    println("la palabra Spark está " + sparkWord.count() + " en el artículo")

    //2.D Contar el número de veces que aparece cada palabra y guardarlo en un directorio
    val words = intro.flatMap(x => x.split(" ")).map(word => (word, 1)).reduceByKey((x,y) => x + y )
    words.saveAsTextFile("src/main/scala/ejercicios/RDD/palabras") //el directorio no debe existir o fallará. Debemos borrarlo para re-ejecutar

    spark.stop()
  }


}
