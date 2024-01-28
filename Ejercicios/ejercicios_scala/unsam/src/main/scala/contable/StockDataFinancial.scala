package contable


import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.types._
import Consorcio.schemas.StockDataSchema._
import java.nio.file.{Path, Paths}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.expressions.Window
import mlflow.MlStockDataFinancial._


/**
  * Objeto StockDataFinancial para realizar operaciones y transformaciones en datos financieros.
  *
  * Contiene funciones para cargar, transformar y analizar datos financieros utilizando Spark.
  *
  * @version 1.0
  * @author Federico Cristian Pfund
  */

object StockDataFinancial {

  /**
    * Función para cargar datos y eliminar filas con valores nulos.
    *
    * @param spark     Objeto SparkSession para interactuar con Spark.
    * @param dataPath  Ruta del archivo CSV que contiene los datos financieros.
    * @return          DataFrame con los datos cargados y filas sin valores nulos.
    */
  def loadData(spark: SparkSession, dataPath: String): DataFrame = {
    spark.read.schema(schema).csv(dataPath).na.drop()
  }

  /**
    * Función para guardar el DataFrame en formato Parquet.
    *
    * @param data        DataFrame que se desea guardar.
    * @param outputPath  Ruta donde se guardará el archivo Parquet.
    */
  def saveAsParquet(data: DataFrame, outputPath: String): Unit = {
    data.write.parquet(outputPath)
  }

  /**
    * Función para calcular la media del precio.
    *
    * @param stockData  DataFrame que contiene los datos financieros.
    * @return           Valor Double que representa la media del precio.
    */
  def calcularMediaPrecio(stockData: DataFrame): Double = {
    stockData.agg(avg("price")).head().getAs[Double](0)
  }

  /**
    * Función para agregar la columna de cambio porcentual.
    *
    * @param df  DataFrame que contiene los datos financieros.
    * @return    DataFrame con la columna adicional de cambio porcentual.
    */
  def agregarCambioPorcentual(df: DataFrame): DataFrame = {
    df.withColumn("change_percent", col("change") / col("price") * 100)
  }

  /**
    * Función para filtrar datos con cambio positivo.
    *
    * @param df  DataFrame que contiene los datos financieros.
    * @return    DataFrame filtrado con datos de cambio positivo.
    */
  def filtrarCambioPositivo(df: DataFrame): DataFrame = {
    df.filter(col("change") > 0)
  }

  /**
    * Función para calcular el volumen total por nombre.
    *
    * @param df  DataFrame que contiene los datos financieros.
    * @return    DataFrame con la columna de volumen total por nombre.
    */
  def calcularVolumenTotal(df: DataFrame): DataFrame = {
    df.groupBy("name").agg(sum("volume").alias("total_volume"))
  }

  /**
    * Función para realizar un join entre dos DataFrames.
    *
    * @param df1         Primer DataFrame para el join.
    * @param df2         Segundo DataFrame para el join.
    * @param joinColumn  Nombre de la columna común para el join.
    * @return            DataFrame resultante del join.
    */
  def joinDataFrames(df1: DataFrame, df2: DataFrame, joinColumn: String): DataFrame = {
    df1.join(df2, Seq(joinColumn), "inner")
  }

  /**
    * Función para calcular el cambio acumulado por nombre.
    *
    * @param df  DataFrame que contiene los datos financieros.
    * @return    DataFrame con la columna de cambio acumulado por nombre.
    */
  def calcularCambioAcumulado(df: DataFrame): DataFrame = {
    val windowSpec = Window.partitionBy("name").orderBy("time")
    df.withColumn("cambio_acumulado", sum("change").over(windowSpec))
  }

  /**
    * Función para calcular el promedio móvil del precio por nombre.
    *
    * @param df          DataFrame que contiene los datos financieros.
    * @param windowSize  Tamaño de la ventana para el promedio móvil.
    * @return            DataFrame con la columna de promedio móvil.
    */
  def calcularPromedioMovil(df: DataFrame, windowSize: Int): DataFrame = {
    val windowSpec = Window.orderBy("time").rowsBetween(-windowSize, 0)
    df.withColumn("promedio_movil", avg("price").over(windowSpec))
  }

  /**
    * Función para calcular el rango de precio (diferencia entre el máximo y el mínimo) por nombre.
    *
    * @param df  DataFrame que contiene los datos financieros.
    * @return    DataFrame con la columna de rango de precio.
    */
  def calcularRangoPrecio(df: DataFrame): DataFrame = {
    val windowSpec = Window.orderBy("time")
    df.withColumn("rango_precio", max("price").over(windowSpec) - min("price").over(windowSpec))
  }

  /**
    * Función para calcular la suma acumulada del volumen por nombre.
    *
    * @param df  DataFrame que contiene los datos financieros.
    * @return    DataFrame con la columna de suma acumulada de volumen por nombre.
    */
  def calcularSumaAcumuladaVolumen(df: DataFrame): DataFrame = {
    val windowSpec = Window.orderBy("time")  // Ordena por "time" sin partición
    df.withColumn("suma_acumulada_volumen", sum("volume").over(windowSpec))
  }

  /**
    * Función para agregar la hora a la columna de tiempo.
    *
    * @param df  DataFrame que contiene los datos financieros.
    * @return    DataFrame con la columna de tiempo convertida a formato de hora.
    */
  def agregarHora(df: DataFrame): DataFrame = {
     df.withColumn("time", to_timestamp(col("time"), "h:mma"))
  }

  /**
    * Función para extraer solo la fecha de la columna de fecha.
    *
    * @param df  DataFrame que contiene los datos financieros.
    * @return    DataFrame con la columna de fecha extraída.
    */
  def extraerFecha(df: DataFrame): DataFrame = {
       df.withColumn("date", to_date(col("date"), "MM/dd/yyyy"))
  }

  /**
    * Función principal que ejecuta las transformaciones y muestra resultados.
    *
    * @param dataPath    Ruta del archivo CSV que contiene los datos financieros.
    * @param outputPath  Ruta donde se guardará el archivo Parquet.
    */
  def run(dataPath: String, outputPath: String): Unit = {
    // Configurar una sesión de Spark
    val spark = SparkSession.builder
        .appName("StockDataFinancial")
        .config("spark.master", "local")
        .config("spark.sql.legacy.timeParserPolicy", "LEGACY")
        .config("spark.sql.shuffle.partitions", "8")  // Número de particiones para operaciones shuffle
        .config("spark.default.parallelism", "8")     // Número de tareas en paralelo
        .config("spark.executor.memory", "2g")         // Memoria por ejecutor
        .config("spark.executor.cores", "2")           // Número de núcleos por ejecutor
        .getOrCreate()

    // Cargar datos y eliminar filas con valores nulos
    val dfr: DataFrame = loadData(spark, dataPath)
    dfr.cache()  // Almacenar en caché para mejorar el rendimiento

    // Mostrar resultados
    // ...

    // Aplicar transformaciones
    val hora = agregarHora(dfr).cache()
    val df = extraerFecha(hora).cache()
    val tendenciaPrecio = calcularTendenciaPrecio(df)
    val mediaPrecio = calcularMediaPrecio(df)
    val datosConCambio = agregarCambioPorcentual(df)
    val datosCambioPositivo = filtrarCambioPositivo(df)

    val rangoPrecio = calcularRangoPrecio(df)
    val sumaAcumuladaVolumen = calcularSumaAcumuladaVolumen(df)
    val volumenTotal = calcularVolumenTotal(df)
    val promedioMovil = calcularPromedioMovil(df, windowSize = 3)
    val cambioAcumulado = calcularCambioAcumulado(df)

    // Realizar un join entre los dos DataFrames utilizando una columna común (por ejemplo, 'name')
    val joinedData = joinDataFrames(volumenTotal, datosCambioPositivo, "name")

    // Mostrar resultados
    println(s"Media del precio: $mediaPrecio")
    tendenciaPrecio.show()
    datosConCambio.show()
    promedioMovil.show()
    cambioAcumulado.show()
    sumaAcumuladaVolumen.show()
    datosCambioPositivo.show()
    rangoPrecio.show()
    volumenTotal.show()
    joinedData.show()

    // Guardar el DataFrame en formato Parquet
    // saveAsParquet(df, outputPath)

    // Detener la sesión de Spark al final
    spark.stop()
  }
}


