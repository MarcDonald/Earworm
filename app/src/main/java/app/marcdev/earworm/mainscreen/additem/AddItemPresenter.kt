package app.marcdev.earworm.mainscreen.additem

import app.marcdev.earworm.database.FavouriteItem
import java.util.*

interface AddItemPresenter {

  fun addItem(primaryInput: String, secondaryInput: String, type: Int, dateChosen: Calendar, itemId: Int?, imageUri: String)

  fun addItemCallback()

  fun getItem(itemId: Int)

  fun getItemCallback(items: MutableList<FavouriteItem>)
}