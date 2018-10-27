package app.marcdev.earworm.mainscreen

import app.marcdev.earworm.database.FavouriteItem

interface MainFragmentModel {

  fun getAllItemsAsync()

  fun deleteItemAsync(item: FavouriteItem)

  fun searchAsync(input: String)
}