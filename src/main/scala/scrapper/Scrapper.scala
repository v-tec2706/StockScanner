package scrapper

import java.net.URL

import domain.{Company, Index}
import net.ruippeixotog.scalascraper.browser.{Browser, JsoupBrowser}
import org.apache.spark.rdd.RDD
import org.htmlcleaner.{CleanerProperties, HtmlCleaner}
import scrapper.parser.IndexParser

class Scrapper {
  val browser: Browser = JsoupBrowser()
  val dataLoader = new DataService(DataService.getContext())

  def getDocuments(path: String): List[Index] = {
    val cleanerProperties = new CleanerProperties()
    val scrappedData = new HtmlCleaner(cleanerProperties).clean(new URL(path))
    //    val filteredValues = CompanyParser.filterParsedRecords(scrappedData).toList
    //    CompanyParser.companyMapping(List(), filteredValues).filter(x => x.isDefined).map(x => x.get)
    val filteredValues = IndexParser.filterParsedRecords(scrappedData).toList
    IndexParser.indexMapping(List(), filteredValues).filter(x => x.isDefined).map(x => x.get)
  }

  def convertToRDD(companies: List[Company]): RDD[Company] = {
    dataLoader.load(companies)
  }

  def convertIndexToRDD(companies: List[Index]): RDD[Index] = {
    dataLoader.load(companies)
  }

  def filterByCompanyName(companies: List[Company], companiesToExtract: List[String]): List[Company] = {
    for (company <- companies if companiesToExtract.contains(company.name)) yield company
  }
}
