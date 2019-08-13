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

package com.marcdonald.earworm.mainscreen

import androidx.lifecycle.*
import com.marcdonald.earworm.data.database.FavouriteItem
import com.marcdonald.earworm.data.repository.FavouriteItemRepository
import com.marcdonald.earworm.internal.*
import com.marcdonald.earworm.utils.FileUtils
import com.marcdonald.earworm.utils.ItemFilter
import kotlinx.coroutines.launch

class MainFragmentViewModel(private val repository: FavouriteItemRepository,
														private val fileUtils: FileUtils)
	: ViewModel() {

	private val activeFilter = MutableLiveData<ItemFilter>()
	private val allItems = repository.allItems

	private val _displayData = MediatorLiveData<List<FavouriteItem>>()
	val displayData: LiveData<List<FavouriteItem>>
		get() = _displayData

	private val _displayLoading = MutableLiveData<Boolean>()
	val displayLoading: LiveData<Boolean>
		get() = _displayLoading

	private val _displayNoEntries = MutableLiveData<Boolean>()
	val displayNoEntries: LiveData<Boolean>
		get() = _displayNoEntries

	private val _displayNoFilteredResults = MutableLiveData<Boolean>()
	val displayNoFilteredResults: LiveData<Boolean>
		get() = _displayNoFilteredResults

	private val _colorFilterIcon = MutableLiveData<Boolean>()
	val colorFilterIcon: LiveData<Boolean>
		get() = _colorFilterIcon

	private val _displaySearchIcon = MutableLiveData<Boolean>()
	val displaySearchIcon: LiveData<Boolean>
		get() = _displaySearchIcon

	init {
		_displayNoEntries.value = false
		_displayNoFilteredResults.value = false
		_displayData.addSource(activeFilter) { filter ->
			workOutDisplayData(allItems.value, filter)
		}
		_displayData.addSource(allItems) { items ->
			workOutDisplayData(items, activeFilter.value)
		}
	}

	private fun workOutDisplayData(items: List<FavouriteItem>?, filter: ItemFilter?) {
		_displayLoading.value = true
		displaySearchIconIfNeeded(filter)
		var finalList: List<FavouriteItem>? = null

		items?.let { fullList ->
			val filteredList = if(filter != null) {
				filterResults(fullList, filter)
			} else {
				fullList
			}
			finalList = addListHeaders(filteredList)
		}

		_displayData.value = finalList?.toList()
		if(activeFilter.value == DEFAULT_FILTER || activeFilter.value == null) {
			_displayNoFilteredResults.value = false
			_displayNoEntries.value = finalList?.isEmpty()
		} else {
			_displayNoEntries.value = false
			_displayNoFilteredResults.value = finalList?.isEmpty()
		}
		_displayLoading.value = false
	}

	fun deleteItem(item: FavouriteItem) {
		viewModelScope.launch {
			repository.deleteItem(item.id)
			deleteImageIfNecessary(item.imageName)
		}
	}

	fun search(searchTermArg: String) {
		val newFilter: ItemFilter? = if(activeFilter.value == null)
			DEFAULT_FILTER.copy()
		else
			activeFilter.value

		newFilter?.searchTerm = searchTermArg
		activeFilter.value = newFilter
	}

	fun filter(filter: ItemFilter) {
		val newFilter: ItemFilter? = if(activeFilter.value == null)
			DEFAULT_FILTER.copy()
		else
			activeFilter.value
		if(newFilter != null) {
			newFilter.endDay = filter.endDay
			newFilter.endMonth = filter.endMonth
			newFilter.endYear = filter.endYear
			newFilter.startDay = filter.startDay
			newFilter.startMonth = filter.startMonth
			newFilter.startYear = filter.startYear
			newFilter.includeAlbums = filter.includeAlbums
			newFilter.includeArtists = filter.includeArtists
			newFilter.includeSongs = filter.includeSongs
		}
		activeFilter.value = newFilter
	}

	private suspend fun deleteImageIfNecessary(imageName: String) {
		if(imageName.isNotBlank()) {
			val uses = repository.countUsesOfImage(imageName)
			if(uses == 0) {
				fileUtils.deleteImage(imageName)
			}
		}
	}

	private fun displaySearchIconIfNeeded(filter: ItemFilter?) {
		if(filter == null) {
			_displaySearchIcon.value = true
		} else {
			_displaySearchIcon.value = filter.searchTerm.isBlank()
		}
	}

	private fun filterResults(allItems: List<FavouriteItem>, filter: ItemFilter): List<FavouriteItem> {
		val filteredByDate = filterByDate(allItems, filter)
		val filteredByType = filterByType(filteredByDate, filter)
		val filteredByText = filterByText(filteredByType, filter)

		colorFilterIconIfNecessary(filter)
		return filteredByText
	}

	private fun filterByDate(items: List<FavouriteItem>, filter: ItemFilter): List<FavouriteItem> {
		val filteredItems = mutableListOf<FavouriteItem>()

		val filterCompleteDateStart = getCompleteDate(filter.startDay, filter.startMonth, filter.startYear)
		val filterCompleteDateEnd: Int = getCompleteDate(filter.endDay, filter.endMonth, filter.endYear)

		for(item in items) {
			val itemCompleteDate = getCompleteDate(item.day, item.month, item.year)
			if(itemCompleteDate in filterCompleteDateStart..filterCompleteDateEnd) {
				filteredItems.add(item)
			}
		}

		return filteredItems
	}

	private fun filterByType(items: List<FavouriteItem>, filter: ItemFilter): List<FavouriteItem> {
		val filteredItems = mutableListOf<FavouriteItem>()

		for(item in items) {
			if(item.type == SONG && filter.includeSongs)
				filteredItems.add(item)

			if(item.type == ALBUM && filter.includeAlbums)
				filteredItems.add(item)

			if(item.type == ARTIST && filter.includeArtists)
				filteredItems.add(item)
		}

		return filteredItems
	}

	private fun filterByText(items: List<FavouriteItem>, filter: ItemFilter): List<FavouriteItem> {
		val filteredItems = mutableListOf<FavouriteItem>()

		for(item in items) {
			if((item.albumName.contains(filter.searchTerm, true)
					|| (item.artistName.contains(filter.searchTerm, true)
							|| (item.songName.contains(filter.searchTerm, true)
									|| (item.genre.contains(filter.searchTerm, true)))))
			) {
				filteredItems.add(item)
			}
		}
		return filteredItems
	}

	private fun getCompleteDate(day: Int, month: Int, year: Int): Int {
		val dayTwoDigit = if(day < 10)
			"0$day"
		else
			"$day"

		val monthTwoDigit = if(month < 10)
			"0$month"
		else
			"$month"

		return "$year$monthTwoDigit$dayTwoDigit".toInt()
	}

	private fun addListHeaders(items: List<FavouriteItem>): List<FavouriteItem> {
		val listWithHeaders = items.toMutableList()

		var lastMonth = 12
		var lastYear = 9999
		if(items.isNotEmpty()) {
			lastMonth = items.first().month + 1
			lastYear = items.first().year
		}

		val headersToAdd = mutableListOf<Pair<Int, FavouriteItem>>()

		for(x in 0 until items.size) {
			if(((items[x].month < lastMonth) && (items[x].year == lastYear))
				 || (items[x].month > lastMonth) && (items[x].year < lastYear)
				 || (items[x].year < lastYear)
			) {
				val header = FavouriteItem("", "", "", "", 0, items[x].month, items[x].year, HEADER, "")
				lastMonth = items[x].month
				lastYear = items[x].year
				headersToAdd.add(Pair(x, header))
			}
		}

		for((add, x) in (0 until headersToAdd.size).withIndex()) {
			listWithHeaders.add(headersToAdd[x].first + add, headersToAdd[x].second)
		}

		return listWithHeaders
	}

	private fun colorFilterIconIfNecessary(filter: ItemFilter) {
		val isDefaultFilter = filter.startDay == DEFAULT_FILTER.startDay
													&& filter.startMonth == DEFAULT_FILTER.startMonth
													&& filter.startYear == DEFAULT_FILTER.startYear
													&& filter.endDay == DEFAULT_FILTER.endDay
													&& filter.endMonth == DEFAULT_FILTER.endMonth
													&& filter.endYear == DEFAULT_FILTER.endYear
													&& filter.includeSongs == DEFAULT_FILTER.includeSongs
													&& filter.includeAlbums == DEFAULT_FILTER.includeAlbums
													&& filter.includeArtists == DEFAULT_FILTER.includeArtists
		_colorFilterIcon.value = !isDefaultFilter
	}
}