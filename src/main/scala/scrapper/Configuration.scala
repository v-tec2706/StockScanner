package scrapper

import utils.PathGenerator._

object Configuration {
  def columnNames = List("name", "rate", "change", "percentageRate", "transactionNumber", "volume", "openingPrice", "max")

  def outputPath: String = "." + pathWithDate("output") + pathWithHour()

  def pathToScrap = "https://www.bankier.pl/gielda/notowania/akcje?start="

  //
  //  def pathToScrap = "https://www.bankier.pl/gielda/notowania/indeksy-gpw"

  def companiesToBeExtracted = List("LIVECHAT", "PEP", "ARCUS", "ARTIFEX")
}
