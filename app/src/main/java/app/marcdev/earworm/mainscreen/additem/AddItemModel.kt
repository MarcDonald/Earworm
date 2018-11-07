package app.marcdev.earworm.mainscreen.additem

import app.marcdev.earworm.database.FavouriteItem
import java.io.File

interface AddItemModel {

  fun addItemAsync(item: FavouriteItem)

  fun getItemAsync(itemId: Int)

  fun saveFileToAppStorage(file: File)

  fun countUsesOfImage(filePath: String)

  fun deleteImage(filePath: String)
}