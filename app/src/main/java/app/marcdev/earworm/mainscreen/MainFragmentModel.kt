package app.marcdev.earworm.mainscreen

import app.marcdev.earworm.data.database.FavouriteItem
import app.marcdev.earworm.utils.ItemFilter

interface MainFragmentModel {

  fun getAllItemsAsync()

  fun getAllItemsAsync(filter: ItemFilter)

  fun deleteItemAsync(item: FavouriteItem)

  fun countUsesOfImage(item: FavouriteItem)

  fun deleteImage(filePath: String)
}