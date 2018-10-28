package app.marcdev.earworm.mainscreen

import app.marcdev.earworm.ItemFilter
import app.marcdev.earworm.database.FavouriteItem

interface MainFragmentPresenter {

  fun getAllItems()

  fun getAllItems(filter: ItemFilter)

  fun getAllItemsCallback(items: MutableList<FavouriteItem>)

  fun getAllItemsCallback(items: MutableList<FavouriteItem>, filter: ItemFilter)

  fun deleteItem(item: FavouriteItem)

  fun deleteItemCallback()

  fun editItemClick(itemId: Int)

  fun search(input: String)
}