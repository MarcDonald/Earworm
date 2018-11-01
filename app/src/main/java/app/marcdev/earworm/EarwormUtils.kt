package app.marcdev.earworm

import android.content.Context
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import app.marcdev.earworm.database.FavouriteItem
import timber.log.Timber

const val SONG = 0
const val ALBUM = 1
const val ARTIST = 2
const val GENRE = 3
const val HEADER = 4
val DEFAULT_FILTER = ItemFilter(1, 0, 1900, 31, 11, 2099, true, true, true, "")

/**
 * Replaces a fragment in a frame with another fragment
 * @param fragment The fragment to display
 * @param fragmentManager The Fragment Manager
 * @param frameId The ID of the frame to display the new fragment in
 */
fun setFragment(fragment: Fragment, fragmentManager: FragmentManager, frameId: Int) {
  Timber.d("Log: setFragment: Replacing frame $frameId with fragment $fragment")
  val fragmentTransaction = fragmentManager.beginTransaction()
  fragmentTransaction.replace(frameId, fragment)
  fragmentTransaction.commit()
}

/**
 * Converts date to a format suitable for display
 * @param day The day
 * @param month The month (indexed at 0 the same as the Java calendar, so January is 0)
 * @param year The year
 */
fun formatDateForDisplay(day: Int, month: Int, year: Int): String {
  Timber.d("Log: formatDateForDisplay: Started with day = $day, month = $month, year = $year")
  // Add 1 to month to make it non-zero indexed (January will now be 1 rather than 0)
  return "$day/${month + 1}/$year"
}

/**
 * Changes the color of a drawable in an ImageView to indicate whether it is activated or not
 * @param context Context
 * @param button The button to change the color of
 * @param
 */
fun changeColorOfImageButtonDrawable(context: Context, button: ImageButton, isActivated: Boolean) {
  Timber.v("Log: changeColorOfImageButtonDrawable: Started")

  if(isActivated) {
    DrawableCompat.setTint(button.drawable, ContextCompat.getColor(context, R.color.colorAccent))
  } else {
    DrawableCompat.setTint(button.drawable, ContextCompat.getColor(context, R.color.black))
  }
}

/**
 * Filters a list based on a filter input by the user
 * @param allItems The complete list of items from the database
 * @param filter The filter to apply
 */
fun applyFilter(allItems: MutableList<FavouriteItem>, filter: ItemFilter): MutableList<FavouriteItem> {
  val filteredItems = mutableListOf<FavouriteItem>()
  filteredItems.addAll(allItems)

  val startDayTwoDigit = if(filter.startDay < 10)
    "0${filter.startDay}"
  else {
    "${filter.startDay}"
  }

  val startMonthTwoDigit = if(filter.startMonth < 10)
    "0${filter.startMonth}"
  else {
    "${filter.startMonth}"
  }

  val filterCompleteDateStart: Int = "${filter.startYear}$startMonthTwoDigit$startDayTwoDigit".toInt()

  val endDayTwoDigit = if(filter.endDay < 10)
    "0${filter.endDay}"
  else {
    "${filter.endDay}"
  }

  val endMonthTwoDigit = if(filter.endMonth < 10)
    "0${filter.endMonth}"
  else {
    "${filter.endMonth}"
  }

  val filterCompleteDateEnd: Int = "${filter.endYear}$endMonthTwoDigit$endDayTwoDigit".toInt()

  for(x in 0 until allItems.size) {
    if(allItems[x].type == SONG && !filter.includeSongs) {
      filteredItems.remove(allItems[x])
    }

    if(allItems[x].type == ALBUM && !filter.includeAlbums) {
      filteredItems.remove(allItems[x])
    }

    if(allItems[x].type == ARTIST && !filter.includeArtists) {
      filteredItems.remove(allItems[x])
    }

    val dayTwoDigit = if(allItems[x].day < 10)
      "0${allItems[x].day}"
    else {
      "${allItems[x].day}"
    }

    val monthTwoDigit = if(allItems[x].month < 10)
      "0${allItems[x].month}"
    else {
      "${allItems[x].month}"
    }

    val completeDate = "${allItems[x].year}$monthTwoDigit$dayTwoDigit"
    val completeDateI: Int = completeDate.toInt()

    if(completeDateI < filterCompleteDateStart || completeDateI > filterCompleteDateEnd) {
      filteredItems.remove(allItems[x])
    }

    if(!(allItems[x].albumName.contains(filter.searchTerm, true))
       && !(allItems[x].artistName.contains(filter.searchTerm, true))
       && !(allItems[x].songName.contains(filter.searchTerm, true))
       && !(allItems[x].genre.contains(filter.searchTerm, true))
    ) {
      filteredItems.remove(allItems[x])
    }
  }

  return filterByDateDescending(filteredItems)
}

fun filterByDateDescending(items: MutableList<FavouriteItem>): MutableList<FavouriteItem> {
  val filteredItems = items.sortedWith(
    compareBy(
      { -it.year },
      { -it.month },
      { -it.day }))

  return filteredItems.toMutableList()
}

fun addListHeaders(allItems: MutableList<FavouriteItem>): List<FavouriteItem> {
  val listWithHeaders = mutableListOf<FavouriteItem>()
  listWithHeaders.addAll(allItems)

  var lastMonth = allItems.first().month + 1
  var lastYear = allItems.first().year
  val headersToAdd = mutableListOf<Pair<Int, FavouriteItem>>()

  for(x in 0 until allItems.size) {
    if(((allItems[x].month < lastMonth) && (allItems[x].year == lastYear))
       || (allItems[x].month > lastMonth) && (allItems[x].year < lastYear)
       || (allItems[x].year < lastYear)
    ) {
      Timber.i("Log: addListHeaders: x = $x")
      val header = FavouriteItem("", "", "", "", 0, allItems[x].month, allItems[x].year, HEADER)
      lastMonth = allItems[x].month
      lastYear = allItems[x].year
      headersToAdd.add(Pair(x, header))
    }
  }

  for((add, x) in (0 until headersToAdd.size).withIndex()) {
    listWithHeaders.add(headersToAdd[x].first + add, headersToAdd[x].second)
  }

  return listWithHeaders
}

fun getMonthName(month: Int, context: Context): String {
  val monthArray = context.resources.getStringArray(R.array.months)
  return monthArray[month]
}