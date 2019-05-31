package app.marcdev.earworm.mainscreen

import android.content.Context
import app.marcdev.earworm.data.database.FavouriteItem
import app.marcdev.earworm.utils.*
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
    } else {
      view.activateFilterIcon(false)
    }
    model.getAllItemsAsync(filter)
  }

  override fun getAllItemsCallback(items: MutableList<FavouriteItem>) {
    Timber.d("Log: getAllItemsCallback: Started")

    val sortedItems = sortByDateDescending(items)
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
    Timber.d("Log: getAllItemsCallback: Started with filter = $filter")

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
    if(item.imageName != "") {
      Timber.d("Log: deleteItem: Item has an image, checking if used elsewhere")
      model.countUsesOfImage(item)
    } else {
      Timber.d("Log: deleteItem: Item does not have an image, deleting item")
      model.deleteItemAsync(item)
    }
  }

  override fun deleteItemCallback() {
    Timber.d("Log: deleteItemCallback: Started")
    if(view.getActiveFilter() != DEFAULT_FILTER.copy()) {
      getAllItems(view.getActiveFilter())
    } else {
      getAllItems()
    }
  }

  override fun countUsesOfImageCallback(item: FavouriteItem, uses: Int) {
    Timber.d("Log: countUsesOfImageCallback: Started with imageName = ${item.imageName} and uses = $uses")

    if(uses <= 1) {
      val filePath = getArtworkDirectory(context) + item.imageName
      model.deleteImage(filePath)
    }

    model.deleteItemAsync(item)
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