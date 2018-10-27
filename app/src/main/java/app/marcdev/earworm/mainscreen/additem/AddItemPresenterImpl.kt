package app.marcdev.earworm.mainscreen.additem

import android.content.Context
import app.marcdev.earworm.ALBUM
import app.marcdev.earworm.ARTIST
import app.marcdev.earworm.SONG
import app.marcdev.earworm.database.FavouriteItem
import timber.log.Timber
import java.util.*

class AddItemPresenterImpl(private val view: AddItemView, context: Context) : AddItemPresenter {

  private val model: AddItemModel

  init {
    model = AddItemModelImpl(this, context)
  }

  override fun addItem(primaryInput: String, secondaryInput: String, type: Int, dateChosen: Calendar, itemId: Int?) {
    Timber.d("Log: addItem: Started")

    if(primaryInput.trim() == "" || secondaryInput.trim() == "") {
      Timber.d("Log: addItem: Empty input")
      view.displayEmptyToast()
    } else {
      Timber.d("Log: addItem: Adding item")
      val day = dateChosen.get(Calendar.DAY_OF_MONTH)
      val month = dateChosen.get(Calendar.MONTH)
      val year = dateChosen.get(Calendar.YEAR)

      val item: FavouriteItem = when(type) {
        SONG -> FavouriteItem(primaryInput, "", secondaryInput, "", day, month, year, type)
        ALBUM -> FavouriteItem("", primaryInput, secondaryInput, "", day, month, year, type)
        ARTIST -> FavouriteItem("", "", primaryInput, secondaryInput, day, month, year, type)
        else -> FavouriteItem("", "", "", "", 0, 0, 0, type)
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
    } else {
      Timber.e("Log: getItemCallback: Returned empty list")
    }
  }
}