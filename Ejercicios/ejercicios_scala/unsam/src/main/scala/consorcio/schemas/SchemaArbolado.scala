package Consorcio.schemas

// En el archivo ArboladoSchema.scala
import org.apache.spark.sql.types._

object ArboladoParque {
  val arboladoSchema: StructType = StructType(
    Seq(
      StructField("long", DoubleType, nullable = true),
      StructField("lat", DoubleType, nullable = true),
      StructField("id_arbol", IntegerType, nullable = true),
      StructField("altura_tot", DoubleType, nullable = true),
      StructField("diametro", DoubleType, nullable = true),
      StructField("inclinacio", DoubleType, nullable = true),
      StructField("id_especie", IntegerType, nullable = true),
      StructField("nombre_com", StringType, nullable = true),
      StructField("nombre_cie", StringType, nullable = true),
      StructField("tipo_folla", StringType, nullable = true),
      StructField("espacio_ve", StringType, nullable = true),
      StructField("ubicacion", StringType, nullable = true),
      StructField("nombre_fam", StringType, nullable = true),
      StructField("nombre_gen", StringType, nullable = true),
      StructField("origen", StringType, nullable = true),
      StructField("coord_x", DoubleType, nullable = true),
      StructField("coord_y", DoubleType, nullable = true)
    )
  )
}
