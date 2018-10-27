package app.marcdev.earworm.mainscreen

import app.marcdev.earworm.database.FavouriteItem

interface MainFragmentPresenter {

  fun getAllItems()

  fun getAllItemsCallback(items: MutableList<FavouriteItem>)

  fun deleteItem(item: FavouriteItem)

  fun deleteItemCallback()

  fun editItemClick(itemId: Int)

  fun search(input: String)

  fun searchCallback(items: MutableList<FavouriteItem>)
}