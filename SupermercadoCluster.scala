package ejercicios.DataFrames

import org.apache.log4j.{Level, Logger}
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._

object SupermercadoCluster {

  def main(args: Array[String]): Unit = {

    //Utilidad: Cambiar el log para que solo muestre errores y no saque tantas trazas
    val logger = Logger.getLogger("org.apache.spark")
    logger.setLevel(Level.ERROR)

    val spark = SparkSession.builder()
      .appName("Victor_Valero-Ejercicio4Spark")
      .getOrCreate()

    val hdfsPath = "/sparkBatch"

    //Lectura de los ficheros HDFS
    val mArticulos = spark.read.format("csv").option("header", "true").load(hdfsPath + "/maestroART.csv")
    val mTiendas = spark.read.format("csv").option("header", "true").load(hdfsPath + "/maestroTIEND.csv")
    val articulosDesc = spark.read.format("csv").option("header", "true").load(hdfsPath + "/descripcionesART.csv")
                             .filter(col("language") === "EN")
    val tiendasDesc = spark.read.format("csv").option("header", "true").load(hdfsPath + "/descripcionesTIEND.csv")
                           .dropDuplicates("store_code")

    val stock = spark.read.format("csv").option("header", "true").load(hdfsPath + "/stock.csv")
    val ventas = spark.read.format("csv").option("header", "true").load(hdfsPath + "/ventas.csv")
    val pedidos = spark.read.format("csv").option("header", "true").load(hdfsPath + "/pedidos.csv")

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

    //Escritura en HDFS
    reporte
      .write.format("csv")
      .option("header", "true")
      .option("path", hdfsPath+"/reporte")
      .mode("overwrite")
      .save()




  }

}
