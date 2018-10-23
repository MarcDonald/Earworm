package app.marcdev.earworm.mainscreen.mainrecycler

import android.view.View
import android.widget.TextView
import app.marcdev.earworm.R
import app.marcdev.earworm.database.FavouriteItem
import timber.log.Timber

class MainRecyclerViewHolderArtist(itemView: View) : MainRecyclerViewHolder(itemView) {

  private var artistNameDisplay: TextView = itemView.findViewById(R.id.txt_artistName)
  private var artistGenreDisplay: TextView = itemView.findViewById(R.id.txt_artistGenre)
  private var artistDateDisplay: TextView = itemView.findViewById(R.id.txt_artistDate)

  override fun display(favouriteItemToDisplay: FavouriteItem) {
    Timber.d("Log: display: $favouriteItemToDisplay")
    artistNameDisplay.text = favouriteItemToDisplay.artistName
    artistGenreDisplay.text = favouriteItemToDisplay.genre
    // + 1 to month for non-zero-indexed display
    val date = "${favouriteItemToDisplay.day}/${favouriteItemToDisplay.month + 1}/${favouriteItemToDisplay.year}"
    artistDateDisplay.text = date
  }
}