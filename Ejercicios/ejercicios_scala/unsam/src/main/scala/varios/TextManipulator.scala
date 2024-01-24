package varios

class TextManipulator(text: String) {
  val words: Array[String] = text.split("\\s+")

  def neutralizeWord(word: String): String = {
    if (word.length >= 2 && word.takeRight(2) == "te")
      word.dropRight(2) + "to"
    else if (word.length >= 2 && word.takeRight(2) == "0e")
      word.dropRight(2) + "e"
    else if (word.last == 'o')
      word.dropRight(1) + "e"
    else
      word
  }

  def neutralizeAllWordsGenerator: Stream[String] = {
    words.toStream.map(neutralizeWord)
  }

  def manipulateText: String = {
    val neutralizedWords = neutralizeAllWordsGenerator
    neutralizedWords.mkString(" ")
  }
}