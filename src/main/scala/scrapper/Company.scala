package scrapper

import org.htmlcleaner.TagNode
import scrapper.StringUtil.stringToFloat

object Company {
  def convertToCompany(name: TagNode,
                       rate: TagNode,
                       change: TagNode,
                       percentageChange: TagNode,
                       transactionNumber: TagNode,
                       volume: TagNode,
                       openingPrice: TagNode,
                       max: TagNode): Company = {

    Company(
      name.getAttributeByName("href").split("/").last,
      stringToFloat(rate.getAllChildren.get(0).toString),
      stringToFloat(change.getAllChildren.get(0).toString),
      stringToFloat(percentageChange.getAllChildren.get(0).toString.filterNot(x => x == '%')),
      transactionNumber.getAllChildren.get(0).toString,
      volume.getAllChildren.get(0).toString,
      stringToFloat(openingPrice.getAllChildren.get(0).toString),
      stringToFloat(max.getAllChildren.get(0).toString))
  }
}

case class Company(name: String,
                   rate: Float,
                   change: Float,
                   percentageChange: Float,
                   transactionNumber: String,
                   volume: String,
                   openingPrice: Float,
                   max: Float) {
  override def toString(): String = {
    s"""|  Name: $name,
        |  rate: $rate, change: $percentageChange, transactions: $transactionNumber, opening price: $openingPrice, max price: $max
        |""".stripMargin
  }
}
