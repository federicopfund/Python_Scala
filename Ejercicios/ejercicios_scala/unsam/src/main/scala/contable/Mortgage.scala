package contable


class Mortgage(
  var principal: Double,
  val annualInterestRate: Double,
  val monthlyPayment: Double,
  val loanTermYears: Int,
  val adelanta: Double = 0.0,
  val adelantaStartMonth: Int = 0,
  val adelantaEndMonth: Int = 0
) {
  private var totalPaid: Double = 0.0
  private var currentMonth: Int = 0

  private def calculateMonthlyInterest(annualInterestRate: Double): Double = {
    annualInterestRate / 12
  }

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

   // Método público para obtener el valor de totalPaid
  def getTotalPaid: Double = totalPaid
  
  def calculateTotalPaid(): Unit = {
    val payments = monthlyPaymentsGenerator()
    payments.foreach { payment =>
      principal *= (1 + calculateMonthlyInterest(annualInterestRate)) - payment
      totalPaid += payment
    }
  }

  def displayResult(): Unit = {
    println(s"Total paid: ${totalPaid.round}")
  }
}
