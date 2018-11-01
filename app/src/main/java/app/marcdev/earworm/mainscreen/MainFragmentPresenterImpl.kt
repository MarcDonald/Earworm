package app.marcdev.earworm.mainscreen

import android.content.Context
import app.marcdev.earworm.*
import app.marcdev.earworm.database.FavouriteItem
import timber.log.Timber

class MainFragmentPresenterImpl(val view: MainFragmentView, val context: Context) : MainFragmentPresenter {
  private var model = MainFragmentModelImpl(this, context)

  override fun getAllItems() {
    Timber.d("Log: getAllItems: Started")
    model.getAllItemsAsync()
  }

  override fun getAllItems(filter: ItemFilter) {
    Timber.d("Log: getAllItems with Filter: Started")
    Timber.i("Log: getAllItems: Input Filter = $filter")
    if(filter != DEFAULT_FILTER.copy()) {
      view.activateFilterIcon(true)
      model.getAllItemsAsync(filter)
    } else {
      view.activateFilterIcon(false)
      getAllItems()
    }
  }

  override fun getAllItemsCallback(items: MutableList<FavouriteItem>) {
    Timber.d("Log: getAllItemsCallback: Started")

    val sortedItems = filterByDateDescending(items)
    val itemsWithHeaders = addListHeaders(sortedItems)

    view.updateRecycler(itemsWithHeaders)
    view.displayProgress(false)

    if(itemsWithHeaders.isEmpty()) {
      view.displayNoEntriesWarning(true)
    } else {
      view.displayNoEntriesWarning(false)
    }
  }

  override fun getAllItemsCallback(items: MutableList<FavouriteItem>, filter: ItemFilter) {
    Timber.d("Log: getAllItemsCallback: Started")

    val sortedItems = applyFilter(items, filter)
    val itemsWithHeaders = addListHeaders(sortedItems)

    view.updateRecycler(itemsWithHeaders)
    view.displayProgress(false)

    if(itemsWithHeaders.isEmpty()) {
      view.displayNoFilteredResultsWarning(true)
    } else {
      view.displayNoFilteredResultsWarning(false)
    }
  }

  override fun deleteItem(item: FavouriteItem) {
    Timber.d("Log: deleteItem: Started")
    model.deleteItemAsync(item)
  }

  override fun deleteItemCallback() {
    Timber.d("Log: deleteItemCallback: Started")
    if(view.getActiveFilter() != DEFAULT_FILTER.copy()) {
      getAllItems(view.getActiveFilter())
    } else {
      getAllItems()
    }
  }

  override fun editItemClick(itemId: Int) {
    Timber.d("Log: editItemClick: Started")
    view.displayEditItemSheet(itemId)
  }

  override fun search(input: String) {
    Timber.d("Log: search: Started with input = $input")
    if(input.isBlank()) {
      val inputFilter = view.getActiveFilter()
      inputFilter.searchTerm = ""
      model.getAllItemsAsync(inputFilter)
      view.changeSearchIcon(true)
    } else {
      val inputFilter = view.getActiveFilter()
      inputFilter.searchTerm = input.trim()
      model.getAllItemsAsync(inputFilter)
      view.changeSearchIcon(false)
    }
  }
}