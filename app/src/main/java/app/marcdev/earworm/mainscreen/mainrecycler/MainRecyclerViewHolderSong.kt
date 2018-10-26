package app.marcdev.earworm.mainscreen.mainrecycler

import android.view.View
import android.widget.TextView
import app.marcdev.earworm.R
import app.marcdev.earworm.database.FavouriteItem
import app.marcdev.earworm.formatDateForDisplay
import timber.log.Timber

class MainRecyclerViewHolderSong(itemView: View) : MainRecyclerViewHolder(itemView) {

  private var songNameDisplay: TextView = itemView.findViewById(R.id.txt_songName)
  private var songDateDisplay: TextView = itemView.findViewById(R.id.txt_songDate)
  private var songArtistDisplay: TextView = itemView.findViewById(R.id.txt_songArtist)

  override fun display(favouriteItemToDisplay: FavouriteItem) {
    Timber.d("Log: display: $favouriteItemToDisplay")
    songNameDisplay.text = favouriteItemToDisplay.songName
    songArtistDisplay.text = favouriteItemToDisplay.artistName
    val date = formatDateForDisplay(favouriteItemToDisplay.day, favouriteItemToDisplay.month, favouriteItemToDisplay.year)
    songDateDisplay.text = date
  }
}