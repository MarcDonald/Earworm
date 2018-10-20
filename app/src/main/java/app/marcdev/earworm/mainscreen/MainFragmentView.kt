package app.marcdev.earworm.mainscreen

import app.marcdev.earworm.database.FavouriteItem

interface MainFragmentView {
  fun displayAddedToast()

  fun displayClearedToast()

  fun updateRecycler(items: MutableList<FavouriteItem>)
}