package contable

import scala.io.Source
import java.nio.file.Path

/**
  * Objeto Buscador para buscar el precio de una fruta en un archivo de precios.
  */
object Buscador {

  /**
    * Busca el precio de una fruta en un archivo y devuelve el costo.
    *
    * @param fruta    Nombre de la fruta cuyo precio se busca.
    * @param filePath Ruta del archivo de precios.
    * @return Precio de la fruta. Si la fruta no está en el listado, se imprime un mensaje y se devuelve 0.0.
    */
  def precio(fruta: String, filePath: Path): Double = {
    // Abre el archivo de precios utilizando Scala.io.Source
    val preciosFile = Source.fromFile(filePath.toString, "UTF-8")

    try {
      var costo = 0.0
      // Itera sobre cada línea del archivo
      for (line <- preciosFile.getLines()) {
        val nuevaLinea = line.split(",")
        val opcion = nuevaLinea(0)

        // Compara la fruta actual con la fruta buscada y actualiza el costo si coincide
        if (opcion == fruta) {
          costo = nuevaLinea(1).toDouble
        }
      }

      // Imprime un mensaje si la fruta no está en el listado de precios
      if (costo == 0.0) {
        println(s"$fruta no figura en el listado de precios")
      }

      costo
    } catch {
      case _: Throwable =>
        // Imprime un mensaje de error si ocurre un problema durante la búsqueda
        println("Error al buscar el precio.")
        0.0
    } finally {
      // Cierra el archivo al finalizar
      preciosFile.close()
    }
  }
}
