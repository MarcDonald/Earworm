package app.marcdev.earworm.utils

import app.marcdev.earworm.database.FavouriteItem
import timber.log.Timber

/**
 * Filters a list based on a filter input by the user
 * @param allItems The complete list of items from the database
 * @param filter The filter to apply
 * @return Filtered list
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

  return sortByDateDescending(filteredItems)
}

/**
 * Sorts a list by date in descending order
 * @param items List to sort
 * @return Sorted list of FavouriteItems
 */
fun sortByDateDescending(items: MutableList<FavouriteItem>): MutableList<FavouriteItem> {
  val filteredItems = items.sortedWith(
    compareBy(
      { -it.year },
      { -it.month },
      { -it.day }))

  return filteredItems.toMutableList()
}

/**
 * Adds header items to a list on a monthly basis
 * @param allItems List to add headers to (sorted by date descending)
 * @return List with headers every new month
 */
fun addListHeaders(allItems: MutableList<FavouriteItem>): List<FavouriteItem> {
  val listWithHeaders = mutableListOf<FavouriteItem>()
  listWithHeaders.addAll(allItems)

  var lastMonth = 12
  var lastYear = 9999
  if(allItems.isNotEmpty()) {
    lastMonth = allItems.first().month + 1
    lastYear = allItems.first().year
  }

  val headersToAdd = mutableListOf<Pair<Int, FavouriteItem>>()

  for(x in 0 until allItems.size) {
    if(((allItems[x].month < lastMonth) && (allItems[x].year == lastYear))
       || (allItems[x].month > lastMonth) && (allItems[x].year < lastYear)
       || (allItems[x].year < lastYear)
    ) {
      Timber.v("Log: addListHeaders: x = $x")
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
