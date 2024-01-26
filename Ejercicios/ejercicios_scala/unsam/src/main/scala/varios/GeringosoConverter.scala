
package varios

/**
  * Clase GeringosoConverter para convertir palabras al estilo "geringoso".
  * El "geringoso" es una jerga argentina donde se agrega 'p' entre cada vocal en una palabra.
  *
  * @constructor Crea una instancia de GeringosoConverter con un diccionario vacío.
  */
class GeringosoConverter {
  // Diccionario interno para almacenar las palabras originales y sus versiones convertidas.
  private var dic: Map[String, String] = Map()

  /**
    * Convierte una palabra al estilo "geringoso".
    *
    * @param word Palabra a convertir.
    * @return Palabra convertida al estilo "geringoso".
    */
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

  /**
    * Procesa una lista de palabras y construye un diccionario con las versiones convertidas al "geringoso".
    *
    * @param wordList Lista de palabras a procesar.
    * @return Diccionario con palabras originales y sus versiones convertidas.
    */
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
