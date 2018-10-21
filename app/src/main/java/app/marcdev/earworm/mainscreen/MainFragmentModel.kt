package app.marcdev.earworm.mainscreen

interface MainFragmentModel {
  fun addItemAsync()

  fun getAllItemsAsync()

  fun clearListAsync()
}