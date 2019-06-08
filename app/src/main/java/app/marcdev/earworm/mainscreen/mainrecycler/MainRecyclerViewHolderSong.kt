package app.marcdev.earworm.mainscreen.mainrecycler

import android.content.res.Resources
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

class MainRecyclerViewHolderSong(itemView: View,
                                 itemClick: (Int) -> Unit,
                                 itemLongClick: (FavouriteItem) -> Unit,
                                 private val theme: Resources.Theme)
  : MainRecyclerViewHolder(itemView, itemClick, itemLongClick), KodeinAware {
  override val kodein: Kodein by closestKodein(itemView.context)
  private val fileUtils: FileUtils by instance()

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
        .load(fileUtils.artworkDirectory + favouriteItemToDisplay.imageName)
        .apply(RequestOptions().centerCrop())
        .apply(RequestOptions().error(itemView.resources.getDrawable(R.drawable.ic_error_24px, theme)))
        .into(songImageDisplay)
    } else {
      Glide.with(itemView)
        .load(itemView.resources.getDrawable(R.drawable.ic_music_note_24px, theme))
        .apply(RequestOptions().centerCrop())
        .apply(RequestOptions().error(itemView.resources.getDrawable(R.drawable.ic_error_24px, theme)))
        .into(songImageDisplay)
    }
  }
}