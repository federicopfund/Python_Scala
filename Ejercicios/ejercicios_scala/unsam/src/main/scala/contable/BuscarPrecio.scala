package contable

import scala.io.Source
import java.nio.file.Path

object Buscador {
  def precio(fruta: String, filePath: Path): Double = {
    val preciosFile = Source.fromFile(filePath.toString, "UTF-8")

    try {
      var costo = 0.0
      for (line <- preciosFile.getLines()) {
        val nuevaLinea = line.split(",")
        val opcion = nuevaLinea(0)

        if (opcion == fruta) {
          costo = nuevaLinea(1).toDouble
        }
      }

      if (costo == 0.0) {
        println(s"$fruta no figura en el listado de precios")
      }

      costo
    } catch {
      case _: Throwable =>
        println("Error al buscar el precio.")
        0.0
    } finally {
      preciosFile.close()
    }
  }
}