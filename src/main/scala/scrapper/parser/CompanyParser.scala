package scrapper.parser

import org.htmlcleaner.TagNode

object CompanyParser extends Parser {

  override def batchSize: Int = 8

  override def dataPattern: Seq[String] = "a" :: List.fill(7)("td")

  override def recordsPredicate(x: TagNode): Boolean = (x.getName == "a" && x.getAttributes.size() == 3) || x.getName == "td"
}
