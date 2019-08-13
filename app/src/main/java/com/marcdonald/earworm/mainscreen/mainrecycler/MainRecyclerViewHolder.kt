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
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.earworm.data.database.FavouriteItem

open class MainRecyclerViewHolder(itemView: View,
																	private val itemClick: (Int) -> Unit,
																	private val itemLongClick: (FavouriteItem) -> Unit)
	: RecyclerView.ViewHolder(itemView) {

	protected var displayedItem: FavouriteItem? = null

	init {
		itemView.setOnClickListener {
			displayedItem?.let { item ->
				itemClick(item.id)
			}
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