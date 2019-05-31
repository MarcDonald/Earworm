package app.marcdev.earworm.data.repository

import app.marcdev.earworm.data.database.FavouriteItem

interface FavouriteItemRepository {

  suspend fun addItem(item: FavouriteItem)

  suspend fun getAllItems(): MutableList<FavouriteItem>

  suspend fun getItem(id: Int): MutableList<FavouriteItem>

  suspend fun deleteItem(id: Int)

  suspend fun countUsesOfImage(imageName: String): Int
}