package app.marcdev.earworm.mainscreen

import app.marcdev.earworm.database.FavouriteItem

interface MainFragmentView {

  fun displayNoEntriesWarning(display: Boolean)

  fun displayAddedToast()

  fun displayClearedToast()

  fun updateRecycler(items: MutableList<FavouriteItem>)
}