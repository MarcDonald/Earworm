package app.marcdev.earworm.mainscreen

import app.marcdev.earworm.ItemFilter
import app.marcdev.earworm.database.FavouriteItem

interface MainFragmentView {

  fun displayNoEntriesWarning(display: Boolean)

  fun displayNoFilteredResultsWarning(display: Boolean)

  fun displayAddedToast()

  fun displayItemDeletedToast()

  fun updateRecycler(items: List<FavouriteItem>)

  fun displayProgress(isVisible: Boolean)

  fun displayEditItemSheet(itemId: Int)

  fun getActiveFilter(): ItemFilter

  fun changeSearchIcon(isSearch: Boolean)

  fun activateFilterIcon(isActive: Boolean)
}