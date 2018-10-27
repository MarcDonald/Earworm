package app.marcdev.earworm.mainscreen

import app.marcdev.earworm.database.FavouriteItem

interface MainFragmentPresenter {

  fun getAllItems()

  fun getAllItemsCallback(items: MutableList<FavouriteItem>)

  fun deleteItem(item: FavouriteItem)

  fun deleteItemCallback()
}