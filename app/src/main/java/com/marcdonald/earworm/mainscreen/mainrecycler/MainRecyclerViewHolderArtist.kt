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

class MainRecyclerViewHolderArtist(itemView: View,
																	 itemClick: (Int) -> Unit,
																	 itemLongClick: (FavouriteItem) -> Unit,
																	 private val theme: Resources.Theme)
	: MainRecyclerViewHolder(itemView, itemClick, itemLongClick), KodeinAware {

	override val kodein: Kodein by closestKodein(itemView.context)
	private val fileUtils: FileUtils by instance()

	private val artistNameDisplay: TextView = itemView.findViewById(R.id.txt_main_primary)
	private val artistDateDisplay: TextView = itemView.findViewById(R.id.txt_main_date)
	private val artistGenreDisplay: TextView = itemView.findViewById(R.id.txt_main_secondary)
	private var artistImageDisplay: ImageView = itemView.findViewById(R.id.img_main_icon)

	init {
		itemView.findViewById<TextView>(R.id.txt_main_type).text = itemView.resources.getString(R.string.artist)
	}

	override fun display(favouriteItemToDisplay: FavouriteItem) {
		displayedItem = favouriteItemToDisplay
		artistNameDisplay.text = favouriteItemToDisplay.artistName
		artistGenreDisplay.text = favouriteItemToDisplay.genre
		val date = formatDateForDisplay(favouriteItemToDisplay.day, favouriteItemToDisplay.month, favouriteItemToDisplay.year)
		artistDateDisplay.text = date

		if(favouriteItemToDisplay.imageName.isNotBlank()) {
			Glide.with(itemView)
				.load(fileUtils.artworkDirectory + favouriteItemToDisplay.imageName)
				.apply(RequestOptions().centerCrop())
				.apply(RequestOptions().error(itemView.resources.getDrawable(R.drawable.ic_error_24px, theme)))
				.into(artistImageDisplay)
		} else {
			Glide.with(itemView)
				.load(itemView.resources.getDrawable(R.drawable.ic_person_24px, theme))
				.apply(RequestOptions().centerCrop())
				.apply(RequestOptions().error(itemView.resources.getDrawable(R.drawable.ic_error_24px, theme)))
				.into(artistImageDisplay)
		}
	}
}