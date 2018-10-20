package app.marcdev.earworm.mainscreen

import app.marcdev.earworm.database.FavouriteItem
import timber.log.Timber

class MainFragmentPresenterImpl(var view: MainFragmentView) : MainFragmentPresenter {
  private var model = MainFragmentModelImpl(this)

  override fun fabClick() {
    Timber.d("Log: fabClick: Started")
    model.addTestItem()
  }

  override fun fabLongClick() {
    Timber.d("Log: fabLongClick: Started")
    model.clearList()
  }

  override fun addTestItemCallback(items: MutableList<FavouriteItem>) {
    Timber.d("Log: addTestItemCallback: Started")
    view.updateRecycler(items)
    view.displayAddedToast()
  }

  override fun clearListCallback(items: MutableList<FavouriteItem>) {
    Timber.d("Log: clearListCallback: Started")
    view.updateRecycler(items)
    view.displayClearedToast()
  }
}