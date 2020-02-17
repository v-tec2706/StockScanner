package scrapper

object StringUtil {
  def stringToFloat(toParse: String): Float = {
    try {
      toParse.replace(",", ".").toFloat
    } catch {
      case _: NumberFormatException => Float.NaN
    }
  }
}
