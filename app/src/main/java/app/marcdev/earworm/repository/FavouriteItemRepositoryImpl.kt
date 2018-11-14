package app.marcdev.earworm.repository

import app.marcdev.earworm.database.DAO
import app.marcdev.earworm.database.FavouriteItem

class FavouriteItemRepositoryImpl(private val dao: DAO) : FavouriteItemRepository {

  override suspend fun insertOrUpdateItem(item: FavouriteItem) {
    return dao.insertOrUpdateItem(item)
  }

  override suspend fun getAllItems(): MutableList<FavouriteItem> {
    return dao.getAllItems()
  }

  override suspend fun getItem(id: Int): MutableList<FavouriteItem> {
    return dao.getItemById(id)
  }

  override suspend fun deleteItem(id: Int) {
    return dao.deleteItemById(id)
  }

  override suspend fun countUsesOfImage(imageName: String): Int {
    return dao.getNumberOfEntriesUsingImage(imageName)
  }
}