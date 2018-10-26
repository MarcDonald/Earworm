package app.marcdev.earworm.mainscreen.mainrecycler

import android.content.Context
import app.marcdev.earworm.database.AppDatabase
import app.marcdev.earworm.database.FavouriteItem
import app.marcdev.earworm.repository.FavouriteItemRepository
import app.marcdev.earworm.repository.FavouriteItemRepositoryImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber

class MainRecyclerModelImpl(private val presenter: MainRecyclerPresenter, context: Context) : MainRecyclerModel {

  private var repository: FavouriteItemRepository

  init {
    val db: AppDatabase = AppDatabase.getDatabase(context)
    repository = FavouriteItemRepositoryImpl(db.dao())
  }

  override fun deleteItemAsync(item: FavouriteItem) {
    Timber.d("Log: deleteItemAsync: Started")

    GlobalScope.launch(Dispatchers.Main) {
      async(Dispatchers.IO) {
        repository.deleteItem(item.id!!)
      }.await()

      // After deleting, will get the updated list of items and return them to the presenter
      getAllItemsAsync()
    }
  }

  private fun getAllItemsAsync() {
    Timber.d("Log: getAllItemsAsync: Started")

    GlobalScope.launch(Dispatchers.Main) {
      val allItems = async(Dispatchers.IO) {
        repository.getAllItems()
      }.await()

      presenter.deleteItemCallback(allItems)
    }
  }
}