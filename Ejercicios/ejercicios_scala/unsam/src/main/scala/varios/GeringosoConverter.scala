
package varios


class GeringosoConverter {
  private var dic: Map[String, String] = Map()

  def convertToGeringoso(word: String): String = {
    var convertedWord = ""
    for (char <- word) {
      if ("aeiou".contains(char)) {
        convertedWord = convertedWord + s"${char}p${char}"
      } else {
        convertedWord = convertedWord + char
      }
    }
    convertedWord.trim
  }

  def processList(wordList: List[String]): Map[String, String] = {
    try {
      for (gerin <- wordList) {
        val convertedWord = convertToGeringoso(gerin)
        dic += (gerin -> convertedWord)
      }
      dic
    } catch {
      case _: Throwable =>
        println("No admite alfanumericos")
        dic
    }
  }
}