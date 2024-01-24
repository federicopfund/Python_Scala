import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import varios.TextManipulator

class TextManipulatorSpec extends AnyFlatSpec with Matchers {

  "A TextManipulator" should "neutralize words correctly" in {
    val textManipulator = new TextManipulator("Some example text with words to neutralize.")
    val manipulatedText = textManipulator.manipulateText
    manipulatedText shouldEqual "Some example text with words te neutralize."
  }

  it should "neutralize words with '0e' correctly" in {
    val textManipulator = new TextManipulator("Some words with 0e at the end to neutralize.")
    val manipulatedText = textManipulator.manipulateText
    manipulatedText shouldEqual "Some words with e at the end te neutralize."
  }
 
}
