package scrapper

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{SaveMode, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

import scala.reflect.ClassTag

object DataService {

  var sparkContext: Option[SparkContext] = None

  def initializeSparkContext(numberOfThreads: Int, name: String): Unit = {
    val sparkConfiguration = new SparkConf()
      .setMaster(s"local[$numberOfThreads]")
      .set("spark.driver.allowMultipleContexts", "true")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .setAppName(name)

    sparkContext = Some(new SparkContext(sparkConfiguration))
  }

  def getContext(): SparkContext = {
    if (sparkContext.isDefined) sparkContext.get
    else {
      initializeSparkContext(2, "myApp")
      sparkContext.get
    }
  }
}


class DataService(var sparkContext: SparkContext) {
  def load[T:ClassTag](data: List[T]): RDD[T] = {
    val sc = DataService.getContext()
    sc.parallelize(data.toSeq)
  }

  def save(data: RDD[Company], outPath: String, columnNames: String*): Unit = {
    val spark = SparkSession
      .builder
      .getOrCreate()

    import org.apache.spark.sql.functions._
    import spark.implicits._

    data.toDF(columnNames: _*)
      .withColumn("companyNameSymbol", split($"name", "")(0))
      .write.partitionBy("companyNameSymbol").mode(SaveMode.Overwrite).csv(outPath)
  }
}

