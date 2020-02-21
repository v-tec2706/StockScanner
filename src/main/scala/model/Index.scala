package model

import org.htmlcleaner.TagNode
import scrapper.parser.IndexParser
import utils.StringUtil.{removeLiterals, stringToFloat}

case class Index(
                  name: String,
                  rate: Float,
                  change: Float,
                  percentageChange: Float,
                  openingPrice: Float,
                  max: Float,
                  min: Float,
                  volume: Float
                ) extends Element {
  override def toString: String = {
    s"""|  Name: $name,
        |  rate: $rate, change: $percentageChange,  opening price: $openingPrice, max price: $max
        |""".stripMargin
  }
}

object Index {

  def convertToIndex(elements: List[TagNode]): Option[Index] = {
    if (elements.length != IndexParser.batchSize) None
    else Some(Index(
      elements.head.getAttributeByName("title"),
      stringToFloat(removeLiterals(elements(1).getAllChildren.get(0).toString, "&nbsp;")),
      stringToFloat(elements(2).getAllChildren.get(0).toString),
      stringToFloat(elements(3).getAllChildren.get(0).toString.filterNot(x => x == '%')),
      stringToFloat(removeLiterals(elements(4).getAllChildren.get(0).toString, "&nbsp;")),
      stringToFloat(removeLiterals(elements(5).getAllChildren.get(0).toString, "&nbsp;")),
      stringToFloat(removeLiterals(elements(6).getAllChildren.get(0).toString, "&nbsp;")),
      stringToFloat(removeLiterals(elements(7).getAllChildren.get(0).toString, "&nbsp;"))))
  }
}
