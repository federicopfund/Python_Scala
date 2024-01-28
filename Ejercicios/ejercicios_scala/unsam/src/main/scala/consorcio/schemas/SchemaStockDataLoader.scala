package Consorcio.schemas

import org.apache.spark.sql.types._

object StockDataSchema {
  // Definir el esquema
  val schema: StructType = StructType(
    Array(
      StructField("name", StringType, nullable = true),
      StructField("price", DoubleType, nullable = true),
      StructField("date", StringType, nullable = true),
      StructField("time", StringType, nullable = true),
      StructField("change", DoubleType, nullable = true),
      StructField("open", DoubleType, nullable = true),
      StructField("high", DoubleType, nullable = true),
      StructField("low", DoubleType, nullable = true),
      StructField("volume", LongType, nullable = true)
    )
  )
}
