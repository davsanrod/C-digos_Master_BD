package ejercicios.cuarto
import java.io.File


//Ejercicio 4
// A - Importar el csv constituents-financials de la carpeta resources y leerlo con la librería scala-csv
// B - Aplicar el case class Financials para tipar las líneas y poder acceder a las columnas por su nombre
// C - Calcular el número de empresas del sector “Industrials” que tengan un MarketCap > 2000000
// D - Calcular el Price total de las acciones que contengan “Air” en su columna name
// E - Calcular cuantas acciones tienen precio por debajo de su un target price
// F - Si mi cartera se compone de las acciones cuyos Symbol son MMM, AAPL, MCD y PYPL y he comprado 10 acciones de cada una,
//      obtener el % que representa cada accion sobre el total de mi cartera

object App3 {

  def main(args: Array[String]): Unit = {

    case class Financial(Symbol: String,
                         Name: String,
                         Sector: String,
                         Price: Double,
                         PriceEarnings: Float,
                         DividendYield: Double,
                         EarningsShare: Double,
                         FiftyTwoWeekLow: Double,
                         FiftyTwoWeekHigh: Double,
                         MarketCap: Double,
                         EBITDA: Double,
                         PriceSales: Double,
                         PriceBook: Double,
                         SECFilings: String)


  }
}
