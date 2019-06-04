package app.marcdev.earworm.mainscreen.mainrecycler

import android.view.View
import android.widget.TextView
import app.marcdev.earworm.R
import app.marcdev.earworm.data.database.FavouriteItem
import app.marcdev.earworm.utils.formatDateForDisplay
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

class MainRecyclerViewHolderGenre(itemView: View,
                                  itemClick: () -> Unit,
                                  itemLongClick: (FavouriteItem) -> Unit)
  : MainRecyclerViewHolder(itemView, itemClick, itemLongClick), KodeinAware {
  override val kodein: Kodein by closestKodein(itemView.context)

  private var genreNameDisplay: TextView = itemView.findViewById(R.id.txt_genreName)
  private var genreDateDisplay: TextView = itemView.findViewById(R.id.txt_genreDate)

  override fun display(favouriteItemToDisplay: FavouriteItem) {
    displayedItem = favouriteItemToDisplay
    genreNameDisplay.text = favouriteItemToDisplay.genre
    val date = formatDateForDisplay(favouriteItemToDisplay.day, favouriteItemToDisplay.month, favouriteItemToDisplay.year)
    genreDateDisplay.text = date
  }
}