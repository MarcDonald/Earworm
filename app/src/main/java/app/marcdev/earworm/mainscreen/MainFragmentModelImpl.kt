package app.marcdev.earworm.mainscreen

import android.content.Context
import app.marcdev.earworm.ItemFilter
import app.marcdev.earworm.database.AppDatabase
import app.marcdev.earworm.database.FavouriteItem
import app.marcdev.earworm.repository.FavouriteItemRepository
import app.marcdev.earworm.repository.FavouriteItemRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber

class MainFragmentModelImpl(private val presenter: MainFragmentPresenter, context: Context) : MainFragmentModel {

  private var repository: FavouriteItemRepository

  init {
    val db: AppDatabase = AppDatabase.getDatabase(context)
    repository = FavouriteItemRepositoryImpl(db.dao())
  }

  override fun getAllItemsAsync() {
    Timber.d("Log: getAllItemsAsync: Started")

    GlobalScope.launch(Dispatchers.Main) {
      val allItems = async(Dispatchers.IO) {
        repository.getAllItems()
      }.await()

      presenter.getAllItemsCallback(allItems)
    }
  }

  override fun getAllItemsAsync(filter: ItemFilter) {
    Timber.d("Log: getAllItemsAsync: Started")

    GlobalScope.launch(Dispatchers.Main) {
      val allItems = async(Dispatchers.IO) {
        repository.getAllItems()
      }.await()

      presenter.getAllItemsCallback(allItems, filter)
    }
  }

  override fun deleteItemAsync(item: FavouriteItem) {
    Timber.d("Log: deleteItemAsync: Started")

    GlobalScope.launch(Dispatchers.Main) {
      async(Dispatchers.IO) {
        repository.deleteItem(item.id!!)
      }.await()

      presenter.deleteItemCallback()
    }
  }
}