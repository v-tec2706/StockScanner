package utils

object StringUtil {
  def stringToFloat(toParse: String): Float = {
    try {
      toParse.replace(",", ".").toFloat
    } catch {
      case _: NumberFormatException => Float.NaN
    }
  }

  def removeLiterals(s: String, pattern: String): String = {
    s.filter(x => !pattern.contains(x))
  }
}
