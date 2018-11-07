package app.marcdev.earworm.mainscreen.mainrecycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.marcdev.earworm.R
import app.marcdev.earworm.database.FavouriteItem
import app.marcdev.earworm.utils.formatDateForDisplay
import app.marcdev.earworm.utils.getArtworkDirectory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import timber.log.Timber

class MainRecyclerViewHolderSong(itemView: View) : MainRecyclerViewHolder(itemView) {

  private val songNameDisplay: TextView = itemView.findViewById(R.id.txt_songName)
  private val songDateDisplay: TextView = itemView.findViewById(R.id.txt_songDate)
  private val songArtistDisplay: TextView = itemView.findViewById(R.id.txt_songArtist)
  private val songImageDisplay: ImageView = itemView.findViewById(R.id.img_song_icon)

  override fun display(favouriteItemToDisplay: FavouriteItem) {
    Timber.d("Log: display: $favouriteItemToDisplay")
    songNameDisplay.text = favouriteItemToDisplay.songName
    songArtistDisplay.text = favouriteItemToDisplay.artistName
    val date = formatDateForDisplay(favouriteItemToDisplay.day, favouriteItemToDisplay.month, favouriteItemToDisplay.year)
    songDateDisplay.text = date

    if(favouriteItemToDisplay.imageName.isNotBlank()) {
      Timber.d("Log: display: ${favouriteItemToDisplay.imageName}")
      Glide.with(itemView)
        .load(getArtworkDirectory(itemView.context) + favouriteItemToDisplay.imageName)
        .apply(RequestOptions().centerCrop())
        .apply(RequestOptions().error(itemView.resources.getDrawable(R.drawable.ic_error_24px, null)))
        .into(songImageDisplay)
    }
  }
}