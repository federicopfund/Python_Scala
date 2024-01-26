package Operation

import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{StructType, StructField, StringType, DoubleType,IntegerType}
import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.functions.col
import scala.io.Source
import java.nio.file.{Path, Paths}


object CsvParserRecursive {

  def run(pathFile: Path, select: Option[List[String]], types: Option[List[String => Any]]): Unit = {
    // Crear una sesión de Spark
    val spark = SparkSession.builder.appName("CsvParserApp").master("local").getOrCreate()

    try {
      val source = Source.fromFile(pathFile.toFile)
      val lines = source.getLines()

      // Parsear el CSV y construir el DataFrame
      val data = parseCsvRec(lines, select, types, hasHeaders = true, silenceErrors = false)
      println("Data que viene de parcer:")
      println(data)
      println("Obtenemos los encabezados")
      val headers = select.getOrElse(List()) // Obtener los encabezados seleccionados
      println(headers)
      // Crear el esquema
      val schema = StructType(
        headers.map {
          case "id_arbol"    => StructField("id_arbol", IntegerType, nullable = true)
          case "altura_tot"  => StructField("altura_tot", IntegerType, nullable = true)
          case "diametro"    => StructField("diametro", DoubleType, nullable = true)
          case _             => throw new IllegalArgumentException("Encabezado no reconocido")
        })
      
      val rows = data.map(row => headers.zip(row).map { case (header, value) => (header, value.toString) })
      println("Printea la data de las rows:")
      println(rows)
      // Verificar que el número de columnas coincida
      val numColumns = headers.length
      val isValidData: Boolean = rows.forall(row => row.length == numColumns)

      if (isValidData) {
       // Crear el DataFrame utilizando el esquema y las filas

           // Definir el esquema
        

        // Crear el rdd
        val rdd = spark.sparkContext.parallelize(data.toSeq)
        
          // Printear el contenido del RDD
        rdd.collect().foreach(println)

        //val df = spark.createDataFrame(rdd, schema)

        // Mostrar el DataFrame
        //df.show()
      } else {
        println(s"Error: El número de columnas en los datos no coincide con el número de encabezados.")
      }
    } finally {
      // Detener la sesión de Spark al finalizar
      spark.stop()
    }
  }


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
    // Imprimir encabezados seleccionados
    select.foreach(selectedColumns => println(s"Selected headers: ${selectedColumns.mkString(", ")}"))
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
    }

    // Lógica para procesar las líneas
    val parsedData = remainingLines.flatMap { line =>
      val row = line.split(",").toList
      if (row.nonEmpty) Some(parseRow(row, headers))
      else {
        println("Skipping empty row.")
        None
      }
    }.toList

    // Devolver solo las primeras 15 filas
    parsedData.take(15)
  }
}
