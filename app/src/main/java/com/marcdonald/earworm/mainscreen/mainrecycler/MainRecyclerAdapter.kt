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

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marcdonald.earworm.R
import com.marcdonald.earworm.data.database.FavouriteItem
import com.marcdonald.earworm.internal.*

class MainRecyclerAdapter(context: Context,
													private val itemClick: (Int) -> Unit,
													private val itemLongClick: (FavouriteItem) -> Unit,
													private val theme: Resources.Theme)
	: RecyclerView.Adapter<MainRecyclerViewHolder>() {

	private var items: List<FavouriteItem> = mutableListOf()
	private var inflater: LayoutInflater = LayoutInflater.from(context)

	override fun getItemViewType(position: Int): Int {
		return items[position].type
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainRecyclerViewHolder {
		lateinit var viewHolder: MainRecyclerViewHolder

		when(viewType) {
			HEADER -> {
				val view = inflater.inflate(R.layout.item_header, parent, false)
				viewHolder = MainRecyclerViewHolderHeader(view)
			}
			SONG   -> {
				val view = inflater.inflate(R.layout.item_mainrecycler, parent, false)
				viewHolder = MainRecyclerViewHolderSong(view, itemClick, itemLongClick, theme)
			}

			ALBUM  -> {
				val view = inflater.inflate(R.layout.item_mainrecycler, parent, false)
				viewHolder = MainRecyclerViewHolderAlbum(view, itemClick, itemLongClick, theme)
			}

			ARTIST -> {
				val view = inflater.inflate(R.layout.item_mainrecycler, parent, false)
				viewHolder = MainRecyclerViewHolderArtist(view, itemClick, itemLongClick, theme)
			}

			GENRE  -> {
				val view = inflater.inflate(R.layout.item_mainrecycler_genre, parent, false)
				viewHolder = MainRecyclerViewHolderGenre(view, itemClick, itemLongClick, theme)
			}
		}

		return viewHolder
	}

	override fun onBindViewHolder(holder: MainRecyclerViewHolder, position: Int) {
		holder.display(items[position])
	}

	override fun getItemCount(): Int {
		return items.size
	}

	fun updateItems(items: List<FavouriteItem>) {
		this.items = items
		notifyDataSetChanged()
	}
}