package app.marcdev.earworm.data.repository

import androidx.lifecycle.LiveData
import app.marcdev.earworm.data.database.FavouriteItem

interface FavouriteItemRepository {

  val allItems: LiveData<List<FavouriteItem>>

  suspend fun addItem(item: FavouriteItem)

  suspend fun getItem(id: Int): MutableList<FavouriteItem>

  suspend fun deleteItem(id: Int)

  suspend fun countUsesOfImage(imageName: String): Int
}