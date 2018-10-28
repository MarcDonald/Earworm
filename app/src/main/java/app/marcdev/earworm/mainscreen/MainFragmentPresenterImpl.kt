package app.marcdev.earworm.mainscreen

import android.content.Context
import app.marcdev.earworm.ItemFilter
import app.marcdev.earworm.applyFilter
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
    model.getAllItemsAsync(filter)
  }

  override fun getAllItemsCallback(items: MutableList<FavouriteItem>) {
    Timber.d("Log: getAllItemsCallback: Started")

    val sortedItems = items.sortedWith(
      compareBy(
        { it.year },
        { it.month },
        { it.day }))

    view.updateRecycler(sortedItems)
    view.displayProgress(false)

    if(items.size == 0) {
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

    if(sortedItems.size == 0) {
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
    if(view.getActiveFilter() != ItemFilter(1, 0, 1900, 31, 12, 2099, true, true, true)) {
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
      view.displayEmptySearchToast()
    } else {
      model.searchAsync(input.trim())
    }
  }

  override fun searchCallback(items: MutableList<FavouriteItem>) {
    // TODO
  }
}