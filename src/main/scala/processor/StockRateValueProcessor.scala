package processor

import data.Reader

class StockRateValueProcessor {

  def process(path: String) = {
    val reader: Reader = new Reader
    reader.parse(reader.loadData(path))
  }
}

