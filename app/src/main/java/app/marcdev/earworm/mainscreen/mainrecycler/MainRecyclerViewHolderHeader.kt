package app.marcdev.earworm.mainscreen.mainrecycler

import android.view.View
import android.widget.TextView
import app.marcdev.earworm.R
import app.marcdev.earworm.database.FavouriteItem
import app.marcdev.earworm.utils.getMonthName
import timber.log.Timber

open class MainRecyclerViewHolderHeader(itemView: View) : MainRecyclerViewHolder(itemView) {

  private var dateDisplay: TextView = itemView.findViewById(R.id.txt_header_title)

  override fun display(favouriteItemToDisplay: FavouriteItem) {
    Timber.d("Log: display: $favouriteItemToDisplay")

    dateDisplay.text = ("${getMonthName(favouriteItemToDisplay.month, itemView.context)} ${favouriteItemToDisplay.year}")
  }
}
