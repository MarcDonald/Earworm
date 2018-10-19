package app.marcdev.earworm.mainscreen.mainrecycler

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import app.marcdev.earworm.database.FavouriteItem

open class MainRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
{
  open fun display(favouriteItemToDisplay: FavouriteItem)
  {
    // TO BE OVERRIDEN
  }
}