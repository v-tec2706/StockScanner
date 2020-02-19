package scrapper.parser

import domain.Company
import org.htmlcleaner.TagNode

import scala.collection.immutable

object CompanyParser {

  val CompanyDataBatchSize = 8
  val CompanyDataPattern: immutable.Seq[String] = "a" :: List.fill(7)("td")

  @scala.annotation.tailrec
  def companyMapping(fullList: List[Option[Company]], list: List[TagNode]): List[Option[Company]] = {
    list match {
      case Nil => fullList
      case a: List[TagNode] =>
        val processedBatch = a.take(CompanyDataBatchSize)
        val namesExtracted = processedBatch.map(x => x.getName)
        val newList: List[Option[Company]] = if (namesExtracted.zip(CompanyDataPattern).forall(x => x._1 == x._2)) {
          fullList.::(Company.convertToCompany(processedBatch))
        } else {
          fullList
        }
        companyMapping(newList, list.tail)
      case _ => fullList
    }
  }

  def filterParsedRecords(tagNode: TagNode): Array[TagNode] = {
    tagNode.getAllElements(true)
      .filter(x => {
        (x.getName == "a" && x.getAttributes.size() == 3) || x.getName == "td"
      })
  }
}
