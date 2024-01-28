package mlflow

import org.apache.spark.ml.classification.MultilayerPerceptronClassifier
import org.apache.spark.ml.evaluation.BinaryClassificationEvaluator
import org.apache.spark.ml.feature.{VectorAssembler, StandardScaler}
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.ml.evaluation.RegressionEvaluator
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
  def trainAndPredictMultilayerPerceptron(data: DataFrame): DataFrame = {
    // Assemble features into a single vector
    val assembler = new VectorAssembler()
        .setInputCols(Array("change", "open", "high", "low", "volume"))
        .setOutputCol("features")

    val assembledData = assembler.transform(data)

    // Scale features using StandardScaler
    val scaler = new StandardScaler()
        .setInputCol("features")
        .setOutputCol("scaledFeatures")
        .setWithStd(true)
        .setWithMean(true)

    val scaledData = scaler.fit(assembledData).transform(assembledData)

    // Split data into training and testing sets
    val Array(trainingData, testData) = scaledData.randomSplit(Array(0.8, 0.2), seed = 1234)

    // Define the layers for the multilayer perceptron
    val layers = Array(5, 10, 5, 2) // Input, 2 hidden layers with 10 and 5 neurons, and output

    // Define the Multilayer Perceptron model
    val mlp = new MultilayerPerceptronClassifier()
        .setLayers(layers)
        .setBlockSize(128)
        .setMaxIter(100)
        .setFeaturesCol("scaledFeatures")
        .setLabelCol("label") // Assuming you have a "label" column for binary classification

    // Train the Multilayer Perceptron model
    val mlpModel = mlp.fit(trainingData)

    // Make predictions on the test data
    val predictions = mlpModel.transform(testData)

    // Evaluate the model
    val evaluator = new BinaryClassificationEvaluator()
        .setLabelCol("label")
        .setRawPredictionCol("rawPrediction")
        .setMetricName("areaUnderROC")

    val areaUnderROC = evaluator.evaluate(predictions)
    println(s"Area under ROC: $areaUnderROC")

    predictions
  }
}
