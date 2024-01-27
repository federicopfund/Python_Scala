package varios

import scala.util.Random
/**
  * Objeto Scala llamado Generala que simula el juego de dados Generala.
  */
object Generala {

  /**
    * Simula una tirada de dados.
    *
    * @return Una secuencia de 5 números aleatorios entre 1 y 6.
    */
  def tirar(): Seq[Int] = {
    // Utilizar Seq.fill para generar una secuencia con 5 elementos, donde cada elemento es un número aleatorio entre 1 y 6
    Seq.fill(5)(util.Random.nextInt(6) + 1)
  }

  /**
    * Verifica si una tirada es Generala.
    *
    * @param tirada La secuencia de números resultantes de la tirada de dados.
    * @return true si todos los elementos de la tirada son iguales, false en caso contrario.
    */
  def esGenerala(tirada: Seq[Int]): Boolean = {
    // Verificar si el máximo y el mínimo de la tirada son iguales, lo que indica que todos los dados tienen el mismo valor
    tirada.max == tirada.min
  }
}
