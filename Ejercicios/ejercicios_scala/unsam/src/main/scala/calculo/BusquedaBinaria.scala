package calculo


object BusquedaBinaria {

  /**
    * Realiza una búsqueda binaria en una lista ordenada.
    *
    * @param lista   Lista de enteros en la que se realiza la búsqueda.
    * @param x       Elemento que se busca en la lista.
    * @param verbose Indica si se debe imprimir información de depuración.
    * @return        La posición del elemento en la lista o -1 si no se encuentra.
    */
  def busquedaBinaria(lista: List[Int], x: Int, verbose: Boolean = false): Int = {

    /**
      * Función interna que realiza la búsqueda binaria de manera recursiva.
      *
      * @param izq Límite izquierdo de la sublista actual.
      * @param der Límite derecho de la sublista actual.
      * @return    La posición del elemento en la sublista o -1 si no se encuentra.
      */
    def busquedaBinariaRec(izq: Int, der: Int): Int = {
      if (izq > der) {
        -1 // Elemento no encontrado
      } else {
        val medio = izq + (der - izq) / 2

        if (verbose) {
          println(s"[DEBUG] Izquierda: ${lista(izq)}, Derecha: ${lista(der)}, Medio: ${lista(medio)}, Elemento buscado: $x")
        }

        if (lista(medio) == x) {
          medio // Elemento encontrado
        } else if (lista(medio) > x) {
          busquedaBinariaRec(izq, medio - 1) // Buscar en la mitad izquierda
        } else {
          busquedaBinariaRec(medio + 1, der) // Buscar en la mitad derecha
        }
      }
    }

    // Ordenar la lista (si aún no está ordenada)
    val listaOrdenada = lista.sorted

    // Iniciar la búsqueda binaria con la lista ordenada
    busquedaBinariaRec(0, listaOrdenada.length - 1)
  }

  // Ejemplo de uso
  def run(args: List[Int],elemento: Int,verbose: Boolean): Unit = {
    val lista = List(5, 8, 12, 15, 22, 30, 34, 40)
    val elementoBuscado = 22
    val resultado = busquedaBinaria(lista, elementoBuscado, verbose = true)

    if (resultado != -1) {
      println(s"El elemento $elementoBuscado se encuentra en la posición $resultado.")
    } else {
      println(s"El elemento $elementoBuscado no se encuentra en la lista.")
    }
  }
}

