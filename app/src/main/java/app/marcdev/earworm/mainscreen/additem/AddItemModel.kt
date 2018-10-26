package app.marcdev.earworm.mainscreen.additem

import app.marcdev.earworm.database.FavouriteItem

interface AddItemModel {

  fun addItemAsync(item: FavouriteItem)
}