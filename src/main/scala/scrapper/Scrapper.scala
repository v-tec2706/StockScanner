package scrapper

import java.net.URL

import net.ruippeixotog.scalascraper.browser.JsoupBrowser
import org.apache.spark.rdd.RDD
import org.htmlcleaner.{CleanerProperties, HtmlCleaner, TagNode}
import scrapper.Company.convertToCompany

class Scrapper {
  val browser = JsoupBrowser()
  val dataLoader = new DataService(DataService.getContext())

  def getDocuments(path: String): List[Company] = {
    val cleanerProperties = new CleanerProperties()
    val scrappedData = new HtmlCleaner(cleanerProperties).clean(new URL(path))
    val filteredValues = filterParsedRecords(scrappedData).toList
    mapping(List(), filteredValues)
  }

  def filterParsedRecords(tagNode: TagNode): Array[TagNode] = {
    tagNode.getAllElements(true)
      .filter(x => {
        (x.getName == "a" && x.getAttributes.size() == 3) || x.getName == "td"
      })
  }

  def convertToRDD(companies: List[Company]): RDD[Company] = {
    dataLoader.load(companies)
  }

  def mapping(fullList: List[Company], list: List[TagNode]): List[Company] = {
    list match {
      case Nil => fullList
      case (a: TagNode) :: (b: TagNode) :: (c: TagNode) :: (d: TagNode) :: (e: TagNode) :: (f: TagNode) :: (g: TagNode) :: (h: TagNode) :: rest => {
        val attributesList = List(a, b, c, d, e, f, g, h)
        val namesExtracted = attributesList.map(x => x.getName)
        val newList: List[Company] = namesExtracted match {
          case "a" :: "td" :: "td" :: "td" :: "td" :: "td" :: "td" :: "td" :: Nil => {
            fullList.::(convertToCompany(a, b, c, d, e, f, g, h))
          }
          case _ => fullList
        }
        mapping(newList, list.tail)
      }
      case _ => fullList
    }
  }

  def filterByCompanyName(companies: List[Company], companiesToExtract: List[String]): List[Company] = {
    for (company <- companies if companiesToExtract.contains(company.name)) yield company
  }
}
