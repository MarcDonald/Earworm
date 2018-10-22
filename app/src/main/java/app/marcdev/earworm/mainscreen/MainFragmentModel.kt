package app.marcdev.earworm.mainscreen

import app.marcdev.earworm.database.FavouriteItem

interface MainFragmentModel {
  fun addItemAsync(item: FavouriteItem)

  fun getAllItemsAsync()

  fun clearListAsync()
}