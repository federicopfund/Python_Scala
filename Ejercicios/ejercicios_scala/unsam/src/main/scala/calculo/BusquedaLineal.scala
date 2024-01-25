package calculo

object BusquedaLinealOrdenada {

  /**
    * Realiza una búsqueda lineal en una lista ordenada.
    *
    * @param lista Lista de enteros en la que se realiza la búsqueda.
    * @param e     Elemento que se busca en la lista.
    * @return      La posición del elemento en la lista o -1 si no se encuentra.
    */

  def busquedaLinealOrdenada(lista: List[Int], e: Int): Int = {
    // Asegúrate de que la lista esté ordenada antes de realizar la búsqueda
    val listaOrdenada = lista.sorted

    /**
      * Función interna que realiza la búsqueda lineal de manera recursiva.
      *
      * @param listaOrdenada Lista ordenada en la que se realiza la búsqueda.
      * @param e             Elemento que se busca en la lista.
      * @param indice        Índice actual en la lista.
      * @return              La posición del elemento en la lista o -1 si no se encuentra.
      */
    def busquedaRecursiva(listaOrdenada: List[Int], e: Int, indice: Int): Int = {
      if (indice >= listaOrdenada.length) {
        -1 // Elemento no encontrado
      } else if (listaOrdenada(indice) == e) {
        indice // Elemento encontrado
      } else if (listaOrdenada(indice) > e) {
        -1 // Elemento no encontrado y la lista está ordenada, así que no es necesario buscar más
      } else {
        busquedaRecursiva(listaOrdenada, e, indice + 1) // Buscar en la siguiente posición
      }
    }

    // Iniciar la búsqueda recursiva con la lista ordenada
    busquedaRecursiva(listaOrdenada, e, 0)
  }

  // Ejemplo de uso
  def run(args: Array[String]): Unit = {
    val lista = List(5, 8, 12, 15, 22, 30, 34, 40)
    val elementoBuscado = 22
    val resultado = busquedaLinealOrdenada(lista, elementoBuscado)

    if (resultado != -1) {
      println(s"El elemento $elementoBuscado se encuentra en la posición $resultado.")
    } else {
      println(s"El elemento $elementoBuscado no se encuentra en la lista.")
    }
  }
}
