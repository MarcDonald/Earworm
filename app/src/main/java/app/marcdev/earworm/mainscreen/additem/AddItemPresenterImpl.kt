package app.marcdev.earworm.mainscreen.additem

import android.content.Context
import app.marcdev.earworm.database.FavouriteItem
import app.marcdev.earworm.utils.ALBUM
import app.marcdev.earworm.utils.ARTIST
import app.marcdev.earworm.utils.SONG
import app.marcdev.earworm.utils.getArtworkDirectory
import timber.log.Timber
import java.io.File
import java.util.*

class AddItemPresenterImpl(private val view: AddItemView, private val context: Context) : AddItemPresenter {

  private val model: AddItemModel
  var imageFilePath: String = ""
  var oldImageFilePath: String = ""

  init {
    model = AddItemModelImpl(this, context)
  }

  override fun addItem(primaryInput: String, secondaryInput: String, type: Int, dateChosen: Calendar, itemId: Int?) {
    Timber.d("Log: addItem: Started")

    if(primaryInput.isBlank() || secondaryInput.isBlank()) {
      Timber.d("Log: addItem: Empty input")
      view.displayEmptyToast()
    } else {
      Timber.d("Log: addItem: Adding item")
      val day = dateChosen.get(Calendar.DAY_OF_MONTH)
      val month = dateChosen.get(Calendar.MONTH)
      val year = dateChosen.get(Calendar.YEAR)

      var imageName = ""

      if(imageFilePath != "") {
        Timber.d("Log: addItem: imageFilePath = $imageFilePath")

        val imageFile = File(imageFilePath)
        imageName = imageFile.name
        model.saveFileToAppStorage(imageFile)
      }

      val item: FavouriteItem = when(type) {
        SONG -> FavouriteItem(primaryInput, "", secondaryInput, "", day, month, year, type, imageName)
        ALBUM -> FavouriteItem("", primaryInput, secondaryInput, "", day, month, year, type, imageName)
        ARTIST -> FavouriteItem("", "", primaryInput, secondaryInput, day, month, year, type, imageName)
        else -> FavouriteItem("", "", "", "", 0, 0, 0, type, imageName)
      }

      if(itemId != null) {
        Timber.d("Log: addItem: Updating old item with ID = $itemId")
        item.id = itemId
      }

      model.addItemAsync(item)
    }
  }

  override fun addItemCallback() {
    Timber.d("Log: addItemCallback: Started")
    view.saveCallback()
  }

  override fun getItem(itemId: Int) {
    Timber.d("Log: getItem: Started")
    model.getItemAsync(itemId)
  }

  override fun getItemCallback(items: MutableList<FavouriteItem>) {
    Timber.d("Log: getItemCallback: Started")

    if(items.isNotEmpty()) {
      view.convertToEditMode(items.first())
      view.displayImage(getArtworkDirectory(context) + items.first().imageName)
    } else {
      Timber.e("Log: getItemCallback: Returned empty list")
    }
  }

  override fun saveFileToAppStorageCallback(fileName: String, exception: NoSuchFileException?) {
    Timber.d("Log: saveFileToAppStorageCallback: Started")
    if(exception != null) {
      view.displayErrorToast()
    }
  }

  override fun updateFilePath(filePath: String) {
    Timber.d("Log: updateFilePath: Started")

    this.oldImageFilePath = imageFilePath
    this.imageFilePath = filePath
    Timber.d("Log: updateFilePath: oldImageFilePath = $oldImageFilePath")
    Timber.d("Log: updateFilePath: imageFilePath = $imageFilePath")

    if(oldImageFilePath != "") {
      model.countUsesOfImage(oldImageFilePath)
    }
  }

  override fun countUsesOfImageCallback(filePath: String, uses: Int) {
    Timber.d("Log: countUsesOfImageCallback: Started with filePath = $filePath and uses = $uses")

    if(uses <= 1) {
      Timber.d("Log: countUsesOfImageCallback: No other uses, deleting image at $filePath")
      model.deleteImage(filePath)
    } else {
      Timber.d("Log: countUsesOfImageCallback: Used elsewhere, not deleting")
    }
  }
}