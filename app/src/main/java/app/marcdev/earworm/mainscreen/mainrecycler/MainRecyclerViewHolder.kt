package app.marcdev.earworm.mainscreen.mainrecycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.earworm.data.database.FavouriteItem

open class MainRecyclerViewHolder(itemView: View,
                                  private val itemClick: () -> Unit,
                                  private val itemLongClick: (FavouriteItem) -> Unit)
  : RecyclerView.ViewHolder(itemView) {

  protected var displayedItem: FavouriteItem? = null

  init {
    itemView.setOnClickListener {
      itemClick
    }
    itemView.setOnLongClickListener {
      displayedItem?.let { item ->
        itemLongClick(item)
      }
      true
    }
  }

  open fun display(favouriteItemToDisplay: FavouriteItem) {
    // TO BE OVERRIDDEN
  }
}