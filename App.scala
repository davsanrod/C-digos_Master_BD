package ejercicios.segundo

//Ejercicio 2
// A. Crear una clase coche con color, modelo y velocidad
// B. Crear una función que obtenga los atributos del coche por pantalla
// C. Instanciar la clase Coche en una aplicación
// D. Imprimir por pantalla desde la aplicacion la velocidad del coche
// E. Crear una funcion dentro de coche que permita acelerar una determinada velocidad
// F. Añadir logica a la funcion de acelerar para no pasar nunca de 120
// G. Crear una fabrica de coches en nuevo objeto Singleton que cree coches parados y que reciba solo el color y el modelo
// H. Añadir una funcionalidad a la fabrica de coches que compruebe si el color y modelo especificados pueden crearse y sino devuelva un mensaje disculpandose con el comprador
//    y encargar un modelo disponible y otro que no exista

object App {
  def main(args: Array[String]): Unit = {
    val coche = new Coche("blanco" , modelo= "opel", velocidad= 100)
    coche.obtenerAtributos()
  }
}

