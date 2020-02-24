package data

import model.Company
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SparkSession}

class Reader {

  def loadData(path: String): DataFrame = {
    SparkSession.builder()
      .master("local[*]")
      .appName("StockScanner")
      .getOrCreate().sqlContext.read.json(path)
  }

  def parse(df: DataFrame): RDD[Company] = {
    df.rdd.map(x => new Company(percentageChange = x.getDouble(0).toFloat,
      rate = x.getString(1).toFloat,
      name = x.getString(2),
      openingPrice = x.getString(3).toFloat,
      change = x.getDouble(4).toFloat,
      max = x.getString(5).toFloat,
      transactionNumber = x.getString(6),
      volume = x.getDouble(7).toFloat))
  }
}
