package plotter

import breeze.plot._
import java.io.File
import java.nio.file.{Files, Paths}


object figus {
    /**
    * Función para crear un gráfico de línea.
    *
    * @param x Secuencia de valores para el eje x.
    * @param y Secuencia de valores para el eje y.
    * @param fileName Nombre del archivo de como se guardara.
    */
  def plot(x: Seq[Int], y: Seq[Int]): Unit = {
    // Crear una nueva figura y subparcela para el gráfico
    val fig = Figure()
    val plt = fig.subplot(0)
    
    // Agregar el gráfico de línea usando la biblioteca Breeze
    plt += breeze.plot.plot(x, y)
    
    // Personalizar etiquetas y título del gráfico
    plt.xlabel = "Cantidad de paquetes comprados"
    plt.ylabel = "Cantidad de figuritas pegadas"
    plt.title = "La curva de llenado se desacelera al final"
    
    // Refrescar la figura para mostrar el gráfico
    fig.refresh()
    // Verificar si la carpeta 'plotter' existe en el directorio 'resources' y, si no, crearla
   // Obtener la ruta del directorio 'resources' en el directorio de trabajo del proyecto
    val resourcesPath = Paths.get("src", "resources")
    
    // Crear la ruta completa del directorio 'plotter' dentro de 'resources'
    val plotterFolderPath = resourcesPath.resolve("plotter")

    // Verificar si el directorio 'plotter' existe y, si no, crearlo
    if (!Files.exists(plotterFolderPath)) {
      Files.createDirectories(plotterFolderPath)
    }
    val fileName : String = "AlbumFigusPlot"
    // Guardar el gráfico en la carpeta 'plotter' dentro de 'resources'
    fig.saveas(s"src/resources/plotter/$fileName.png")
  }
}
