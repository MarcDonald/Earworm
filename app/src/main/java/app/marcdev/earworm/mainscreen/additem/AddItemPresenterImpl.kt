package app.marcdev.earworm.mainscreen.additem

import android.content.Context
import app.marcdev.earworm.EarwormUtils
import app.marcdev.earworm.database.FavouriteItem
import timber.log.Timber

class AddItemPresenterImpl(private val view: AddItemView, context: Context) : AddItemPresenter {

  private val model: AddItemModel

  init {
    model = AddItemModelImpl(this, context)
  }

  override fun addItem(primaryInput: String, secondaryInput: String, type: Int) {
    Timber.d("Log: addItem: Started")

    if(primaryInput == "" || secondaryInput == "") {
      Timber.d("Log: addItem: Empty input")
      view.displayEmptyToast()
    } else {
      // TODO dates
      Timber.d("Log: addItem: Adding item")
      val item: FavouriteItem = when(type) {
        EarwormUtils.SONG -> FavouriteItem(primaryInput, "", secondaryInput, "", "01-10-2018", type)
        EarwormUtils.ALBUM -> FavouriteItem("", primaryInput, secondaryInput, "", "01-10-2018", type)
        EarwormUtils.ARTIST -> FavouriteItem("", "", primaryInput, secondaryInput, "01-10-2018", type)
        else -> FavouriteItem("", "", "", "", "", type)
      }
      model.addItemAsync(item)
    }
  }

  override fun addItemCallback() {
    Timber.d("Log: addItemCallback: Started")
    view.saveCallback()
  }
}