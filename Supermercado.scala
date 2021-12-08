package ejercicios.DataFrames

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.{col, count, current_timestamp, date_format, row_number, sum}
import org.apache.spark.sql.expressions.Window

object Supermercado {

  def main(args: Array[String]): Unit = {

    //Utilidad: Cambiar el log para que solo muestre errores y no saque tantas trazas
    val logger = Logger.getLogger("org.apache.spark")
    logger.setLevel(Level.ERROR)

    val spark = SparkSession.builder()
      .master("local")
      .appName("Ejercicio 4 DF")
      .getOrCreate()

    val path = "src/main/scala/resources"

    //Lectura de los ficheros en CSV
    val mArticulos = spark.read.format("csv").option("header", "true").load(path + "/maestroART.csv")
    val mTiendas = spark.read.format("csv").option("header", "true").load(path + "/maestroTIEND.csv")
    val articulosDesc = spark.read.format("csv").option("header", "true").load(path + "/descripcionesART.csv")
          .filter(col("language") === "EN")
    val tiendasDesc = spark.read.format("csv").option("header", "true").load(path + "/descripcionesTIEND.csv")
        .dropDuplicates("store_code")

    val stock = spark.read.format("csv").option("header", "true").load(path + "/stock.csv")
    val ventas = spark.read.format("csv").option("header", "true").load(path + "/ventas.csv")
    val pedidos = spark.read.format("csv").option("header", "true").load(path + "/pedidos.csv")

    //Cruce de los maestros con sus descripciones
    val articulos = mArticulos.join(articulosDesc, Seq("article_code"), "left")
    val tiendas = mTiendas.join(tiendasDesc, Seq("store_code"), "left")

    //Cruce para obtener el stock y los atributos de tiendas y artículos
    val stockEnriquecido = stock.join(articulos, Seq("id_article"), "left")
      .join(tiendas, Seq("id_store"), "left")

    //Lógica de cálculo del número de pedidos
    val pedidosAgrup = pedidos.groupBy("id_article", "id_store")
                                .agg(
                                    sum("quantity_ordered").as("sum_quantity_ordered"),
                                    count("id_order").as("num_orders"))

    //Lógica de cálculo de suma de ventas
    val ventasAgrup = ventas.groupBy("id_article", "id_store")
                                .agg(
                                    sum("sales_amount").as("sum_sales"))

    //Ventana definida por cada tienda
    val window = Window.partitionBy("id_store").orderBy(col("sum_sales").desc)

    //Cruce del stock con DF de pedidos y DF de ventas. Obtención de las columnas sales_ranking y insert_date
    val reporte = stockEnriquecido.join(pedidosAgrup, Seq("id_article", "id_store"), "left")
      .join(ventasAgrup, Seq("id_article", "id_store"), "left")
      .withColumn("sales_ranking", row_number().over(window))
      .withColumn("insert_date", date_format(current_timestamp(),"yyyy-MM-dd HH:mm:ss"))
      .select(
        col("insert_date"),
        col("article_code"),
        col("article_desc"),
        col("store_code"),
        col("store_desc"),
        col("stock_amount"),
        col("stock_measure"),
        col("num_orders"),
        col("sum_quantity_ordered"),
        col("sum_sales"),
        col("sales_ranking"))

    //Escritura en CSV cambiando el particionado a 1 para que solo genere un csv
    reporte.repartition(1)
      .write.format("csv")
      .option("header", "true")
      .option("path", path+"/reporte")
      .mode("overwrite")
      .save()




  }

}
