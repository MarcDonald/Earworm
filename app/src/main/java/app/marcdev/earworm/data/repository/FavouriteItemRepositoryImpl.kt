package app.marcdev.earworm.data.repository

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import app.marcdev.earworm.data.database.DAO
import app.marcdev.earworm.data.database.FavouriteItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class FavouriteItemRepositoryImpl(private val dao: DAO) : FavouriteItemRepository {

  override val allItems: LiveData<List<FavouriteItem>> by lazy {
    dao.getAllItems()
  }

  override suspend fun addItem(item: FavouriteItem) {
    withContext(Dispatchers.IO) {
      try {
        Timber.d("Log: addItem: Item doesn't exist, adding new")
        dao.insertItem(item)
      } catch(exception: SQLiteConstraintException) {
        Timber.d("Log: addItem: Item already exists, updating existing")
        dao.updateItem(item)
      }
    }
  }


  override suspend fun getItem(id: Int): FavouriteItem {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getItemById(id)
    }
  }

  override suspend fun deleteItem(id: Int) {
    withContext(Dispatchers.IO) {
      dao.deleteItemById(id)
    }
  }

  override suspend fun countUsesOfImage(imageName: String): Int {
    return withContext(Dispatchers.IO) {
      return@withContext dao.getNumberOfEntriesUsingImage(imageName)
    }
  }

  companion object {
    @Volatile private var instance: FavouriteItemRepositoryImpl? = null

    fun getInstance(dao: DAO) =
      instance ?: synchronized(this) {
        instance ?: FavouriteItemRepositoryImpl(dao).also { instance = it }
      }
  }
}