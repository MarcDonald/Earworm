package com.marcdonald.earworm.mainscreen.mainrecycler

import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.marcdonald.earworm.R
import com.marcdonald.earworm.data.database.FavouriteItem
import com.marcdonald.earworm.utils.FileUtils
import com.marcdonald.earworm.utils.formatDateForDisplay
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

  private val songNameDisplay: TextView = itemView.findViewById(R.id.txt_main_primary)
  private val songDateDisplay: TextView = itemView.findViewById(R.id.txt_main_date)
  private val songArtistDisplay: TextView = itemView.findViewById(R.id.txt_main_secondary)
  private var songImageDisplay: ImageView = itemView.findViewById(R.id.img_main_icon)

  init {
    itemView.findViewById<TextView>(R.id.txt_main_type).text = itemView.resources.getString(R.string.song)
  }

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