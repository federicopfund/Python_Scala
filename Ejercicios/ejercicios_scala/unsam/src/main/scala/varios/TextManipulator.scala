package varios

/**
  * Clase TextManipulator para manipular y neutralizar palabras en un texto.
  *
  * @param text Texto a manipular.
  */
class TextManipulator(text: String) {
  // Se divide el texto en palabras y se almacenan en un array.
  val words: Array[String] = text.split("\\s+")

  /**
    * Neutraliza una palabra según ciertas reglas predefinidas.
    *
    * @param word Palabra a neutralizar.
    * @return Palabra neutralizada.
    */
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

  /**
    * Genera un flujo (Stream) de palabras neutralizadas a partir de las palabras originales.
    *
    * @return Flujo de palabras neutralizadas.
    */
  def neutralizeAllWordsGenerator: Stream[String] = {
    words.toStream.map(neutralizeWord)
  }

  /**
    * Manipula el texto completo neutralizando todas las palabras según las reglas definidas.
    *
    * @return Texto manipulado.
    */
  def manipulateText: String = {
    val neutralizedWords = neutralizeAllWordsGenerator
    neutralizedWords.mkString(" ")
  }
}
