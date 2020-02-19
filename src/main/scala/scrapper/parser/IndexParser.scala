package scrapper.parser

import domain.Index
import org.htmlcleaner.TagNode

object IndexParser {


  val IndexDataBatchSize = 12
  val IndexDataPattern: Seq[String] = "a" :: List.fill(8)("td").:::(List("tr", "td").reverse).reverse

  @scala.annotation.tailrec
  def indexMapping(fullList: List[Option[Index]], list: List[TagNode]): List[Option[Index]] = {
    list match {
      case Nil => fullList
      case a: List[TagNode] =>
        val processedBatch = a.take(IndexDataBatchSize)
        val namesExtracted = processedBatch.map(x => x.getName)
        val newList: List[Option[Index]] = if (IndexDataPattern.zip(namesExtracted).forall(x => x._1 == x._2)) {
          fullList.::(Index.convertToIndex(processedBatch))
        } else {
          fullList
        }
        indexMapping(newList, list.tail)
      case _ => fullList
    }
  }


  def filterParsedRecords(tagNode: TagNode): Array[TagNode] = {
    tagNode.getAllElements(true)
      .filter(x => {
        (x.getName == "a" && x.getAttributes.size() == 2) || x.getName == "td" || x.getName == "tr"
      })
  }
}
