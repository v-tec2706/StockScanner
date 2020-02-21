package scrapper.parser

import model.Element
import org.htmlcleaner.TagNode

trait Parser {
  def batchSize: Int

  def dataPattern: Seq[String]

  def recordsPredicate(tagNode: TagNode): Boolean

  def convert(list: List[TagNode]): Option[Element]

  def parse(fullList: List[Option[Element]], list: List[TagNode]): List[Option[Element]] = {
    list match {
      case Nil => fullList
      case a: List[TagNode] =>
        val processedBatch = a.take(batchSize)
        val namesExtracted = processedBatch.map(x => x.getName)
        val newList: List[Option[Element]] = if (dataPattern.zip(namesExtracted).forall(x => x._1 == x._2)) {
          fullList.::(convert(processedBatch))
        } else {
          fullList
        }
        parse(newList, list.tail)
      case _ => fullList
    }
  }

  def filterParsedRecords(tagNode: TagNode): Array[TagNode] = {
    tagNode.getAllElements(true)
      .filter(x => recordsPredicate(x))
  }
}


object Parser {

  def doMapping(elementType: ElementType.Value, list: List[TagNode]): List[Element] = {
    elementType match {
      case ElementType.COMPANY => CompanyParser.parse(List(), list).filter(x => x.isDefined).map(x => x.get)
      case ElementType.INDEX => IndexParser.parse(List(), list).filter(x => x.isDefined).map(x => x.get)
    }
  }

  def filterParsedRecords(elementType: ElementType.Value, tagNode: TagNode): Array[TagNode] = {
    elementType match {
      case ElementType.COMPANY => CompanyParser.filterParsedRecords(tagNode)
      case ElementType.INDEX => IndexParser.filterParsedRecords(tagNode)
    }
  }
}