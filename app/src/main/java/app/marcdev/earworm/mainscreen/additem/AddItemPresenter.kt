package app.marcdev.earworm.mainscreen.additem

interface AddItemPresenter {

  fun addItem(primaryInput: String, secondaryInput: String, type: Int)

  fun addItemCallback()
}