package app.marcdev.earworm.mainscreen

import app.marcdev.earworm.EarwormUtils
import app.marcdev.earworm.database.FavouriteItem
import timber.log.Timber

class MainFragmentModelImpl(var presenter: MainFragmentPresenter) : MainFragmentModel {
  private var items: MutableList<FavouriteItem> = mutableListOf()

  override fun addTestItem() {
    Timber.d("Log: addTestItem: Started")
    val numItems = items.size

    val item: FavouriteItem = when (numItems) {
      2 -> FavouriteItem("", "Test Album $numItems", "Test Artist $numItems", "", "$numItems-10-2018", EarwormUtils.ALBUM)
      5 -> FavouriteItem("", "", "Test Artist $numItems", "Test Genre $numItems", "$numItems-10-2018", EarwormUtils.ARTIST)
      7 -> FavouriteItem("", "", "", "Test Genre $numItems", "$numItems-10-2018", EarwormUtils.GENRE)
      else -> FavouriteItem("Test Song $numItems", "Test Album $numItems", "Test Artist $numItems", "Test Genre $numItems", "$numItems-10-2018", EarwormUtils.SONG)
    }

    items.add(item)

    presenter.addTestItemCallback(items)
  }

  override fun clearList() {
    Timber.d("Log: clearList: Started")
    items.clear()
    presenter.clearListCallback(items)
  }
}