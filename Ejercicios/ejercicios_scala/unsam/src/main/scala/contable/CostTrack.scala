package contable

import scala.io.Source
import java.nio.file.Path

object Camion {

  /**
    * Calcula el costo total de la carga de un camión a partir de un archivo de datos.
    *
    * @param nombreArchivo Ruta del archivo que contiene los datos de la carga del camión.
    * @return Costo total de la carga del camión. Si hay filas vacías o errores en los datos, se imprime un mensaje y se devuelve 0.0.
    */
  def costo(nombreArchivo: Path): Double = {
    try {
      // Abre el archivo utilizando Scala.io.Source
      val a = Source.fromFile(nombreArchivo.toFile)
      
      // Obtiene las filas del archivo excluyendo la primera (encabezados)
      val rows = a.getLines().drop(1).map(_.split(","))
      
      var precioFinal = 0.0
      var contador = 0

      // Itera sobre cada línea del archivo
      for (line <- rows) {
        contador += 1
        try {
          // Intenta obtener el número de cajones y el precio de la línea actual
          val cajon = line(1).toInt
          val precio = line(2).toDouble
          // Calcula el costo total sumando el costo de cada línea
          precioFinal += cajon * precio
        } catch {
          case _: Throwable =>
            // Imprime un mensaje si hay una fila vacía o un error en los datos
            println(s"La fila $contador se encuentra vacía. ${line.mkString(", ")}")
        }
      }

      // Cierra el archivo al finalizar
      a.close()
      precioFinal
    } catch {
      case _: Throwable =>
        // Imprime un mensaje si hay un error al abrir el archivo
        println(s"${nombreArchivo.toString}\nNo corresponde a una dirección válida.")
        0.0
    }
  }
}