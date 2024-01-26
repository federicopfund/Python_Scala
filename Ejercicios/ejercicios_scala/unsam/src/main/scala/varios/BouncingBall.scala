package varios

// La pelota que rebota
class BouncingBall(initialHeight: Double, bounces: Int) {
  var height: Double = initialHeight
  var currentBounce: Int = 0

  /**
    * Simula el rebote de la pelota.
    * Imprime la altura después de cada rebote.
    */
  def simulateBounces(): Unit = {
    while (currentBounce < bounces) {
      currentBounce += 1
      height *= (3.0 / 5.0)
      println(s"Bounce $currentBounce: Height ${height.round}")
    }
  }
}