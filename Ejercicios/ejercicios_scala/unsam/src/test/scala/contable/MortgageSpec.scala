import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import contable.Mortgage

class MortgageSpec extends AnyFlatSpec with Matchers {
  "A Mortgage" should "calculate total paid correctly" in {
    val mortgage = new Mortgage(
      principal = 500000.0,
      annualInterestRate = 0.05,
      monthlyPayment = 2684.11,
      loanTermYears = 30,
      adelanta = 1000,
      adelantaStartMonth = 6,
      adelantaEndMonth = 17
    )

    mortgage.calculateTotalPaid()
    val actualResult = BigDecimal(mortgage.getTotalPaid).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    actualResult shouldEqual 977279.6 // Ajusta este valor según el resultado esperado
  
  }
   "A Mortgage" should "calculates total paid without delivery" in {
    // David solicitó un crédito a 30 años para comprar una vivienda, 
    //  con una tasa fija nominal anual del 5%.
    // Pidió $500000 al banco y acordó un pago mensual fijo de $2684"""
    //Example of using the Mortgage class
    
    val mortgage = new Mortgage(
      principal = 500000.0,
      annualInterestRate = 0.05,
      monthlyPayment = 2684.11,
      loanTermYears = 30,
      adelanta = 0,
      adelantaStartMonth = 0,
      adelantaEndMonth = 0
    )

    mortgage.calculateTotalPaid()
    val actualResult = BigDecimal(mortgage.getTotalPaid).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    actualResult shouldEqual 966279.6// Adjust this value based on your expected result
   }

  "A Mortgage" should "calculates the total paid in advance" in {
    //  Adelantos
    //  Supongamos que David adelanta pagos extra de $1000/mes,
    //  durante los primeros 12 meses de la hipoteca.
    
    val mortgage = new Mortgage(
      principal = 500000.0,
      annualInterestRate = 0.05,
      monthlyPayment = 2684.11,
      loanTermYears = 30,
      adelanta = 1000,
      adelantaStartMonth = 12,
      adelantaEndMonth = 0
    )

    mortgage.calculateTotalPaid()
    val actualResult = BigDecimal(mortgage.getTotalPaid).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    actualResult shouldEqual 966279.6// Adjust this value based on your expected result
    }

      "A Mortgage" should "calculates the total paid Bonus" in {
    //  Adelantos
    //  Supongamos que David adelanta pagos extra de $1000/mes,
    //  durante los primeros 12 meses de la hipoteca.
    
    val mortgage = new Mortgage(
      principal = 500000.0,
      annualInterestRate = 0.05,
      monthlyPayment = 2684.11,
      loanTermYears = 30,
      adelanta = 1000,
      adelantaStartMonth = 60,
      adelantaEndMonth = 108
    )

    mortgage.calculateTotalPaid()
    val actualResult = BigDecimal(mortgage.getTotalPaid).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    actualResult shouldEqual 1014279.6// Adjust this value based on your expected result
    }
}