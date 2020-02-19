package scrapper.parser

import org.htmlcleaner.TagNode

object IndexParser extends Parser {

  override def batchSize: Int = 12

  override def dataPattern: Seq[String] = "a" :: List.fill(8)("td").:::(List("tr", "td").reverse).reverse

  override def recordsPredicate(x: TagNode): Boolean = (x.getName == "a" && x.getAttributes.size() == 2) || x.getName == "td" || x.getName == "tr"
}
