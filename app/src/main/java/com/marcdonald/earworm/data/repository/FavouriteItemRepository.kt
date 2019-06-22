package com.marcdonald.earworm.data.repository

import androidx.lifecycle.LiveData
import com.marcdonald.earworm.data.database.FavouriteItem

interface FavouriteItemRepository {

  val allItems: LiveData<List<FavouriteItem>>

  suspend fun addItem(item: FavouriteItem)

  suspend fun getItem(id: Int): FavouriteItem

  suspend fun deleteItem(id: Int)

  suspend fun countUsesOfImage(imageName: String): Int
}