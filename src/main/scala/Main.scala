import configuration.AppConfiguration
import data.{SparkContextLoader, Writer}
import model.{Element, Index}
import processor.StockRateValueProcessor
import scrapper.Scrapper
import scrapper.parser.ElementType

object Main {

  implicit val elementType: ElementType.Value = ElementType.COMPANY

  def main(args: Array[String]): Unit = {

    val sparkContext = SparkContextLoader.getContext
    //    scrapCompanies()

    //    val fullCompaniesList = scrapper.getDocuments(Configuration.pathToScrap)
    //    val fullCompaniesListFiltered = scrapper.filterByCompanyName(fullCompaniesList, Configuration.companiesToBeExtracted)
    //    val companies = scrapper.convertToRDD(fullCompaniesListFiltered)
    processStockValues("./output/21-02-2020/*/*/*.json")
  }

  def processStockValues(output: String): Unit = {
    val stockProcessor = new StockRateValueProcessor
    stockProcessor.process(output)
  }

  def scrapCompanies(): Unit = {
    val letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val scrapper = new Scrapper
    var fullCompaniesList: List[Element] = Nil
    for (letter <- letters) {
      fullCompaniesList = fullCompaniesList.:::(scrapper.getDocuments(AppConfiguration.pathToScrap + letter))
    }
    val companies = scrapper.convertToRDD(fullCompaniesList.asInstanceOf[List[Index]])
    val dataService = new Writer
    val resultAsDF = dataService.toDataFrame(companies, AppConfiguration.columnNames: _*)
    dataService.saveInFilesByNames(resultAsDF, AppConfiguration.outputPath)
  }
}
