package Consorcio

import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.SparkSession
import java.nio.file.{Path, Paths}
import varios.GeringosoConverter
import varios.TextManipulator
import contable.Mortgage
import varios.BouncingBall
import contable.Camion
import contable.Buscador
import botanica.ArboladoParque
import Operation.BusquedaBinaria
import Operation.CsvParserRecursive
import varios.Propagacion
import varios.AlbumFigus._
import plotter.plt.plotfigus
import varios.TermometroGenerador
import plotter._
import breeze.linalg._
import simulacion.ProbabilidadGenerala
import varios._
import contable._
import org.apache.log4j.PropertyConfigurator
import botanica._

object MainClass {
  def main(args: Array[String]): Unit = {
    
  // Crear una instancia de GeringosoConverter
  //  val geringosoInstance = new GeringosoConverter()
 

  // Usar la instancia para procesar una lista de palabras
  //  val result = geringosoInstance.processList(List("pera", "mandarina", "naranja"))

  // Mostrar el resultado
  //  println(result)

    
  // Ejemplo de uso Hipoteca
  //  val mortgage = new Mortgage(
  //    principal = 500000.0,
  //    annualInterestRate = 0.05,
  //    monthlyPayment = 2684.11,
  //    loanTermYears = 30,
  //    adelanta = 1000,
  //    adelantaStartMonth = 60,
  //    adelantaEndMonth = 108
  //  )

  //  mortgage.calculateTotalPaid()
  //  mortgage.displayResult()

  // Ejemplo de uso
  //  val textManipulator = new TextManipulator("Some werds ending with e to neutralize.")
  //  val manipulatedText = textManipulator.manipulateText
  //  println(manipulatedText)
  //  import contable.ProcesadorCamion._
  //  val procesadorCamionInstance = new ProcesadorCamion()
  //  val resultBalance = procesadorCamionInstance.mostrarPantalla()


  // Ejemplo de uso de la clase BouncingBall
  //  val initialHeight: Double = 100.0
  //  val bounces: Int = 12

  //  val bouncingBall = new BouncingBall(initialHeight, bounces)
  //  bouncingBall.simulateBounces()

  //  val nombreArchivo = Paths.get(s"./src/resources/camion.csv")
  //  val costo = Camion.costo(nombreArchivo)
  //  println(s"Costo total: $costo")

  //  val filePath = Paths.get(s"./src/resources/precios.csv")
  //  val precio = Buscador.precio("Cebolla",filePath)
  //  println(s"El precio de la Cebolla es: $precio")

  
  //  val arboleda = ArboladoParque.leerParque(Paths.get("./src/resources/arbolado-en-espacios-verdes.csv"))
  //  arboleda.foreach(println)
  // Obtener el DataFrame del archivo
  //  val arboladoDF : DataFrame = ArboladoParque.leerParque(Path.of("./src/resources/arbolado-en-espacios-verdes.csv"))

  // Calcular la altura promedio de los Jacarandás
  //  val alturaPromedio = ArboladoParque.alturaPromedioJacaranda(arboladoDF)
  //  Calcula el diametro y alto
  //  val altosYDiametrosJacaranda = ArboladoParque.altosYDiametrosJacaranda(arboladoDF)
  //  println(s"Altura promedio de los Jacarandás: $alturaPromedio")
  //  println(s"Altura y diametro de los Jacarandás: $altosYDiametrosJacaranda")


  //  val especies: List[String] = List("Eucalipto", "Palo borracho rosado", "Jacarandá")
  //  val medidas: Map[String, List[(Double, Double)]] = ArboladoParque.medidasDeEspecies(especies, arboladoDF)

  // Imprimir las medidas para cada especie
  //  medidas.foreach { case (especie, listaMedidas) =>
  //    println(s"Primeras 15 medidas para $especie:")
  //    listaMedidas.take(15).foreach { case (altura, diametro) =>
  //     println(s"Altura: $altura, Diámetro: $diametro")
  //  }
  //  println("------")
  //  }

  // Ejemplos de uso busqueda binaria
  // val resultadoBusqueda = BusquedaBinaria.run(List(1, 2, 4, 6, 8, 6, 7, 5, 6), 5, verbose = true)
  // val filePathcsv = Paths.get(s"./src/resources/arbolado-en-espacios-verdes.csv")
  
  // val select: Option[List[String]] = Some(List("id_arbol", "altura_tot", "diametro"))
  // val types: Option[List[String => Any]] = Some(List( _.toInt,_.toInt, _.toDouble))
  // val resultado = CsvParserRecursive.run(filePathcsv, select,types)
  
  //  val propagars1 = Propagacion.run(List(0, 0, 0, -1, 1, 0, 0, 0, -1, 0, 1, 0, 0))
  //  println(propagars1)

  //  Otro ejemplo de propagación en una lista diferente
  //  val propagars2 = Propagacion.run(List(0, 0, 0, 1, 0, 0))
  //  println(propagars2)


  // val figusTotal = 670
  // val figusPaquete = 5

  // Calcular la historia de figuritas pegadas
  // val historiaFigusPegadas = calcularHistoriaFigusPegadas(figusTotal, figusPaquete)
  
  // Generar temperaturas
  // Generar temperaturas
  // val temperaturas =  TermometroGenerador.generarTemperaturas(100, 25.0)
  // println(temperaturas)
  // Mostrar estadísticas y generar gráfico
  // plt.plottermo(temperaturas)
  // Juego de dados
  // ProbabilidadGenerala.run(10000)
  // Torre de CControl
  // Crear una instancia inicial de la Torre de Control
  // Llamada básica a las funciones de la clase TorreDeControl
  // val torreInicial = new TorreDeControl()
  // val torreConArribo = torreInicial.nuevoArribo("Vuelo1")
  // val torreConPartida = torreConArribo.nuevaPartida("Vuelo2")
  // torreConPartida.verEstado()
  // val torreDespuesDeAsignar = torreConPartida.asignarPista()
  // torreDespuesDeAsignar.verEstado()
  //<-------Datos de financieros---------->
  // val dataPath : String = "./src/resources/dataset/dowstocks.csv"
  // val outputPath : String = "./src/resources/dataset/parquet/Stockdata.parquet"
  // val arboleda = StockDataFinancial.run(dataPath,outputPath)

  MareasSpark.run(Array(s"OBS_SHN_SF-BA.csv"))
  }
}