package app.marcdev.earworm.mainscreen

import app.marcdev.earworm.ItemFilter
import app.marcdev.earworm.database.FavouriteItem

interface MainFragmentModel {

  fun getAllItemsAsync()

  fun getAllItemsAsync(filter: ItemFilter)

  fun deleteItemAsync(item: FavouriteItem)
}