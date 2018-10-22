package app.marcdev.earworm.mainscreen

import android.content.Context
import app.marcdev.earworm.database.FavouriteItem
import timber.log.Timber

class MainFragmentPresenterImpl(val view: MainFragmentView, val context: Context) : MainFragmentPresenter {
  private var model = MainFragmentModelImpl(this, context)

  override fun fabLongClick() {
    Timber.d("Log: fabLongClick: Started")
    model.clearListAsync()
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