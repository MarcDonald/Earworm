package app.marcdev.earworm.mainscreen

import android.content.Context
import app.marcdev.earworm.EarwormUtils
import app.marcdev.earworm.database.AppDatabase
import app.marcdev.earworm.database.FavouriteItem
import app.marcdev.earworm.repository.FavouriteItemRepository
import app.marcdev.earworm.repository.FavouriteItemRepositoryImpl
import kotlinx.coroutines.*
import timber.log.Timber

class MainFragmentModelImpl(private val presenter: MainFragmentPresenter, context: Context) : MainFragmentModel {

  private var repository: FavouriteItemRepository

  init {
    val db: AppDatabase = AppDatabase.getDatabase(context)
    repository = FavouriteItemRepositoryImpl(db.dao())
  }

  override fun addItemAsync() {
    Timber.d("Log: addItemAsync: Started")

    // TODO adds a test item, this will be removed in the future
    val item = FavouriteItem("Test Song", "Test Album", "Test Artist", "Test Genre", "01-10-2018", EarwormUtils.SONG)

    // Launches a co-routine that won't block the UI thread
    GlobalScope.launch(Dispatchers.Main) {
      // Performs the insert operation on the IO dispatcher
      async(Dispatchers.IO) {
        repository.insertOrUpdateItem(item)
      }.await()

      // After the insert is complete, perform the callback to presenter
      presenter.addTestItemCallback()
    }
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

  // TODO this is just for testing, will be removed in the future
  override fun clearListAsync() {
    Timber.d("Log: clearListAsync: Started")

    GlobalScope.launch(Dispatchers.Main) {
      withContext(Dispatchers.IO) {
        repository.getAllItems().forEach {
          val id: Int = it.id ?: 1
          repository.deleteItem(id)
        }
      }
      presenter.clearListCallback()
    }
  }
}