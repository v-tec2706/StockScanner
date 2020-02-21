import model.Index
import scrapper.parser.ElementType
import scrapper.{Configuration, DataService, Scrapper}

object Main {

  implicit val elementType: ElementType.Value = ElementType.INDEX

  def main(args: Array[String]): Unit = {
    val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val scrapper = new Scrapper
    //    var fullCompaniesList: List[Company] = Nil
    //    for (letter <- letters) {
    //      fullCompaniesList = fullCompaniesList.:::(scrapper.getDocuments(Configuration.pathToScrap + letter))
    //    }
    val fullCompaniesList = scrapper.getDocuments(Configuration.pathToScrap)
    //val fullCompaniesListFiltered = scrapper.filterByCompanyName(fullCompaniesList, Configuration.companiesToBeExtracted)
    //val companies = scrapper.convertToRDD(fullCompaniesListFiltered)
    val companies = scrapper.convertToRDD(fullCompaniesList.asInstanceOf[List[Index]])
    val dataService = new DataService(DataService.getContext)
    val resultAsDF = dataService.toDataFrame(companies, Configuration.columnNames: _*)
    dataService.saveInFilesByNames(resultAsDF, Configuration.outputPath)
  }
}
