package app.marcdev.earworm.mainscreen.mainrecycler

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.marcdev.earworm.R
import app.marcdev.earworm.data.database.FavouriteItem
import app.marcdev.earworm.utils.FileUtils
import app.marcdev.earworm.utils.formatDateForDisplay
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class MainRecyclerViewHolderAlbum(itemView: View,
                                  itemClick: () -> Unit,
                                  itemLongClick: (FavouriteItem) -> Unit)
  : MainRecyclerViewHolder(itemView, itemClick, itemLongClick), KodeinAware {
  override val kodein: Kodein by closestKodein(itemView.context)
  private val fileUtils: FileUtils by instance()

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
        .load(fileUtils.artworkDirectory + favouriteItemToDisplay.imageName)
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