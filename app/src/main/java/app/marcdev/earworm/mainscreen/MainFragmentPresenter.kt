package app.marcdev.earworm.mainscreen

import app.marcdev.earworm.database.FavouriteItem

interface MainFragmentPresenter {
  fun fabClick()

  fun addTestItemCallback(items: MutableList<FavouriteItem>)

  fun fabLongClick()

  fun clearListCallback(items: MutableList<FavouriteItem>)
}