package Consorcio


import varios.GeringosoConverter
import varios.TextManipulator
import varios.ProcesadorCamion
import java.nio.file.{Paths, Path}
import contable.Mortgage

object MainClass {
  def main(args: Array[String]): Unit = {
    // Crear una instancia de GeringosoConverter
    val geringosoInstance = new GeringosoConverter()
    val procesadorCamionInstance = new ProcesadorCamion()

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
  val pathCamion: Path = Paths.get("resources", "camion.csv")
  val pathPrecio: Path = Paths.get("resources", "precios.csv")
  val resultBalance = procesadorCamionInstance.main(pathCamion,pathPrecio)

  }
}
