package Operation

import scala.io.Source
import java.nio.file.{Path, Paths}

object CsvParserRecursive {
     /**
    * Parsea un archivo CSV de manera recursiva, con opciones para seleccionar columnas y especificar tipos de datos.
    *
    * @param lines        Iterator de líneas del archivo CSV.
    * @param select       Opción para seleccionar columnas específicas.
    * @param types        Opción para especificar tipos de datos de columnas.
    * @param hasHeaders   Indica si el archivo CSV tiene encabezados.
    * @param silenceErrors Indica si se deben silenciar los errores de conversión de tipos.
    * @return             Lista de listas que representa los datos parseados del CSV.
    */

  def parseCsvRec(lines: Iterator[String], select: Option[List[String]] = None, types: Option[List[String => Any]] = None, hasHeaders: Boolean = true, silenceErrors: Boolean = false): List[List[Any]] = {
     // Leer los encabezados (si los hay)
    val (headers, remainingLines) = if (hasHeaders && lines.hasNext) {
      val header :: restOfLines = lines.toList
      val headerList = header.split(",").toList
      println(s"Headers found: $headerList")
      (headerList, restOfLines.iterator)
    } else (List(), lines)
    /**
      * Función auxiliar para parsear una única fila del CSV.
      *
      * @param row Lista de valores de una fila del CSV.
      * @return    Lista de valores parseados según las opciones especificadas.
      */
    def parseRow(row: List[String], headers: List[String]): List[Any] = {
      val selectedRow = select match {
        case Some(selectedColumns) =>
          selectedColumns.flatMap { col =>
            headers.indexOf(col) match {
              case -1 =>
                println(s"Column '$col' not found in headers.")
                None
              case index => Some(row(index))
            }
          }
        case None => row
      }

      types match {
        case Some(typeConverters) =>
          try {
            typeConverters.zip(selectedRow).map { case (converter, value) => converter(value) }
          } catch {
            case e: NumberFormatException if !silenceErrors =>
              println(s"Error en la conversión de tipos: $e")
              List()
          }
        case None => selectedRow
      }
    
  // Lógica para procesar las líneas
    remainingLines.flatMap { line =>
      val row = line.split(",").toList
      if (row.nonEmpty) Some(parseRow(row, headers))
      else {
        println("Skipping empty row.")
        None
      }
    }.toList
  }


    /**
      * Función recursiva para procesar las líneas del CSV.
      *
      * @param acc Lista acumuladora que almacena las filas parseadas.
      * @return    Lista final de filas parseadas.
      */
  def processLines(acc: List[List[Any]]): List[List[Any]] = {
    if (lines.hasNext) {
      val rowString = lines.next()
      val row = rowString.split(",").toList

      if (row.nonEmpty) {
        val parsedRow = parseRow(row)
        if (parsedRow.nonEmpty) {
          processLines(acc :+ parsedRow)
        } else {
            // Skip invalid rows
          processLines(acc)
        }
      } else {
        // Skip empty rows
        processLines(acc)
      }
    } else {
      acc
    }
  }
    processLines(List())
  }

  // Ejemplo de uso:
  def run(pathFile: Path, select: Option[List[String]] = None, types: Option[List[String => Any]] = None): Unit = {
    val source = Source.fromFile(pathFile.toFile)
    val lines = source.getLines()
    
    val result = parseCsvRec(lines, select, types)

    source.close()

    println(result)
  }
}
