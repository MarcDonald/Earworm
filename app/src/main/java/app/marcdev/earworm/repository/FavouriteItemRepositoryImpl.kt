package app.marcdev.earworm.repository

import app.marcdev.earworm.database.DAO
import app.marcdev.earworm.database.FavouriteItem

class FavouriteItemRepositoryImpl(private val dao: DAO) : FavouriteItemRepository {

  override fun insertOrUpdateItem(item: FavouriteItem) {
    return dao.insertOrUpdateItem(item)
  }

  override fun getAllItems(): MutableList<FavouriteItem> {
    return dao.getAllItems()
  }

  override fun getItem(id: Int): MutableList<FavouriteItem> {
    return dao.getItemById(id)
  }

  override fun deleteItem(id: Int) {
    return dao.deleteItemById(id)
  }
}