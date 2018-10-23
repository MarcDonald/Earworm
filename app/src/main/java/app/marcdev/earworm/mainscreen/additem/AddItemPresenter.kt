package app.marcdev.earworm.mainscreen.additem

import java.util.*

interface AddItemPresenter {

  fun addItem(primaryInput: String, secondaryInput: String, type: Int, dateChosen: Calendar)

  fun addItemCallback()
}