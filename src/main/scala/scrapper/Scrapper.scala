package scrapper

import java.net.URL

import data.Writer
import model.{Company, Element, Index}
import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import org.apache.spark.rdd.RDD
import org.htmlcleaner.{CleanerProperties, HtmlCleaner}
import scrapper.parser.{ElementType, _}

class Scrapper {
  val browser: Browser = JsoupBrowser()
  val dataLoader = new Writer

  def getDocuments(path: String)(implicit elementType: ElementType.Value): List[Element] = {
    val cleanerProperties = new CleanerProperties()
    val scrappedData = new HtmlCleaner(cleanerProperties).clean(new URL(path))
    val filteredValues = Parser.filterParsedRecords(elementType, scrappedData).toList
    Parser.doMapping(elementType, filteredValues)
  }

  def convertToRDD(element: List[Element])(implicit elementType: ElementType.Value): RDD[_ >: Company with Index <: Element] = {
    elementType match {
      case ElementType.COMPANY => convertCompanyToRDD(element.asInstanceOf[List[Company]])
      case ElementType.INDEX => convertIndexToRDD(element.asInstanceOf[List[Index]])
    }
  }

  def convertCompanyToRDD(companies: List[Company]): RDD[Company] = {
    dataLoader.load(companies)
  }

  def convertIndexToRDD(companies: List[Index]): RDD[Index] = {
    dataLoader.load(companies)
  }

  def filterByCompanyName(companies: List[Company], companiesToExtract: List[String]): List[Company] = {
    for (company <- companies if companiesToExtract.contains(company.name)) yield company
  }
}
