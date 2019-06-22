package com.marcdonald.earworm.mainscreen.mainrecycler

import android.content.res.Resources
import android.view.View
import android.widget.TextView
import com.marcdonald.earworm.R
import com.marcdonald.earworm.data.database.FavouriteItem
import com.marcdonald.earworm.utils.formatDateForDisplay
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

class MainRecyclerViewHolderGenre(itemView: View,
                                  itemClick: (Int) -> Unit,
                                  itemLongClick: (FavouriteItem) -> Unit,
                                  private val theme: Resources.Theme)
  : MainRecyclerViewHolder(itemView, itemClick, itemLongClick), KodeinAware {
  override val kodein: Kodein by closestKodein(itemView.context)

  private var genreNameDisplay: TextView = itemView.findViewById(R.id.txt_genreName)
  private var genreDateDisplay: TextView = itemView.findViewById(R.id.txt_genreDate)

  override fun display(favouriteItemToDisplay: FavouriteItem) {
    displayedItem = favouriteItemToDisplay
    genreNameDisplay.text = favouriteItemToDisplay.genre
    val date = formatDateForDisplay(favouriteItemToDisplay.day, favouriteItemToDisplay.month, favouriteItemToDisplay.year)
    genreDateDisplay.text = date
  }
}