package simulacion

import scala.util.Random

import varios.Generala._
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

object ProbabilidadGenerala {

  /**
    * Función principal que ejecuta el cálculo de probabilidad en paralelo.
    *
    * @param N Número de tiradas de dados a realizar.
    */
  def runParallel(N: Int): Unit = {
    // Crear una secuencia de futuros que realizan tiradas de dados en paralelo
    val futures = (1 to N).map(_ => Future(sumarTirada()))

    // Combinar los resultados de los futuros de manera más eficiente
    val futureSumas = Future.sequence(futures)

    // Calcular la suma de las Generalas en paralelo directamente desde el resultado
    val G = Await.result(futureSumas.map(_.count(esGenerala)), Duration.Inf)

    // Calcular la probabilidad
    val prob = G.toDouble / N.toDouble

    println(s"Probabilidad de obtener Generala en $N tiradas (en paralelo): $prob")
  }

  /**
    * Función que simula una tirada de dados y devuelve la suma de los resultados.
    *
    * @return Suma de los resultados de la tirada de dados.
    */
  def sumarTirada(): Int = tirar().sum

  /**
    * Función que verifica si el resultado de una tirada representa una Generala.
    *
    * @param resultadoTirada Resultado de la tirada de dados.
    * @return true si es una Generala, false en caso contrario.
    */
  def esGenerala(resultadoTirada: Int): Boolean = {
    val tirada = resultadoTirada.toString.map(_.asDigit)
    tirada.max == tirada.min
  }

  /**
    * Función principal que llama a runParallel con un valor específico de N.
    *
    * @param N Número de tiradas de dados a realizar.
    */
  def run(N: Int): Unit = runParallel(N)
}

