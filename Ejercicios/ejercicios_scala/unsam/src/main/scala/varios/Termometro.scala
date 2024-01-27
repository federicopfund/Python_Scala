package varios

import breeze.linalg.DenseVector
import scala.util.Random

/**
 * Objeto que genera temperaturas aleatorias y proporciona estadísticas relacionadas.
 */
object TermometroGenerador {
  // Semilla del generador de números aleatorios
  val semilla = 42
  Random.setSeed(semilla)

  /**
   * Genera un vector de temperaturas aleatorias con distribución gaussiana.
   *
   * @param N     Número de temperaturas a generar.
   * @param media Valor medio de la distribución gaussiana.
   * @return      Vector de temperaturas generadas.
   */
  def generarTemperaturas(N: Int, media: Double): DenseVector[Double] = {
    val bigData = (1 to N).map(_ => Random.nextGaussian() * 0.2 + media).toArray
    DenseVector(bigData)
  }
}
