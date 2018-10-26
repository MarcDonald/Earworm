package app.marcdev.earworm.mainscreen.mainrecycler

import android.content.Context
import app.marcdev.earworm.database.FavouriteItem
import timber.log.Timber

class MainRecyclerPresenterImpl(private val view: MainRecyclerView, context: Context) : MainRecyclerPresenter {
  private val model: MainRecyclerModel = MainRecyclerModelImpl(this, context)

  override fun deleteItem(item: FavouriteItem) {
    Timber.d("Log: deleteItem: Started")
    model.deleteItemAsync(item)
    // TODO display delete progress
  }

  override fun deleteItemCallback(items: MutableList<FavouriteItem>) {
    Timber.d("Log: deleteItemCallback: Started")
    view.updateItems(items)
    view.displayItemDeletedToast()
  }

  override fun updateItem(item: FavouriteItem) {
    TODO("not implemented")
  }
}