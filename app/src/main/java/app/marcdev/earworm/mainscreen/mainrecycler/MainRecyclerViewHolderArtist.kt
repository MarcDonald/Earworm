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

class MainRecyclerViewHolderArtist(itemView: View,
                                   itemClick: (Int) -> Unit,
                                   itemLongClick: (FavouriteItem) -> Unit,
                                   private val theme: Resources.Theme)
  : MainRecyclerViewHolder(itemView, itemClick, itemLongClick), KodeinAware {
  override val kodein: Kodein by closestKodein(itemView.context)
  private val fileUtils: FileUtils by instance()

  private val artistNameDisplay: TextView = itemView.findViewById(R.id.txt_main_primary)
  private val artistDateDisplay: TextView = itemView.findViewById(R.id.txt_main_date)
  private val artistGenreDisplay: TextView = itemView.findViewById(R.id.txt_main_secondary)
  private var artistImageDisplay: ImageView = itemView.findViewById(R.id.img_main_icon)

  init {
    itemView.findViewById<TextView>(R.id.txt_main_type).text = itemView.resources.getString(R.string.artist)
  }

  override fun display(favouriteItemToDisplay: FavouriteItem) {
    displayedItem = favouriteItemToDisplay
    artistNameDisplay.text = favouriteItemToDisplay.artistName
    artistGenreDisplay.text = favouriteItemToDisplay.genre
    val date = formatDateForDisplay(favouriteItemToDisplay.day, favouriteItemToDisplay.month, favouriteItemToDisplay.year)
    artistDateDisplay.text = date

    if(favouriteItemToDisplay.imageName.isNotBlank()) {
      Glide.with(itemView)
        .load(fileUtils.artworkDirectory + favouriteItemToDisplay.imageName)
        .apply(RequestOptions().centerCrop())
        .apply(RequestOptions().error(itemView.resources.getDrawable(R.drawable.ic_error_24px, theme)))
        .into(artistImageDisplay)
    } else {
      Glide.with(itemView)
        .load(itemView.resources.getDrawable(R.drawable.ic_person_24px, theme))
        .apply(RequestOptions().centerCrop())
        .apply(RequestOptions().error(itemView.resources.getDrawable(R.drawable.ic_error_24px, theme)))
        .into(artistImageDisplay)
    }
  }
}