package scrapper

import org.apache.spark.rdd.RDD
import org.apache.spark.sql._
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

  val spark = SparkSession
    .builder
    .getOrCreate()

  import spark.implicits._

  def load[T: ClassTag](data: List[T]): RDD[T] = {
    val sc = DataService.getContext()
    sc.parallelize(data.toSeq)
  }

  def partitionBy(data: DataFrameWriter[Row], columnName: String): DataFrameWriter[Row] = {
    data.partitionBy(columnName)
  }

  def toDataFrame(data: RDD[Company], columnNames: String*): DataFrame = {
    data.toDF(columnNames: _*).coalesce(1)
  }

  def addColumnExtractingName(data: DataFrame): DataFrame = {
    import org.apache.spark.sql.functions._

    data.withColumn("companyNameSymbol", split($"name", "")(0))
  }

  def saveInFilesByNames(data: DataFrame, outputPath: String): Unit = {
    val extendedData = addColumnExtractingName(data)
    val partitionedData = partitionBy(extendedData.write, "companyNameSymbol")
    save(partitionedData, outputPath)
  }

  def save(data: DataFrameWriter[Row], outputPath: String): Unit = {
    data
      .mode(SaveMode.Overwrite)
      .csv(outputPath)
  }
}

