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

    // Probabilidad Exacta
    val probabilidadExactaGenerala = Exacta(1.0 / 6.0, 5)

    // Imprimir el resultado
    println(s"Probabilidad exacta de obtener una Generala: $probabilidadExactaGenerala")

    // Calcular y estimar la probabilidad de obtener un Poker
    val resultadosPoker = Seq.fill(N)(esPoker(tirar()))
    val cantidadPoker = resultadosPoker.count(identity)
    val probabilidadPoker = cantidadPoker.toDouble / N.toDouble

    println(s"Tiré $N veces, de las cuales $cantidadPoker fueron poker.")
    println(f"Podemos estimar la probabilidad de sacar poker es: $probabilidadPoker%.6f.")

    // Calcular y estimar la probabilidad de obtener un Full
    val resultadosFull = Seq.fill(N)(esFull(tirar()))
    val cantidadFull = resultadosFull.count(identity)
    val probabilidadFull = cantidadFull.toDouble / N.toDouble

    println(s"Tiré $N veces, de las cuales $cantidadFull fueron Full.")
    println(f"Podemos estimar la probabilidad de sacar Full es: $probabilidadFull%.6f.")

    // Calcular y estimar la probabilidad de obtener una Escalera
    val resultadosEscalera = Seq.fill(N)(esEscalera(tirar()))
    val cantidadEscalera = resultadosEscalera.count(identity)
    val probabilidadEscalera = cantidadEscalera.toDouble / N.toDouble

    println(s"Tiré $N veces, de las cuales $cantidadEscalera fueron escalera.")
    println(f"Podemos estimar la probabilidad de sacar escalera es: $probabilidadEscalera%.6f.")

    // Simular una jugada de Generala No Servida
    val dados = Seq.fill(5)(Random.nextInt(6) + 1)
    val resultadoGeneralNoServida = generalaNoServida(dados)
    println(s"Resultado de la jugada: ${resultadoGeneralNoServida.mkString(", ")}")
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
    * Función que verifica si una secuencia de dados representa un Poker.
    *
    * @param dados Secuencia de valores de dados.
    * @return true si es un Poker, false en caso contrario.
    */
  def esPoker(dados: Seq[Int]): Boolean = {
    dados.groupBy(identity).values.exists(_.length == 4)
  }

  /**
    * Función que verifica si una secuencia de dados representa una Escalera.
    *
    * @param dados Secuencia de valores de dados.
    * @return true si es una Escalera, false en caso contrario.
    */
  def esEscalera(dados: Seq[Int]): Boolean = {
    val esc1 = Seq(1, 2, 3, 4, 5)
    val esc2 = Seq(2, 3, 4, 5, 6)
    dados.sorted == esc1 || dados.sorted == esc2
  }

  /**
    * Función que verifica si una secuencia de dados representa un Full.
    *
    * @param dados Secuencia de valores de dados.
    * @return true si es un Full, false en caso contrario.
    */
  def esFull(dados: Seq[Int]): Boolean = {
    val hayTres = dados.exists(valor => dados.count(_ == valor) == 3)
    val hayDos = dados.exists(valor => dados.count(_ == valor) == 2)
    hayTres && hayDos
  }

  /**
    * Función que simula una jugada de Generala No Servida.
    *
    * @param dados Secuencia de valores de dados.
    * @return Resultado de la jugada.
    */
  def generalaNoServida(dados: Seq[Int]): Seq[Int] = {
    val repeticion = dados.groupBy(identity).mapValues(_.size)
    val masRepet = repeticion.maxBy(_._2)
    var dadoMasRepetido = masRepet._1
    var maxRepeticion = masRepet._2

    for (_ <- 0 until 2) {
      if (dadoMasRepetido < 5) {
        val nuevosDados = Seq.fill(5 - dadoMasRepetido)(Random.nextInt(6) + 1)
        val repeticion1 = nuevosDados.groupBy(identity).mapValues(_.size)
        val masRepet1 = repeticion1.maxBy(_._2)
        val dadoMasRepetido1 = masRepet1._1
        val maxRepeticion1 = masRepet1._2

        if (dadoMasRepetido1 > dadoMasRepetido) {
          dadoMasRepetido = dadoMasRepetido1
          maxRepeticion = maxRepeticion1
        } else {
          dadoMasRepetido += nuevosDados.count(_ == maxRepeticion)
        }
      } else {
        return Seq.fill(dadoMasRepetido)(maxRepeticion)
      }
    }

    val ultima = Seq.fill(dadoMasRepetido)(maxRepeticion) ++ dados.filter(_ != maxRepeticion)
    ultima
  }

  /**
    * Función que calcula la probabilidad exacta de un evento.
    *
    * @param probabilidadDelEvento Probabilidad individual del evento.
    * @param numerosDeEventos Número de eventos.
    * @return Probabilidad exacta del evento.
    */
  def Exacta(probabilidadDelEvento: Double, numerosDeEventos: Int): Double = {
    math.pow(probabilidadDelEvento, numerosDeEventos - 1)
  }

  /**
    * Función principal que llama a runParallel con un valor específico de N.
    *
    * @param N Número de tiradas de dados a realizar.
    */
  def run(N: Int): Unit = runParallel(N)
}
