package varios

object AlbumFigus {
  
  import plotter.figus.plot

  val figusTotal: Int = 670
  val figusPaquete: Int = 5

  /**
   * Función para calcular la historia de las figuritas pegadas.
   *
   * @param figusTotal   Número total de figuritas en el álbum.
   * @param figusPaquete Cantidad de figuritas por paquete.
   * @return             Vector que representa la evolución de las figuritas pegadas en el tiempo.
   */
  def calcularHistoriaFigusPegadas(figusTotal: Int, figusPaquete: Int): Vector[Int] = {
    var album: Vector[Int] = Vector.fill(figusTotal)(0)
    var historiaFigusPegadas: Vector[Int] = Vector(0)

    /**
     * Función para simular la compra de un paquete de figuritas.
     *
     * @param figusTotal   Número total de figuritas en el álbum.
     * @param figusPaquete Cantidad de figuritas por paquete.
     * @return             Vector que representa las figuritas compradas en un paquete.
     */
    def comprarPaquete(figusTotal: Int, figusPaquete: Int): Vector[Int] =
      (1 to figusPaquete).map(_ => scala.util.Random.nextInt(figusTotal) + 1).toVector

    // Bucle principal hasta que se completen todas las figuritas
    while (album.exists(_ == 0)) {
      val paquete: Vector[Int] = comprarPaquete(figusTotal, figusPaquete)
      album = album.zipWithIndex.map {
        case (valor, index) => if (paquete.contains(index + 1)) 1 else valor
      }
      val figusPegadas: Int = album.count(_ == 1)
      historiaFigusPegadas :+= figusPegadas
    }

    historiaFigusPegadas
  }

  // Calcular la historia de figuritas pegadas
  val historiaFigusPegadas: Vector[Int] = calcularHistoriaFigusPegadas(figusTotal, figusPaquete)

  // Llamar a la función plot desde el Plotter
  plot(0 until historiaFigusPegadas.length, historiaFigusPegadas)
}
