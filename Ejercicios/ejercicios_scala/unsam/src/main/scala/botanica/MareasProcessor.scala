package botanica
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types._
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions.date_format

/**
  * Objeto MareasProcessor realiza operaciones y transformaciones en datos de Mareas de Bs As.
  *
  * @version 1.0
  * @author Federico Cristian Pfund
  */
object MareasProcessor {

    /**
  * Lee un archivo CSV y devuelve un DataFrame de Spark.
  *
  * @param spark      Sesión de Spark.
  * @param filePath   Ruta del archivo CSV.
  * @param customSchema Esquema personalizado para el DataFrame.
  * @return           DataFrame de Spark.
  */
  def leerArchivoCSV(spark: SparkSession, filePath: String, customSchema: StructType): DataFrame = {
      spark.read.option("header", "true").schema(customSchema).csv(filePath).cache()
  }

    /**
    * Realiza el desplazamiento de series y guarda el resultado en formato Parquet.
    *
    * @param dfMareas    DataFrame de Spark que contiene datos de mareas.
    * @param directorio  Directorio de salida para los resultados.
    */
  def shiftSerie(dfMareas: DataFrame, directorio: String): Unit = {
    // Desplazamiento de Series
    val windowSpec = Window.orderBy("Fecha")  // Definición de la ventana

    val dh = dfMareas.filter(col("Fecha") >= "2014-12-25")

    val deltaT = 2 // tiempo que tarda la marea entre ambos puertos
    val deltaH = 3 // diferencia de los ceros de escala entre ambos puertos

    dh.select(
        (col("H_SF") - deltaH - lag("H_SF", deltaT).over(windowSpec)).alias("Shifted_H_SF"),
        col("H_BA")
      )
      .coalesce(1)
      .write
      .format("parquet")
      .mode("overwrite")
      .option("header", "true")
      .save(s"$directorio/parquet/shifted_series_plot")
  }

   /**
    * Realiza la lectura de datos de prueba y guarda plots en formato Parquet.
    *
    * @param dfMareas    DataFrame de Spark que contiene datos de mareas.
    * @param directorio  Directorio de salida para los resultados.
    */
  def lecturaPlots(dfMareas: DataFrame, directorio: String): Unit = {
    // Realiza lectura de prueba para imprimir y hace 2 plots en distintos intervalos de tiempo
    dfMareas.filter(col("Fecha").between("2014-01-18 09:00", "2014-01-18 18:00")).show()
    // Ondas de marea en el RdlP
    dfMareas.filter(col("Fecha").between("2014-10-15", "2014-12-15")).coalesce(1)
      .write
      .format("parquet")  // Cambiado a formato parquet
      .mode("overwrite")
      .option("header", "true")
      .save(s"$directorio/parquet/waves_plot")
    // Vientos y ondas de tormenta en el RdlP
    dfMareas.filter(col("Fecha") >= "2014-12-25").coalesce(1)
      .write
      .format("parquet")  // Cambiado a formato parquet
      .mode("overwrite")
      .option("header", "true")
      .save(s"$directorio/parquet/storm_waves_plot")
  }

  /**
    * Llena celdas nulas en una columna específica con el valor proporcionado.
    *
    * @param df               DataFrame de Spark.
    * @param columnasAEvaluar Columnas en las que llenar las celdas nulas.
    * @param valor = 0         Valor con el que llenar las celdas nulas.
    * @return                 DataFrame de Spark con celdas nulas llenas.
    */
  def llenarNulasConCero(df: DataFrame, columnasAEvaluar: Seq[String]): DataFrame = {
    df.na.fill(0, columnasAEvaluar)
  }

  /**
    * Divide la columna "Time" en "Fecha" y "Time".
    *
    * @param df DataFrame de Spark.
    * @return   DataFrame de Spark con columnas "Fecha" y "Time" separadas.
    */
  def dividirColumnaTime(df: DataFrame): DataFrame = {
    df.withColumn("Fecha", split(col("Time"), " ").getItem(0))
      .withColumn("Time", split(col("Time"), " ").getItem(1))
  }

   /**
    * Convierte el campo "Fecha" a tipo Timestamp.
    *
    * @param df DataFrame de Spark.
    * @return   DataFrame de Spark con el campo "Fecha" convertido a tipo Timestamp.
    */
  def convertirCampoFechaATimestamp(df: DataFrame): DataFrame = {
    df.withColumn("Fecha", to_timestamp(col("Fecha"), "yyyy-MM-dd"))
      .withColumn("Fecha", date_format(col("Fecha"), "yyyy-MM-dd HH:mm:ss"))
    }
  /**
    * Función principal que ejecuta todo el proceso.
    *
    * @param args Argumentos de línea de comandos.
    */
  def run(args: Array[String]): Unit = {
    // Definición del esquema para el DataFrame
    val customSchema = StructType(Seq(
      StructField("Time", TimestampType, nullable = true),
      StructField("H_SF", DoubleType, nullable = true),
      StructField("H_BA", DoubleType, nullable = true)
    ))
    // Creación de la sesión de Spark
    val spark = SparkSession.builder.appName("MareasSpark")
        .config("spark.master", "local")
        .config("spark.sql.legacy.timeParserPolicy", "LEGACY")
        .config("spark.sql.warehouse.dir", "./src/resources/dataset/")
        .config("spark.sql.shuffle.partitions", "8")          // Número de particiones para operaciones shuffle
        .config("spark.default.parallelism", "8")             // Paralelismo por defecto para operaciones como join y aggregations
        .config("spark.executor.memory", "2g")                // Tamaño de la memoria asignada a los ejecutores
        .config("spark.executor.cores", "2")                  // Número de núcleos asignados por executor
        .config("spark.executor.instances", "2")              // Número de instancias de executor
        .config("spark.driver.memory", "2g")                  // Tamaño de la memoria asignada al driver
        .config("spark.sql.autoBroadcastJoinThreshold", "104857600")  // Umbral para la transmisión automática de tablas pequeñas
        .getOrCreate()

    try {
      // Creación de la sesión de Spark
      val archivo = if (args.length == 1) args(0) else "OBS_SHN_SF-BA.csv"
      val directorio = "./src/resources/dataset/"
      val fname = s"$directorio$archivo"
      // Extract 
      val dfs : DataFrame  = leerArchivoCSV(spark, fname, customSchema)

      val columnasAEvaluar = Seq("H_SF")
      // Transform
      val dfConCero: DataFrame = llenarNulasConCero(dfs, columnasAEvaluar)
      val dfResultado: DataFrame = dividirColumnaTime(dfConCero)
      dfResultado.show()

      val dfDepurado: DataFrame = convertirCampoFechaATimestamp(dfResultado)
      dfDepurado.show()
      // Load
      lecturaPlots(dfResultado, directorio)
      shiftSerie(dfDepurado, directorio)
    } catch {
      case e: Throwable => println(s"Error en la ejecución: ${e.getMessage}")
    } finally {
      spark.stop()
    }
  }
}
