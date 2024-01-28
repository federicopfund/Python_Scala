package mlflow

import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature.VectorAssembler
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.sql.DataFrame
import org.apache.spark.sql.functions.{hour, minute, col}
import contable.StockDataFinancial
/**
  * Objeto MlStockDataFinancial que proporciona funciones para el análisis y predicción de datos financieros utilizando
  * técnicas de aprendizaje automático en Spark MLlib.
  *
  * @object
  */
object MlStockDataFinancial {

  /**
    * Función mejorada para calcular la tendencia del precio por nombre usando regresión lineal.
    *
    * @param df        DataFrame con los datos financieros.
    * @param outputCol Nombre de la columna que contendrá las predicciones.
    * @return DataFrame con predicciones de tendencia del precio.
    */
  def calcularTendenciaPrecio(df: DataFrame, outputCol: String = "trend_prediction"): DataFrame = {
    try {
      // Convierte la columna "time" a minutos del día (utiliza tu lógica específica aquí)
      val dfWithMinutesOfDay = df.withColumn("minutes_of_day", hour(col("time")) * 60 + minute(col("time")))

      // Ensamblar las características y ajustar un modelo de regresión lineal
      val assembler = new VectorAssembler()
        .setInputCols(Array("minutes_of_day"))
        .setOutputCol("features")

      val lr = new LinearRegression()
        .setLabelCol("price")
        .setFeaturesCol("features")

      val pipeline = new Pipeline().setStages(Array(assembler, lr))
      val model = pipeline.fit(dfWithMinutesOfDay)

      // Realizar predicciones con el modelo
      val predictions = model.transform(dfWithMinutesOfDay).withColumnRenamed("prediction", outputCol)

      // Imprimir métricas de evaluación
      val evaluator = new RegressionEvaluator()
        .setLabelCol("price")
        .setPredictionCol(outputCol)
        .setMetricName("rmse")  // Puedes ajustar la métrica según tus necesidades

      val rmse = evaluator.evaluate(predictions)
      println(s"Root Mean Squared Error (RMSE): $rmse")

      predictions
    } catch {
      case e: Exception =>
        println(s"Error en la función calcularTendenciaPrecio: ${e.getMessage}")
        df // Devuelve el DataFrame original en caso de error
    }
  }

  /**
    * Función para realizar transformaciones y entrenar el modelo.
    *
    * @param stockData DataFrame con los datos financieros.
    * @return DataFrame con predicciones del modelo.
    */
  def trainAndPredict(stockData: DataFrame): DataFrame = {
    // Ensamblar las características en un solo vector
    val assembler = new VectorAssembler()
      .setInputCols(Array("change", "open", "high", "low", "volume"))
      .setOutputCol("features")

    val assembledData = assembler.transform(stockData)

    // Dividir los datos en conjuntos de entrenamiento y prueba
    val Array(trainingData, testData) = assembledData.randomSplit(Array(0.8, 0.2), seed = 1234)

    // Definir el modelo de la red neuronal
    val layers = Array(5, 10, 5, 1) // Capas de entrada, oculta, oculta y salida
    val mlp = new MultilayerPerceptronRegressor()
      .setLayers(layers)
      .setBlockSize(128)
      .setMaxIter(100)
      .setFeaturesCol("features")
      .setLabelCol("price")

    // Entrenar el modelo
    val mlpModel = mlp.fit(trainingData)

    // Realizar predicciones en el conjunto de prueba
    val predictions = mlpModel.transform(testData)

    // Evaluar el rendimiento del modelo
    val evaluator = new RegressionEvaluator()
      .setLabelCol("price")
      .setPredictionCol("prediction")
      .setMetricName("rmse")

    val rmse = evaluator.evaluate(predictions)
    println(s"Root Mean Squared Error (RMSE): $rmse")

    predictions
  }
}
