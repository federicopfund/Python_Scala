package plotter

import breeze.stats.hist._
import breeze.plot._
import java.io.File
import java.nio.file.{Files, Paths}
import breeze.linalg._
import simulacion.ProbabilidadGenerala._
import breeze.linalg.DenseVector

object plt {
    /**
    * Función para crear un gráfico de línea.
    *
    * @param x Secuencia de valores para el eje x.
    * @param y Secuencia de valores para el eje y.
    * @param fileName Nombre del archivo de como se guardara.
    */
  def plotfigus(x: Seq[Int], y: Seq[Int]): Unit = {
    // Crear una nueva figura y subparcela para el gráfico
    val fig = Figure()
    val plts = fig.subplot(0)
    
    // Agregar el gráfico de línea usando la biblioteca Breeze
    plts += breeze.plot.plot(x, y)
    
    // Personalizar etiquetas y título del gráfico
    plts.xlabel = "Cantidad de paquetes comprados"
    plts.ylabel = "Cantidad de figuritas pegadas"
    plts.title = "La curva de llenado se desacelera al final"
    
    // Refrescar la figura para mostrar el gráfico
    fig.refresh()
    Save.Plotter(fig,"DiagramaHistogram")
  }

  def plottermo(temperaturas: breeze.linalg.DenseVector[Double]): Unit = {
    val N = temperaturas.length
    val minimo = temperaturas.min
    val maximo = temperaturas.max
    val promedio = breeze.stats.mean(temperaturas)
    val mediana = breeze.stats.median(temperaturas)
    println(f"| Minimo: $minimo%.2f | Maximo: $maximo%.2f | Promedio: $promedio%.2f | Mediana: $mediana%.2f |")

    // Crear un gráfico
    val fig = Figure()
    val plt = fig.subplot(0)

    // Agregar los datos al gráfico con índices
    plt += plot(DenseVector((1 to N).map(_.toDouble): _*), temperaturas)

    // Configurar el título y etiquetas de los ejes
    plt.title = "Gráfico de Temperaturas"
    plt.xlabel = "Índice"
    plt.ylabel = "Temperatura"

    // Mostrar el gráfico
    fig.refresh()
    Save.Plotter(fig,"DiagramaTemp")
  }
}


object Save {
  // Función para guardar un gráfico en el directorio 'plotter' dentro de 'resources'
  def Plotter(fig: Figure, fileName: String): Unit = {
    // Obtener la ruta del directorio 'resources' en el directorio de trabajo del proyecto
    val resourcesPath = Paths.get("src", "resources")

    // Crear la ruta completa del directorio 'plotter' dentro de 'resources'
    val plotterFolderPath = resourcesPath.resolve("plotter")

    // Verificar si el directorio 'plotter' existe y, si no, crearlo
    if (!Files.exists(plotterFolderPath)) {
      Files.createDirectories(plotterFolderPath)
    }

    // Guardar el gráfico en la carpeta 'plotter' dentro de 'resources'
    fig.saveas(s"src/resources/plotter/$fileName.png")
  }
}
