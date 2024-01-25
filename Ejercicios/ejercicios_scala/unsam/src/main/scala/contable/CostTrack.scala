package contable

import scala.io.Source
import java.nio.file.Path


object Camion {
  def costo(nombreArchivo: Path): Double = {
    try {
      val a = Source.fromFile(nombreArchivo.toFile)
      val rows = a.getLines().drop(1).map(_.split(","))
      var precioFinal = 0.0
      var contador = 0

      for (line <- rows) {
        contador += 1
        try {
          val cajon = line(1).toInt
          val precio = line(2).toDouble
          precioFinal += cajon * precio
        } catch {
          case _: Throwable => println(s"La fila $contador se encuentra vacía. ${line.mkString(", ")}")
        }
      }

      a.close()
      precioFinal
    } catch {
      case _: Throwable =>
        println(s"${nombreArchivo.toString}\nNo corresponde a una dirección válida.")
        0.0
    }
  }
}