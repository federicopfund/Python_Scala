package plotter

import breeze.plot._

/**
  * Objeto `figus` que proporciona funciones para visualizar gráficos relacionados con la colección de figuritas.
  */
object figus {
  
  /**
    * Función para crear un gráfico de línea.
    *
    * @param x Secuencia de valores para el eje x.
    * @param y Secuencia de valores para el eje y.
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
  }
}
