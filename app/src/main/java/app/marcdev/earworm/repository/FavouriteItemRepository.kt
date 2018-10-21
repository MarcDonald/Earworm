package app.marcdev.earworm.repository

import app.marcdev.earworm.database.FavouriteItem

interface FavouriteItemRepository {

  fun insertOrUpdateItem(item: FavouriteItem)

  fun getAllItems(): MutableList<FavouriteItem>

  fun getItem(id: Int): MutableList<FavouriteItem>

  fun deleteItem(id: Int)
}