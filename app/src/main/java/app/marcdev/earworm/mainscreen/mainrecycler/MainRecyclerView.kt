package app.marcdev.earworm.mainscreen.mainrecycler

import app.marcdev.earworm.data.database.FavouriteItem

interface MainRecyclerView {
  fun updateItems(items: List<FavouriteItem>)
}