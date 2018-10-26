package app.marcdev.earworm.mainscreen.mainrecycler

import app.marcdev.earworm.database.FavouriteItem

interface MainRecyclerModel {
  fun deleteItemAsync(item: FavouriteItem)
}