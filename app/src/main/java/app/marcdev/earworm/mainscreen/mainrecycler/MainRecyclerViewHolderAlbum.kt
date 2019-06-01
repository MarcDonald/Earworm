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

class MainRecyclerViewHolderAlbum(itemView: View,
                                  editClick: (FavouriteItem) -> Unit,
                                  deleteClick: (FavouriteItem) -> Unit)
  : MainRecyclerViewHolder(itemView, editClick, deleteClick) {

  private val albumNameDisplay: TextView = itemView.findViewById(R.id.txt_albumName)
  private val albumDateDisplay: TextView = itemView.findViewById(R.id.txt_albumDate)
  private val albumArtistDisplay: TextView = itemView.findViewById(R.id.txt_albumArtist)
  private var albumImageDisplay: ImageView = itemView.findViewById(R.id.img_album_icon)

  override fun display(favouriteItemToDisplay: FavouriteItem) {
    displayedItem = favouriteItemToDisplay
    albumNameDisplay.text = favouriteItemToDisplay.albumName
    albumArtistDisplay.text = favouriteItemToDisplay.artistName
    val date = formatDateForDisplay(favouriteItemToDisplay.day, favouriteItemToDisplay.month, favouriteItemToDisplay.year)
    albumDateDisplay.text = date

    if(favouriteItemToDisplay.imageName.isNotBlank()) {
      Glide.with(itemView)
        .load(getArtworkDirectory(itemView.context) + favouriteItemToDisplay.imageName)
        .apply(RequestOptions().centerCrop())
        .apply(RequestOptions().error(itemView.resources.getDrawable(R.drawable.ic_error_24px, null)))
        .into(albumImageDisplay)
    } else {
      Glide.with(itemView)
        .load(itemView.resources.getDrawable(R.drawable.ic_album_24px, null))
        .apply(RequestOptions().centerCrop())
        .apply(RequestOptions().error(itemView.resources.getDrawable(R.drawable.ic_error_24px, null)))
        .into(albumImageDisplay)
    }
  }
}