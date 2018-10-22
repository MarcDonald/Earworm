package app.marcdev.earworm.repository

import app.marcdev.earworm.database.FavouriteItem

interface FavouriteItemRepository {

  suspend fun insertOrUpdateItem(item: FavouriteItem)

  suspend fun getAllItems(): MutableList<FavouriteItem>

  suspend fun getItem(id: Int): MutableList<FavouriteItem>

  suspend fun deleteItem(id: Int)
}