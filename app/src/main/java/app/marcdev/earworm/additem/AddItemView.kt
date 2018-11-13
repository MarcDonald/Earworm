package app.marcdev.earworm.additem

import app.marcdev.earworm.database.FavouriteItem

interface AddItemView {

  fun saveCallback()

  fun displayEmptyToast()

  fun convertToEditMode(item: FavouriteItem)

  fun displayErrorToast()

  fun displayImage(imagePath: String)
}