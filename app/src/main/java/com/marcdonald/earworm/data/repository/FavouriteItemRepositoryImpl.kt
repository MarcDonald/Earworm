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

package com.marcdonald.earworm.data.repository

import android.database.sqlite.SQLiteConstraintException
import androidx.lifecycle.LiveData
import com.marcdonald.earworm.data.database.DAO
import com.marcdonald.earworm.data.database.FavouriteItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

class FavouriteItemRepositoryImpl(private val dao: DAO) : FavouriteItemRepository {

	override val allItems: LiveData<List<FavouriteItem>> by lazy {
		dao.getAllItems()
	}

	override suspend fun addItem(item: FavouriteItem) {
		withContext(Dispatchers.IO) {
			try {
				Timber.d("Log: addItem: Item doesn't exist, adding new")
				dao.insertItem(item)
			} catch(exception: SQLiteConstraintException) {
				Timber.d("Log: addItem: Item already exists, updating existing")
				dao.updateItem(item)
			}
		}
	}

	override suspend fun getItem(id: Int): FavouriteItem {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getItemById(id)
		}
	}

	override suspend fun deleteItem(id: Int) {
		withContext(Dispatchers.IO) {
			dao.deleteItemById(id)
		}
	}

	override suspend fun countUsesOfImage(imageName: String): Int {
		return withContext(Dispatchers.IO) {
			return@withContext dao.getNumberOfEntriesUsingImage(imageName)
		}
	}

	companion object {
		@Volatile private var instance: FavouriteItemRepositoryImpl? = null

		fun getInstance(dao: DAO) =
			instance ?: synchronized(this) {
				instance ?: FavouriteItemRepositoryImpl(dao).also { instance = it }
			}
	}
}