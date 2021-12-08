package ejercicios.DataFrames

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.functions.{col, lit}
import org.apache.spark.sql.types._
import org.apache.spark.sql.{Row, SparkSession}

object Carrera {

  def main(args: Array[String]): Unit = {

    //Utilidad: Cambiar el log para que solo muestre errores y no saque tantas trazas
    val logger = Logger.getLogger("org.apache.spark")
    logger.setLevel(Level.ERROR)

    //3.A Crear el SparkSession
    val spark = SparkSession
      .builder()
      .appName("Ejercicio DataFrames Spark")
      .master("local[*]")
      .getOrCreate()

    val sc = spark.sparkContext //Necesario para crear el DataFrame en 1.B

    //3.B Crear un DataFrame con los datos de la carrera y el esquema de los datos y almacenarlo en la variable corredores
    val dataRace = Seq(
      Row("0001", "Maria",  "del Monte", 4,  false),
      Row("0002", "Juan",   "Garcia",    9,  false),
      Row("0003", "Miguel", "Cervantes", 35, false),
      Row("0004", "Alicia", "Fernandez", 30, false)
    )

    val schema = StructType(
      List(
        StructField("idRunner", StringType, false),
        StructField("nombre", StringType, false),
        StructField("apellido", StringType, false),
        StructField("puesto", IntegerType, false),
        StructField("podio", BooleanType, false)
      )
    )

    val corredores = spark.createDataFrame(sc.parallelize(dataRace), schema)

    //3.C Seleccionar las columnas "idRunner" y "puesto" e imprimir el resultado en formato tabular
    println("------ 3.C -------")
    corredores.select("idRunner", "puesto").show()

    //3.D Eliminar la columna podio e imprimir el esquema del nuevo DF
    println("------ 3.D -------")
    println(corredores.drop("podio").schema)

    //3.E Renombrar la columna "idRunner" a "idCorredor" y crear una nueva columna al DF que sea kilometros con el literal (funcion lit en spark) "10". Asignarlo a una nueva variable corredores2.
    println("------ 3.E -------")
    val corredores2 = corredores.withColumnRenamed("idRunner", "idCorredor")
                                 .withColumn("kilometros", lit("10"))
    corredores2.show()

    //3.F Cruzar los datos de corredores2 con el DF de carrera y almacenarlo en una variable corredoresTiempo
    println("------ 3.F -------")
    import spark.implicits._
    val carrera = Seq(("0001", 50.3),
                      ("0002", 52.7),
                      ("0003", 81.3),
                      ("0004", 80.2)).toDF("idCorredor", "tiempo") //Manera alternativa de crear un DF infiriendo schema (necesita el spark.implicts)

    val corredoresTiempo = corredores2.join(carrera, Seq("idCorredor"))
    corredoresTiempo.show()

    //3.G Calcular la columna "min/km" con el ritmo de minutos / km de cada corredor (se puede hacer uso de la función col) y filtrar los corredores que tengan un ritmo menor a 7 min/km
    println("------ 3.G -------")
    corredoresTiempo.withColumn("min/km", col("tiempo")/col("kilometros"))
                    .filter(col("min/km") < 7.0).show()



  }

}
