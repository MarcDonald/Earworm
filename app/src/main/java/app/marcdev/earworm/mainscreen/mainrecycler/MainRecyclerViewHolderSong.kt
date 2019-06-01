package app.marcdev.earworm.mainscreen.mainrecycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.marcdev.earworm.R
import app.marcdev.earworm.data.database.FavouriteItem
import app.marcdev.earworm.utils.formatDateForDisplay
import app.marcdev.earworm.utils.getArtworkDirectory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import timber.log.Timber

class MainRecyclerViewHolderSong(itemView: View,
                                 editClick: (FavouriteItem) -> Unit,
                                 deleteClick: (FavouriteItem) -> Unit)
  : MainRecyclerViewHolder(itemView, editClick, deleteClick) {

  private val songNameDisplay: TextView = itemView.findViewById(R.id.txt_songName)
  private val songDateDisplay: TextView = itemView.findViewById(R.id.txt_songDate)
  private val songArtistDisplay: TextView = itemView.findViewById(R.id.txt_songArtist)
  private val songImageDisplay: ImageView = itemView.findViewById(R.id.img_song_icon)

  override fun display(favouriteItemToDisplay: FavouriteItem) {
    displayedItem = favouriteItemToDisplay
    songNameDisplay.text = favouriteItemToDisplay.songName
    songArtistDisplay.text = favouriteItemToDisplay.artistName
    val date = formatDateForDisplay(favouriteItemToDisplay.day, favouriteItemToDisplay.month, favouriteItemToDisplay.year)
    songDateDisplay.text = date

    if(favouriteItemToDisplay.imageName.isNotBlank()) {
      Glide.with(itemView)
        .load(getArtworkDirectory(itemView.context) + favouriteItemToDisplay.imageName)
        .apply(RequestOptions().centerCrop())
        .apply(RequestOptions().error(itemView.resources.getDrawable(R.drawable.ic_error_24px, null)))
        .into(songImageDisplay)
    } else {
      Glide.with(itemView)
        .load(itemView.resources.getDrawable(R.drawable.ic_music_note_24px, null))
        .apply(RequestOptions().centerCrop())
        .apply(RequestOptions().error(itemView.resources.getDrawable(R.drawable.ic_error_24px, null)))
        .into(songImageDisplay)
    }
  }
}