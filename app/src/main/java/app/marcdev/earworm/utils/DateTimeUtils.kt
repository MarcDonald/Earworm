package app.marcdev.earworm.utils

import android.text.format.DateFormat
import java.util.*

/**
 * Converts date to a format suitable for display
 * @param day The day
 * @param month The month (indexed at 0 the same as the Java calendar, so January is 0)
 * @param year The year
 */
fun formatDateForDisplay(day: Int, month: Int, year: Int): String {
  val calendar = Calendar.getInstance()
  calendar.set(Calendar.DAY_OF_MONTH, day)
  calendar.set(Calendar.MONTH, month)
  calendar.set(Calendar.YEAR, year)
  return formatDateForDisplay(calendar)
}

/**
 * Converts date to a format suitable for display
 * @param calendar Date to convert
 */
fun formatDateForDisplay(calendar: Calendar): String {
  val datePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "ddMMyyyy")
  val formattedDate = DateFormat.format(datePattern, calendar)
  return formattedDate.toString()
}

/**
 * Formats the date for displaying on a header
 * @param month Integer value of the month, indexed at 0
 * @param year Integer value of the year
 * @return Full name of the month in string format
 */
fun formatDateForHeaderDisplay(month: Int, year: Int): String {
  val calendar = Calendar.getInstance()
  calendar.set(Calendar.MONTH, month)
  calendar.set(Calendar.YEAR, year)
  val datePattern = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MMMMyyyy")
  val formattedDate = DateFormat.format(datePattern, calendar)
  return formattedDate.toString()
}