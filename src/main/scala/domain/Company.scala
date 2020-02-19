package domain

import org.htmlcleaner.TagNode
import utils.StringUtil._

object Company {
  def convertToCompany(elements: List[TagNode]): Option[Company] = {
    if (elements.length != 8) None
    else {
      Some(Company(
        elements.head.getAttributeByName("href").split("/").last,
        stringToFloat(elements(1).getAllChildren.get(0).toString),
        stringToFloat(elements(2).getAllChildren.get(0).toString),
        stringToFloat(elements(3).getAllChildren.get(0).toString.filterNot(x => x == '%')),
        elements(4).getAllChildren.get(0).toString,
        stringToFloat(removeLiterals(elements(5).getAllChildren.get(0).toString, "&nbsp;")),
        stringToFloat(elements(6).getAllChildren.get(0).toString),
        stringToFloat(elements(7).getAllChildren.get(0).toString)))
    }
  }
}

case class Company(name: String,
                   rate: Float,
                   change: Float,
                   percentageChange: Float,
                   transactionNumber: String,
                   volume: Float,
                   openingPrice: Float,
                   max: Float) extends Element {
  override def toString(): String = {
    s"""|  Name: $name,
        |  rate: $rate, change: $percentageChange, transactions: $transactionNumber, opening price: $openingPrice, max price: $max
        |""".stripMargin
  }
}

