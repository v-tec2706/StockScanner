package data

import org.apache.spark.{SparkConf, SparkContext}

object SparkContextLoader {
  var sparkContext: Option[SparkContext] = None

  def getContext: SparkContext = {
    if (sparkContext.isDefined) sparkContext.get
    else {
      initializeSparkContext(2, "myApp")
      sparkContext.get
    }
  }

  def initializeSparkContext(numberOfThreads: Int, name: String): Unit = {
    val sparkConfiguration = new SparkConf()
      .setMaster(s"local[$numberOfThreads]")
      .set("spark.driver.allowMultipleContexts", "true")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .setAppName(name)

    sparkContext = Some(new SparkContext(sparkConfiguration))
  }
}
