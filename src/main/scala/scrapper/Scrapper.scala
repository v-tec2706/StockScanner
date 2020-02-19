package scrapper

import java.net.URL

import model.{Company, Element, Index}
import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import org.apache.spark.rdd.RDD
import org.htmlcleaner.{CleanerProperties, HtmlCleaner}
import scrapper.parser.{ElementType, _}

class Scrapper {
  val browser: Browser = JsoupBrowser()
  val dataLoader = new DataService(DataService.getContext())

  def getDocuments(elementType: ElementType.Value, path: String): List[Element] = {
    val cleanerProperties = new CleanerProperties()
    val scrappedData = new HtmlCleaner(cleanerProperties).clean(new URL(path))
    val filteredValues = Parser.filterParsedRecords(elementType, scrappedData).toList
    Parser.doMapping(elementType, filteredValues)
  }

  def convertToRDD(element: List[Element]): RDD[_ >: Company with Index <: Element] = {
    element match {
      case a: List[Company] => convertCompanyToRDD(a)
      case b: List[Index] => convertIndexToRDD(b)
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
