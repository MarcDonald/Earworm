package app.marcdev.earworm.mainscreen

import android.content.Context
import app.marcdev.earworm.DEFAULT_FILTER
import app.marcdev.earworm.ItemFilter
import app.marcdev.earworm.applyFilter
import app.marcdev.earworm.database.FavouriteItem
import app.marcdev.earworm.filterByDateDescending
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

    view.updateRecycler(sortedItems)
    view.displayProgress(false)

    if(sortedItems.isEmpty()) {
      view.displayNoEntriesWarning(true)
    } else {
      view.displayNoEntriesWarning(false)
    }
  }

  override fun getAllItemsCallback(items: MutableList<FavouriteItem>, filter: ItemFilter) {
    Timber.d("Log: getAllItemsCallback: Started")

    val sortedItems = applyFilter(items, filter)

    view.updateRecycler(sortedItems)
    view.displayProgress(false)

    if(sortedItems.isEmpty()) {
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