/*
 * Copyright (c) 2019 Marc Donald
 *
 * The MIT License (MIT)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.marcdonald.earworm.mainscreen.mainrecycler

import android.view.View
import android.widget.TextView
import com.marcdonald.earworm.R
import com.marcdonald.earworm.data.database.FavouriteItem
import com.marcdonald.earworm.utils.formatDateForHeaderDisplay

open class MainRecyclerViewHolderHeader(itemView: View) : MainRecyclerViewHolder(itemView, {}, {}) {

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
