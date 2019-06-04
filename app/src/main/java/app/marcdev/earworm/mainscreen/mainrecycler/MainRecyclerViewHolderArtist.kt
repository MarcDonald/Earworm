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

class MainRecyclerViewHolderArtist(itemView: View,
                                   itemClick: () -> Unit,
                                   itemLongClick: (FavouriteItem) -> Unit)
  : MainRecyclerViewHolder(itemView, itemClick, itemLongClick) {

  private val artistNameDisplay: TextView = itemView.findViewById(R.id.txt_artistName)
  private val artistGenreDisplay: TextView = itemView.findViewById(R.id.txt_artistGenre)
  private val artistDateDisplay: TextView = itemView.findViewById(R.id.txt_artistDate)
  private val artistImageDisplay: ImageView = itemView.findViewById(R.id.img_artist_icon)

  override fun display(favouriteItemToDisplay: FavouriteItem) {
    displayedItem = favouriteItemToDisplay
    artistNameDisplay.text = favouriteItemToDisplay.artistName
    artistGenreDisplay.text = favouriteItemToDisplay.genre
    val date = formatDateForDisplay(favouriteItemToDisplay.day, favouriteItemToDisplay.month, favouriteItemToDisplay.year)
    artistDateDisplay.text = date

    if(favouriteItemToDisplay.imageName.isNotBlank()) {
      Glide.with(itemView)
        .load(getArtworkDirectory(itemView.context) + favouriteItemToDisplay.imageName)
        .apply(RequestOptions().centerCrop())
        .apply(RequestOptions().error(itemView.resources.getDrawable(R.drawable.ic_error_24px, null)))
        .into(artistImageDisplay)
    } else {
      Glide.with(itemView)
        .load(itemView.resources.getDrawable(R.drawable.ic_person_24px, null))
        .apply(RequestOptions().centerCrop())
        .apply(RequestOptions().error(itemView.resources.getDrawable(R.drawable.ic_error_24px, null)))
        .into(artistImageDisplay)
    }
  }
}