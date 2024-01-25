package Consorcio

import java.nio.file.{Path, Paths}
import varios.GeringosoConverter
import varios.TextManipulator
import contable.Mortgage
import varios.BouncingBall
import contable.Camion
import contable.Buscador


object MainClass {
  def main(args: Array[String]): Unit = {
    // Crear una instancia de GeringosoConverter
    val geringosoInstance = new GeringosoConverter()
 

    // Usar la instancia para procesar una lista de palabras
    val result = geringosoInstance.processList(List("pera", "mandarina", "naranja"))

    // Mostrar el resultado
    println(result)

    
    // Ejemplo de uso
    val mortgage = new Mortgage(
      principal = 500000.0,
      annualInterestRate = 0.05,
      monthlyPayment = 2684.11,
      loanTermYears = 30,
      adelanta = 1000,
      adelantaStartMonth = 60,
      adelantaEndMonth = 108
    )

    mortgage.calculateTotalPaid()
    mortgage.displayResult()

  // Ejemplo de uso
  val textManipulator = new TextManipulator("Some werds ending with e to neutralize.")
  val manipulatedText = textManipulator.manipulateText
  println(manipulatedText)
  //import contable.ProcesadorCamion._
  //val procesadorCamionInstance = new ProcesadorCamion()
  //val resultBalance = procesadorCamionInstance.mostrarPantalla()


  // Ejemplo de uso de la clase BouncingBall
  val initialHeight: Double = 100.0
  val bounces: Int = 12

  val bouncingBall = new BouncingBall(initialHeight, bounces)
  bouncingBall.simulateBounces()

  val nombreArchivo = Paths.get(s"./src/resources/camion.csv")
  val costo = Camion.costo(nombreArchivo)
  println(s"Costo total: $costo")

  val filePath = Paths.get(s"./src/resources/precios.csv")
  val precio = Buscador.precio("Cebolla",filePath)
  println(s"El precio de la Cebolla es: $precio")

  }
}
