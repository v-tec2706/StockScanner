package scrapper

import bookExercises.{Counter, Person}


object Main {

  def main(args: Array[String]): Unit = {
    val outputPath = "./output/data/";

    val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val scrapper = new Scrapper
    var fullCompaniesList: List[Company] = Nil

    for (letter <- letters) {
      val pathToScrap = "https://www.bankier.pl/gielda/notowania/akcje?start="
      fullCompaniesList = fullCompaniesList.:::(scrapper.getDocuments(pathToScrap + letter))
    }

    val columnNames = List("name", "rate", "change", "percentageRate", "transactionNumber", "volume", "openingPrice", "max")


//    new DataService(DataService.getContext()).save()
    new DataService(DataService.getContext())
      .save(scrapper.convertToRDD(fullCompaniesList),
        outputPath,
        columnNames: _*)
  }

}
