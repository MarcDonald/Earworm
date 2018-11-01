package app.marcdev.earworm.mainscreen

import app.marcdev.earworm.database.FavouriteItem
import app.marcdev.earworm.utils.ItemFilter

interface MainFragmentModel {

  fun getAllItemsAsync()

  fun getAllItemsAsync(filter: ItemFilter)

  fun deleteItemAsync(item: FavouriteItem)
}