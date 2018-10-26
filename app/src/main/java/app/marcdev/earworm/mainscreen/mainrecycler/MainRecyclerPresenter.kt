package app.marcdev.earworm.mainscreen.mainrecycler

import app.marcdev.earworm.database.FavouriteItem

interface MainRecyclerPresenter {
  fun deleteItem(item: FavouriteItem)

  fun deleteItemCallback(items: MutableList<FavouriteItem>)

  fun updateItem(item: FavouriteItem)
}