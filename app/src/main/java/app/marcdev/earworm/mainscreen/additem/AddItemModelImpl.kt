package app.marcdev.earworm.mainscreen.additem

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

class AddItemModelImpl(private val presenter: AddItemPresenter, context: Context) : AddItemModel {

  private var repository: FavouriteItemRepository

  init {
    val db: AppDatabase = AppDatabase.getDatabase(context)
    repository = FavouriteItemRepositoryImpl(db.dao())
  }

  override fun addItemAsync(item: FavouriteItem) {
    Timber.d("Log: addItemAsync: Started")

    GlobalScope.launch(Dispatchers.Main) {
      async(Dispatchers.IO) {
        repository.insertOrUpdateItem(item)
      }.await()

      presenter.addItemCallback()
    }
  }
}