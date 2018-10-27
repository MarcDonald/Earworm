package app.marcdev.earworm.mainscreen

import android.content.Context
import app.marcdev.earworm.database.FavouriteItem
import timber.log.Timber

class MainFragmentPresenterImpl(val view: MainFragmentView, val context: Context) : MainFragmentPresenter {
  private var model = MainFragmentModelImpl(this, context)

  override fun getAllItems() {
    Timber.d("Log: getAllItems: Started")
    model.getAllItemsAsync()
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
    } else
      view.displayNoEntriesWarning(false)
  }

  override fun deleteItem(item: FavouriteItem) {
    Timber.d("Log: deleteItem: Started")
    model.deleteItemAsync(item)
  }

  override fun deleteItemCallback() {
    Timber.d("Log: deleteItemCallback: Started")
    getAllItems()
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