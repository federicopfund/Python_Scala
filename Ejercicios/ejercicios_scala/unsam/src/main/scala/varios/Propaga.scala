package varios

object Propagacion {

  /**
    * Función para propagar ceros en una lista.
    *
    * @param lista Lista de enteros donde se realizará la propagación.
    * @return Lista resultante después de la propagación.
    */
  def propagar(lista: List[Int]): List[Int] = {
    // Propagación hacia la izquierda
    val result = lista.zipWithIndex.map {
      case (value, index) =>
        // Verifica si el elemento anterior es 1 y el valor actual es 0, luego establece el valor actual como 1
        if (index - 1 >= 0 && value == 0 && lista(index - 1) == 1) 1
        else value
    }

    // Propagación hacia la derecha
    val resultReverse = result.zipWithIndex.reverse.map {
      case (value, index) =>
        // Verifica si el elemento siguiente es 1 y el valor actual es 0, luego establece el valor actual como 1
        if (index + 1 < lista.length && value == 0 && lista(index + 1) == 1) 1
        else value
    }

    resultReverse
  }

  /**
    * Método principal para probar la función propagar.
    *
    * @param args Argumentos de la línea de comandos (no utilizado en este caso).
    */
  def run(args: List[Int]): Unit = {
    // Ejemplo de propagación en una lista con valores específicos
    val propagars1 = propagar(args)
    println(propagars1)

    // Otro ejemplo de propagación en una lista diferente
    val propagars2 = propagar(args)
    println(propagars2)
  }
}
