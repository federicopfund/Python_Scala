package contable

/**
  * Clase Mortgage para calcular y mostrar resultados relacionados con una hipoteca.
  *
  * @param principal           Monto principal del préstamo hipotecario.
  * @param annualInterestRate  Tasa de interés anual del préstamo hipotecario.
  * @param monthlyPayment      Pago mensual del préstamo hipotecario.
  * @param loanTermYears       Plazo del préstamo en años.
  * @param adelanta            Pago adicional adelantado opcional.
  * @param adelantaStartMonth  Mes de inicio para los pagos adicionales adelantados.
  * @param adelantaEndMonth    Mes de finalización para los pagos adicionales adelantados.
  */
class Mortgage(
  var principal: Double,
  val annualInterestRate: Double,
  val monthlyPayment: Double,
  val loanTermYears: Int,
  val adelanta: Double = 0.0,
  val adelantaStartMonth: Int = 0,
  val adelantaEndMonth: Int = 0
) {
  // Variables privadas para realizar cálculos internos
  private var totalPaid: Double = 0.0
  private var currentMonth: Int = 0

  /**
    * Calcula la tasa de interés mensual a partir de la tasa de interés anual.
    *
    * @param annualInterestRate Tasa de interés anual.
    * @return Tasa de interés mensual.
    */
  private def calculateMonthlyInterest(annualInterestRate: Double): Double = {
    annualInterestRate / 12
  }

  /**
    * Genera un flujo (Stream) de pagos mensuales, considerando pagos adelantados si aplican.
    *
    * @return Flujo de pagos mensuales durante el plazo del préstamo.
    */
  private def monthlyPaymentsGenerator(): Stream[Double] = {
    val monthlyInterest = calculateMonthlyInterest(annualInterestRate)
    Stream.continually {
      currentMonth += 1
      if (adelantaStartMonth < currentMonth && currentMonth <= adelantaEndMonth) {
        monthlyPayment + adelanta
      } else {
        monthlyPayment
      }
    }.take(loanTermYears * 12)
  }

  /**
    * Método público para obtener el valor de totalPaid.
    *
    * @return Monto total pagado hasta el momento.
    */
  def getTotalPaid: Double = totalPaid

  /**
    * Calcula el monto total pagado al final del plazo del préstamo.
    * Actualiza el monto principal y el total pagado con cada pago mensual.
    */
  def calculateTotalPaid(): Unit = {
    val payments = monthlyPaymentsGenerator()
    payments.foreach { payment =>
      principal *= (1 + calculateMonthlyInterest(annualInterestRate)) - payment
      totalPaid += payment
    }
  }

  /**
    * Muestra el resultado total pagado hasta el momento.
    */
  def displayResult(): Unit = {
    println(s"Total paid: ${totalPaid.round}")
  }
}
