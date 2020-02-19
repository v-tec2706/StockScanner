import scrapper.parser.ElementType
import scrapper.{Configuration, DataService, Scrapper}

object Main {

  def main(args: Array[String]): Unit = {

    val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val scrapper = new Scrapper
    //    var fullCompaniesList: List[Company] = Nil

    //    for (letter <- letters) {
    //      fullCompaniesList = fullCompaniesList.:::(scrapper.getDocuments(Configuration.pathToScrap + letter))
    //    }

    val fullCompaniesList = scrapper.getDocuments(ElementType.INDEX, Configuration.pathToScrap)
    //        val fullCompaniesListFiltered = scrapper.filterByCompanyName(fullCompaniesList, Configuration.companiesToBeExtracted)

    //    val companies = scrapper.convertToRDD(fullCompaniesListFiltered)
    val companies = scrapper.convertToRDD(fullCompaniesList)
    val dataService = new DataService(DataService.getContext())
    //    val resultAsDF = dataService.toDataFrame(companies, Configuration.columnNames: _*)
    //    dataService.saveInFilesByNames(resultAsDF, Configuration.outputPath)
  }
}
