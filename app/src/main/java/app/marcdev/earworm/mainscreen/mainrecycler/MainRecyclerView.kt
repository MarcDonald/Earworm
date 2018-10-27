package app.marcdev.earworm.mainscreen.mainrecycler

import app.marcdev.earworm.database.FavouriteItem

interface MainRecyclerView {
  fun updateItems(items: List<FavouriteItem>)
}