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
    view.updateRecycler(items)
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
}