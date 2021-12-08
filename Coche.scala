package ejercicios.segundo

class Coche (color: String, modelo: String, var velocidad: Double){
  def obtenerAtributos() ={
    println(color, modelo, velocidad)
  def acelerar(incremento: Double): Unit = {
      var nuevaV = (velocidad + incremento)
      velocidad = nuevaV
    }
  }
}