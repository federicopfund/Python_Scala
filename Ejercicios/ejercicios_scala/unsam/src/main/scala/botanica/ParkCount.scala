package botanica

import org.apache.spark.sql.{SparkSession, DataFrame}
import org.apache.spark.sql.functions._
import scala.io.Source
import java.nio.file.Path
import Consorcio.schemas.ArboladoParque.arboladoSchema
import org.apache.spark.sql.functions._


object ArboladoParque {

  // Crear una sesión de Spark
  val spark = SparkSession.builder
    .appName("ArboladoPark")
    .config("spark.master", "local")  // O especifica la URL del master correspondiente
    .getOrCreate()

  import spark.implicits._

  /**
  * Lee un archivo CSV y devuelve un DataFrame de Spark.
  *
  * @param nuevoArchivo Ruta del archivo CSV.
  * @return DataFrame de Spark.
  */

  def leerParque(nuevoArchivo: Path): DataFrame = {
    try {
      // Leer el archivo CSV como un DataFrame de Spark
      val arboladoDF = spark.read
        .option("header", "true")
        .schema(arboladoSchema)
        .option("numPartitions", 8)  // Número de particiones
        .csv(nuevoArchivo.toString)
        .cache()
      // Mostrar el DataFrame en formato identado
      arboladoDF.show(10, true)

      // Guardar en caché el DataFrame para su reutilización
      arboladoDF
    } catch {
      case _: Throwable =>
        println("No corresponde a una dirección válida.")
        // Devolver un DataFrame vacío en caso de error
        Seq.empty[(String, Double)].toDF()
    }
  }

  /**
  * Calcula la altura promedio de los árboles de especie "Jacarandá".
  *
  * @param arboledaDF DataFrame de árboles.
  * @return Altura promedio de los Jacarandás.
  */
  def alturaPromedioJacaranda(arboledaDF: DataFrame): Double = {
    val alturasJacaranda = arboledaDF.filter($"nombre_com" === "Jacarandá").select("altura_tot").as[Double]

    val promedio: Double = alturasJacaranda
      .agg(avg("altura_tot"))
      .first()
      .getAs[Double]("avg(altura_tot)")

    promedio
  }
  /**
  * Filtra y selecciona las alturas y diámetros de los Jacarandás.
  *
  * @param arboledaDF DataFrame de árboles.
  * @return DataFrame con alturas y diámetros de Jacarandás.
  */
  def altosYDiametrosJacaranda(arboledaDF: DataFrame): DataFrame = {
    arboledaDF.filter($"nombre_com" === "Jacarandá").select("altura_tot", "diametro")
  }

  /**
  * Calcula medidas (altura y diámetro) de varias especies.
  *
  * @param especies   Lista de nombres de especies.
  * @param arboledaDF DataFrame de árboles.
  * @return Mapa con medidas para cada especie.
  */
  def medidasDeEspecies(especies: List[String], arboledaDF: DataFrame): Map[String, List[(Double, Double)]] = {
    especies.map { especie =>
      especie -> arboledaDF.filter($"nombre_com" === especie).select("altura_tot", "diametro").as[(Double, Double)].collect().toList
    }.toMap
  }

  // Agregar Columna de Volumen del Árbol
  def agregarVolumenArbol(arboledaDF: DataFrame): DataFrame = {
    val pi = math.Pi
    arboledaDF.withColumn("volumen_arbol", pow($"diametro" / 2, 2) * pi * $"altura_tot")
  }
  // Filtrar por Especie y Altura Mínima:
  def filtrarPorEspecieYAlturaMinima(arboledaDF: DataFrame, especie: String, alturaMinima: Double): DataFrame = {
    arboledaDF.filter($"nombre_com" === especie && $"altura_tot" >= alturaMinima)
  }

  import org.apache.spark.sql.functions._

  /**
  * Agrega la distancia desde cada punto en arboledaDF al punto de referencia especificado.
  *
  * @param arboledaDF       DataFrame que contiene información sobre los árboles.
  * @param puntoReferencia  Coordenadas del punto de referencia (latitud, longitud).
  * @return                 DataFrame.
  */


  def convertirCoordenadasAGradosDecimales(arboledaDF: DataFrame): DataFrame = {
    // Definir una función UDF para convertir DMS a grados decimales
    val dmsToDecimal = udf((degrees: Double, minutes: Double, seconds: Double, direction: String) => {
      val decimal = degrees + minutes / 60.0 + seconds / 3600.0
      if (direction == "S" || direction == "W") -decimal else decimal
    })

    // Aplicar la función UDF a las columnas de coordenadas
    val arboledaConCoordenadasDecimales = arboledaDF
      .withColumn("lat_decimal", dmsToDecimal($"lat_degrees", $"lat_minutes", $"lat_seconds", $"lat_direction"))
      .withColumn("long_decimal", dmsToDecimal($"long_degrees", $"long_minutes", $"long_seconds", $"long_direction"))

    // Seleccionar las columnas relevantes y devolver el DataFrame resultante
    arboledaConCoordenadasDecimales.select(
      "id_arbol", "altura_tot", "diametro", "lat_decimal", "long_decimal"
    )
  }

   /**
    * Calcula la densidad de cada especie por hectárea y construye un DataFrame.
    *
    * @param arboledaDF          DataFrame de árboles.
    * @param superficieHectareas Superficie en hectáreas.
    * @return DataFrame con densidad por hectárea.
    */

  def calcularDensidadPorHectarea(arboledaDF: DataFrame, superficieHectareas: Double): DataFrame = {
  // Agrupar por especie y calcular la densidad por hectárea
    val densidadDF = arboledaDF.groupBy("nombre_com").agg(
      count("id_arbol").alias("cantidad_arboles"),
      (count("id_arbol") / superficieHectareas).alias("densidad_por_hectarea")
    )

    // Ordenar por densidad descendente
    val densidadOrdenadaDF = densidadDF.orderBy(desc("densidad_por_hectarea"))

    // Mostrar el DataFrame resultante
    densidadOrdenadaDF.show()

    // Devolver el DataFrame con la densidad por hectárea
    densidadOrdenadaDF
  }

  /**
    * Calcula la especie con el promedio más alto de inclinación.
    *
    * @param arboledaDF DataFrame de árboles.
    * @return DataFrame con la especie y su promedio de inclinación.
    */
  def calcularEspecieConMayorInclinacion(arboledaDF: DataFrame): DataFrame = {
    // Calcular el promedio de inclinación por especie
    val promedioInclinacionPorEspecie = arboledaDF.groupBy("nombre_com")
      .agg(avg("inclinacion").as("promedio_inclinacion"))

    // Encontrar la especie con el promedio más alto de inclinación
    val especieConMayorInclinacion = promedioInclinacionPorEspecie
      .orderBy(desc("promedio_inclinacion"))
      .limit(1)
      .select("nombre_com", "promedio_inclinacion")

    especieConMayorInclinacion
  }

  /**
    * Calcula la especie con el promedio más alto de diámetro.
    *
    * @param arboledaDF DataFrame de árboles.
    * @return DataFrame con la especie y su promedio de diámetro.
    */
  def calcularEspecieConMayorDiametro(arboledaDF: DataFrame): DataFrame = {
    // Calcular el promedio de diámetro por especie
    val promedioDiametroPorEspecie = arboledaDF.groupBy("nombre_com")
      .agg(avg("diametro").as("promedio_diametro"))

    // Encontrar la especie con el promedio más alto de diámetro
    val especieConMayorDiametro = promedioDiametroPorEspecie
      .orderBy(desc("promedio_diametro"))
      .limit(1)
      .select("nombre_com", "promedio_diametro")

    especieConMayorDiametro
  }
  
  /**
    * Calcula la especie con la densidad más alta por hectárea.
    *
    * @param arboledaDF DataFrame de árboles.
    * @return DataFrame con la especie y su densidad por hectárea.
    */
  def calcularEspecieConMayorDensidad(arboledaDF: DataFrame): DataFrame = {
    // Calcular la densidad de planta por hectárea para cada especie
    val densidadPorEspecie = arboledaDF.groupBy("nombre_com")
      .agg(count("id_arbol").as("cantidad_arboles"), sum("superficie_hectareas").as("total_superficie"))
      .withColumn("densidad_por_hectarea", col("cantidad_arboles") / col("total_superficie"))

    // Encontrar la especie con la densidad más alta por hectárea
    val especieConMayorDensidad = densidadPorEspecie
      .orderBy(desc("densidad_por_hectarea"))
      .limit(1)
      .select("nombre_com", "densidad_por_hectarea")

    especieConMayorDensidad
  }
}
