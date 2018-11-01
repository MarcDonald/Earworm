package app.marcdev.earworm.mainscreen.mainrecycler

import android.view.View
import android.widget.TextView
import app.marcdev.earworm.R
import app.marcdev.earworm.database.FavouriteItem
import app.marcdev.earworm.utils.formatDateForDisplay
import timber.log.Timber

class MainRecyclerViewHolderAlbum(itemView: View) : MainRecyclerViewHolder(itemView) {

  private var albumNameDisplay: TextView = itemView.findViewById(R.id.txt_albumName)
  private var albumDateDisplay: TextView = itemView.findViewById(R.id.txt_albumDate)
  private var albumArtistDisplay: TextView = itemView.findViewById(R.id.txt_albumArtist)

  override fun display(favouriteItemToDisplay: FavouriteItem) {
    Timber.d("Log: display: $favouriteItemToDisplay")
    albumNameDisplay.text = favouriteItemToDisplay.albumName
    albumArtistDisplay.text = favouriteItemToDisplay.artistName
    val date = formatDateForDisplay(favouriteItemToDisplay.day, favouriteItemToDisplay.month, favouriteItemToDisplay.year)
    albumDateDisplay.text = date
  }
}