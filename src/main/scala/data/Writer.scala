package data

import model.{Company, Index}
import org.apache.spark.rdd.RDD
import org.apache.spark.sql._
import org.apache.spark.{SparkConf, SparkContext}
import scrapper.parser.ElementType

import scala.reflect.ClassTag

object Writer {

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


class Writer {
  val spark: SparkSession = SparkSession
    .builder
    .getOrCreate()

  import spark.implicits._

  def load[A: ClassTag](data: List[A]): RDD[A] = {
    val sc = SparkContextLoader.getContext
    sc.parallelize(data)
  }

  def partitionBy(data: DataFrameWriter[Row], columnName: String): DataFrameWriter[Row] = {
    data.partitionBy(columnName)
  }


  def toDataFrame[A](element: RDD[A], columnNames: String*)(implicit elementType: ElementType.Value): DataFrame = {
    elementType match {
      case ElementType.COMPANY =>
        companyToDataFrame(element.asInstanceOf[RDD[Company]], columnNames: _*)
      case ElementType.INDEX =>
        indexToDataFrame(element.asInstanceOf[RDD[Index]], columnNames: _*)
    }
  }

  def companyToDataFrame(data: RDD[Company], columnNames: String*): DataFrame = {
    data.toDF(columnNames: _*).coalesce(1)
  }

  def indexToDataFrame(data: RDD[Index], columnNames: String*): DataFrame = {
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
      .json(outputPath)
  }
}

