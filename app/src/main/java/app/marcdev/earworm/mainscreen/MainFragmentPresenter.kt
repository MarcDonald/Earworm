package app.marcdev.earworm.mainscreen

import app.marcdev.earworm.database.FavouriteItem

interface MainFragmentPresenter {

  fun fabLongClick()

  fun getAllItems()

  fun getAllItemsCallback(items: MutableList<FavouriteItem>)

  fun clearListCallback()
}