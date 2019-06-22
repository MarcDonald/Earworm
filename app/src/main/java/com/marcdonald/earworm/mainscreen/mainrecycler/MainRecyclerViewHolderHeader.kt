package com.marcdonald.earworm.mainscreen.mainrecycler

import android.view.View
import android.widget.TextView
import com.marcdonald.earworm.R
import com.marcdonald.earworm.data.database.FavouriteItem
import com.marcdonald.earworm.utils.formatDateForHeaderDisplay

open class MainRecyclerViewHolderHeader(itemView: View) : MainRecyclerViewHolder(itemView, {}, {})  {

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
    dateDisplay.text = formatDateForHeaderDisplay(favouriteItemToDisplay.month, favouriteItemToDisplay.year)
  }
}
