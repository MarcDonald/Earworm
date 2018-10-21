package app.marcdev.earworm.mainscreen

import android.content.Context
import app.marcdev.earworm.EarwormUtils
import app.marcdev.earworm.database.FavouriteItem
import timber.log.Timber

class MainFragmentPresenterImpl(val view: MainFragmentView, val context: Context) : MainFragmentPresenter {
  private var model = MainFragmentModelImpl(this, context)

  override fun fabClick() {
    Timber.d("Log: fabClick: Started")

    // TODO adds a test item, this will be removed in the future
    val item = FavouriteItem("Test Song", "Test Album", "Test Artist", "Test Genre", "01-10-2018", EarwormUtils.SONG)

    model.addItemAsync(item)
  }

  override fun fabLongClick() {
    Timber.d("Log: fabLongClick: Started")
    model.clearListAsync()
  }

  override fun addTestItemCallback() {
    Timber.d("Log: addTestItemCallback: Started")
    view.displayAddedToast()
    model.getAllItemsAsync()
  }

  override fun getAllItems() {
    Timber.d("Log: getAllItems: Started")
    model.getAllItemsAsync()
  }

  override fun getAllItemsCallback(items: MutableList<FavouriteItem>) {
    Timber.d("Log: getAllItemsCallback: Started")
    view.updateRecycler(items)

    if(items.size == 0) {
      view.displayNoEntriesWarning(true)
    } else
      view.displayNoEntriesWarning(false)
  }

  override fun clearListCallback() {
    Timber.d("Log: clearListCallback: Started")
    view.displayClearedToast()
    model.getAllItemsAsync()
  }
}