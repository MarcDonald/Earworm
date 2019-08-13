/*
 * Copyright (c) 2019 Marc Donald
 *
 * The MIT License (MIT)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.marcdonald.earworm.utils

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