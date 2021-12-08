package ejercicios.DataFrames

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.{Column, SparkSession}
import org.apache.spark.sql.functions.{col, desc, regexp_replace, split, sum, max}

object Tweets {

  def main(args: Array[String]): Unit = {

    //Utilidad: Cambiar el log para que solo muestre errores y no saque tantas trazas
    val logger = Logger.getLogger("org.apache.spark")
    logger.setLevel(Level.ERROR)

    //4.A Crear el SparkSession
    val spark = SparkSession
      .builder()
      .appName("Ejercicio DataFrames Spark")
      .master("local[*]")
      .getOrCreate()

    //4.B Leer el JSON “resources/tweets.2014102722” mediante Spark e imprimir el esquema con todas las columnas.
    println("------ 4.B -------")
    val data = spark.read.json("src/main/scala/resources/tweets.2014102722")
    data.printSchema()

    // 4.C Generar un DataFrame que tenga solo las columnas id, createdAt y text y añadir una nueva columna "retweet"
    // donde el valor será true si el texto contiene "RT" y falso en caso contrario.
    println("------ 4.C -------")
    //Manera elegante de seleccionar columnas: Pasar una lista con las columnas que queremos
    val listaColumnas = List("id","createdAt","text")
    val tweets = data.select(listaColumnas.head, listaColumnas.tail:_*)
                      .withColumn("retweet", col("text").contains("RT"))
    tweets.show()

    // 4.D Partiendo de la columna "createdAt" del DF anterior crear tres columnas con el dia, mes y año. Se puede hacer uso de la función split de spark
    println("------ 4.D -------")
    //Separamos la lógica de cálculo de la columna para que se entienda mejor
    val month: Column = split(col("createdAt")," ").getItem(0)
    val day: Column = regexp_replace(split(col("createdAt")," ").getItem(1),",","")
    val year: Column = split(col("createdAt")," ").getItem(2)

    tweets.withColumn("day", day)
      .withColumn("month", month)
      .withColumn("year", year)
      .drop("createdAt")
      .show()

    // 4.E Crear un DataFrame solo con datos de usuarios (podemos acceder a los campos de user seleccionando user.* )
    // y obtener los 10 usuarios con más followers
    println("------ 4.E -------")
    val usuarios = data.select("user.*")
    usuarios.groupBy("name")
            .agg(max(col("followersCount")).alias("followers"))
            .orderBy(desc("followers"))
            .limit(10)
            .show()

    // 5.A Partiendo del DF leído en el ejercicio 4.B vamos a crear una tabla temporal
    //para leerla después con querys SQL y obtener el campo "user.name" y "text"
    println("------ 5.A -------")
    data.createOrReplaceTempView("tweets")
    spark.sql("SELECT user.name, text FROM tweets").show()


  }

}
