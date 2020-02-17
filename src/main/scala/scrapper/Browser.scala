package scrapper

abstract class Browser {
  val database: Database

  def getItems()= {
    database.getAllProducts()
  }
}

object SimpleBrowser extends Browser {
   override val database: Database = SimpleDatabase
}
