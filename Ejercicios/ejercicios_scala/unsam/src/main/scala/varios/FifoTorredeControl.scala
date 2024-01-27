package varios



class Cola[T](val items: List[T] = Nil) {
  /**
    * Encola un elemento al final de la cola.
    *
    * @param x El elemento a encolar.
    * @return Una nueva instancia de Cola con el elemento encolado.
    */
  def encolar(x: T): Cola[T] = new Cola(items :+ x)

  /**
    * Desencola el primer elemento de la cola.
    *
    * @return Una tupla con el primer elemento desencolado y una nueva instancia de Cola sin ese elemento.
    * @throws NoSuchElementException si la cola está vacía.
    */
  def desencolar: (T, Cola[T]) = items match {
    case Nil => throw new NoSuchElementException("La cola está vacía")
    case head :: tail => (head, new Cola(tail))
  }

  /**
    * Verifica si la cola está vacía.
    *
    * @return true si la cola está vacía, false si contiene elementos.
    */
  def estaVacia: Boolean = items.isEmpty
}

// Definición de la clase TorreDeControl para simular el funcionamiento de una torre de control de vuelos
class TorreDeControl(private val arribos: Cola[String] = new Cola(), private val partidas: Cola[String] = new Cola()) {
  /**
    * Agrega un nuevo arribo al sistema.
    *
    * @param arribo El nombre del vuelo que está llegando.
    * @return Una nueva instancia de TorreDeControl que refleja el estado después de agregar el arribo.
    */
  def nuevoArribo(arribo: String): TorreDeControl = new TorreDeControl(arribos.encolar(arribo), partidas)

  /**
    * Agrega una nueva partida al sistema.
    *
    * @param partida El nombre del vuelo que está despegando.
    * @return Una nueva instancia de TorreDeControl que refleja el estado después de agregar la partida.
    */
  def nuevaPartida(partida: String): TorreDeControl = new TorreDeControl(arribos, partidas.encolar(partida))

  /**
    * Muestra el estado actual de la torre de control.
    */
  def verEstado(): Unit = {
    println(s"Vuelos esperando para aterrizar: ${if (arribos.estaVacia) "No hay vuelos" else arribos.items.mkString(", ")}")
    println(s"Vuelos esperando para despegar: ${if (partidas.estaVacia) "No hay vuelos" else partidas.items.mkString(", ")}")
  }

  /**
    * Asigna una pista a un vuelo en base al estado actual de la Torre de Control.
    *
    * @return Una nueva instancia de TorreDeControl que refleja el estado después de asignar la pista.
    */
  def asignarPista(): TorreDeControl = {
    (arribos.desencolar, partidas.desencolar) match {
      case ((arribo, arribosRestantes), _) =>
        println(s"El vuelo $arribo aterrizó con éxito")
        new TorreDeControl(arribosRestantes, partidas)

      case (_, (partida, partidasRestantes)) =>
        println(s"El vuelo $partida despegó con éxito")
        new TorreDeControl(arribos, partidasRestantes)

      case _ =>
        println("No hay vuelos en espera")
        this
    }
  }
}
