package app.marcdev.earworm.mainscreen

import android.content.Context
import app.marcdev.earworm.database.AppDatabase
import app.marcdev.earworm.database.FavouriteItem
import app.marcdev.earworm.repository.FavouriteItemRepository
import app.marcdev.earworm.repository.FavouriteItemRepositoryImpl
import app.marcdev.earworm.utils.ItemFilter
import app.marcdev.earworm.utils.getArtworkDirectory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File

class MainFragmentModelImpl(private val presenter: MainFragmentPresenter, private val context: Context) : MainFragmentModel {

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

  override fun countUsesOfImage(item: FavouriteItem) {
    Timber.d("Log: countUsesOfImage: Started with item = $item")
    val filePath = getArtworkDirectory(context) + item.imageName
    val file = File(filePath)
    val fileName = file.name

    GlobalScope.launch(Dispatchers.Main) {
      val uses = async(Dispatchers.IO) {
        repository.countUsesOfImage(fileName)
      }.await()

      presenter.countUsesOfImageCallback(item, uses)
    }
  }

  override fun deleteImage(filePath: String) {
    Timber.d("Log: deleteImage: Started with filePath = $filePath")

    val file = File(filePath)
    if(file.exists()) {
      Timber.d("Log: deleteImage: File exists, deleting")
      val deletionStatus = file.delete()
      Timber.d("Log: deleteImage: Deletion: $deletionStatus")
    } else {
      Timber.w("Log: deleteImage: File doesn't exist")
    }
  }
}