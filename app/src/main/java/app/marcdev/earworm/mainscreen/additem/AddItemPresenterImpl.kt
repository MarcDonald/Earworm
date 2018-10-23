package app.marcdev.earworm.mainscreen.additem

import android.content.Context
import app.marcdev.earworm.EarwormUtils
import app.marcdev.earworm.database.FavouriteItem
import timber.log.Timber
import java.util.*

class AddItemPresenterImpl(private val view: AddItemView, context: Context) : AddItemPresenter {

  private val model: AddItemModel

  init {
    model = AddItemModelImpl(this, context)
  }

  override fun addItem(primaryInput: String, secondaryInput: String, type: Int, dateChosen: Calendar) {
    Timber.d("Log: addItem: Started")

    if(primaryInput == "" || secondaryInput == "") {
      Timber.d("Log: addItem: Empty input")
      view.displayEmptyToast()
    } else {
      Timber.d("Log: addItem: Adding item")
      val day = dateChosen.get(Calendar.DAY_OF_MONTH)
      val month = dateChosen.get(Calendar.MONTH)
      val year = dateChosen.get(Calendar.YEAR)

      val item: FavouriteItem = when(type) {
        EarwormUtils.SONG -> FavouriteItem(primaryInput, "", secondaryInput, "", day, month, year, type)
        EarwormUtils.ALBUM -> FavouriteItem("", primaryInput, secondaryInput, "", day, month, year, type)
        EarwormUtils.ARTIST -> FavouriteItem("", "", primaryInput, secondaryInput, day, month, year, type)
        else -> FavouriteItem("", "", "", "", 0, 0, 0, type)
      }
      model.addItemAsync(item)
    }
  }

  override fun addItemCallback() {
    Timber.d("Log: addItemCallback: Started")
    view.saveCallback()
  }
}