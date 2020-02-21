package utils

import java.text.SimpleDateFormat
import java.util.Calendar

object PathGenerator {
  def pathWithHour(prefix: String = ""): String = {
    formatPath(prefix, DateGenerator.hour)
  }

  def formatPath(pathElements: String*): String = {
    val nonEmptyPathElements = pathElements.filter(x => x.nonEmpty)
    val slashes = List.fill(nonEmptyPathElements.length)("/")
    slashes.zip(nonEmptyPathElements).foldLeft("")((x, y) => x.concat(y._1.concat(y._2)))
  }

  def pathWithDate(prefix: String = ""): String = {
    formatPath(prefix, DateGenerator.dateInDayMonthYearFormat)
  }
}

object DateGenerator {
  def dateInDayMonthYearFormat: String = {
    val date = Calendar.getInstance().getTime
    val format = new SimpleDateFormat("dd-MM-yyyy")
    format.format(date)
  }

  def hour: String = {
    val date = Calendar.getInstance().getTime
    val format = new SimpleDateFormat("hh:mm")
    format.format(date)
  }
}