package app.marcdev.earworm.mainscreen

import android.content.Context
import app.marcdev.earworm.data.database.FavouriteItem
import app.marcdev.earworm.utils.*
import timber.log.Timber

class MainFragmentPresenterImpl(val context: Context) {

  fun getAllItems(filter: ItemFilter) {
//    Timber.d("Log: getAllItems with Filter: Started")
//    Timber.i("Log: getAllItems: Input Filter = $filter")
//    if(filter != DEFAULT_FILTER.copy()) {
//      view.activateFilterIcon(true)
//    } else {
//      view.activateFilterIcon(false)
//    }
//    model.getAllItemsAsync(filter)
  }

  fun getAllItemsCallback(items: MutableList<FavouriteItem>) {
    Timber.d("Log: getAllItemsCallback: Started")

//    val sortedItems = sortByDateDescending(items)
//    val itemsWithHeaders = addListHeaders(sortedItems)
//
//    view.updateRecycler(itemsWithHeaders)
//    view.displayProgress(false)
//
//    if(itemsWithHeaders.isEmpty()) {
//      view.displayNoEntriesWarning(true)
//    } else {
//      view.displayNoEntriesWarning(false)
//    }
  }

  fun getAllItemsCallback(items: MutableList<FavouriteItem>, filter: ItemFilter) {
//    Timber.d("Log: getAllItemsCallback: Started with filter = $filter")
//
//    val sortedItems = applyFilter(items, filter)
//    val itemsWithHeaders = addListHeaders(sortedItems)
//
//    view.updateRecycler(itemsWithHeaders)
//    view.displayProgress(false)
//
//    if(itemsWithHeaders.isEmpty()) {
//      view.displayNoFilteredResultsWarning(true)
//    } else {
//      view.displayNoFilteredResultsWarning(false)
//    }
  }

  fun editItemClick(itemId: Int) {
//    Timber.d("Log: editItemClick: Started")
//    view.editClick(itemId)
  }

  fun search(input: String) {
//    Timber.d("Log: search: Started with input = $input")
//    if(input.isBlank()) {
//      val inputFilter = view.getActiveFilter()
//      inputFilter.searchTerm = ""
//      model.getAllItemsAsync(inputFilter)
//      view.changeSearchIcon(true)
//    } else {
//      val inputFilter = view.getActiveFilter()
//      inputFilter.searchTerm = input.trim()
//      model.getAllItemsAsync(inputFilter)
//      view.changeSearchIcon(false)
//  }
  }
}