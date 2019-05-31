package app.marcdev.earworm.mainscreen.mainrecycler

import android.view.View
import android.widget.TextView
import app.marcdev.earworm.R
import app.marcdev.earworm.data.database.FavouriteItem
import app.marcdev.earworm.utils.getMonthName
import timber.log.Timber

open class MainRecyclerViewHolderHeader(itemView: View) : MainRecyclerViewHolder(itemView) {

  private var dateDisplay: TextView = itemView.findViewById(R.id.txt_header_title)

  private val itemClickListener = View.OnClickListener {
    // Does nothing but overrides default click listener
  }

  private val itemLongClickListener = View.OnLongClickListener {
    // Does nothing but overrides default long click listener
    return@OnLongClickListener true
  }

  init {
    itemView.setOnClickListener(itemClickListener)
    itemView.setOnLongClickListener(itemLongClickListener)
  }

  override fun display(favouriteItemToDisplay: FavouriteItem) {
    Timber.d("Log: display: $favouriteItemToDisplay")

    dateDisplay.text = ("${getMonthName(favouriteItemToDisplay.month, itemView.context)} ${favouriteItemToDisplay.year}")
  }
}
