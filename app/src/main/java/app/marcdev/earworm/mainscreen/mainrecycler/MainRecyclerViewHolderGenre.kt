package app.marcdev.earworm.mainscreen.mainrecycler

import android.view.View
import android.widget.TextView
import app.marcdev.earworm.R
import app.marcdev.earworm.data.database.FavouriteItem
import app.marcdev.earworm.utils.formatDateForDisplay
import timber.log.Timber

class MainRecyclerViewHolderGenre(itemView: View) : MainRecyclerViewHolder(itemView) {

  private var genreNameDisplay: TextView = itemView.findViewById(R.id.txt_genreName)
  private var genreDateDisplay: TextView = itemView.findViewById(R.id.txt_genreDate)

  override fun display(favouriteItemToDisplay: FavouriteItem) {
    Timber.d("Log: display: $favouriteItemToDisplay")
    genreNameDisplay.text = favouriteItemToDisplay.genre
    val date = formatDateForDisplay(favouriteItemToDisplay.day, favouriteItemToDisplay.month, favouriteItemToDisplay.year)
    genreDateDisplay.text = date
  }
}