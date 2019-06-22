package com.marcdonald.earworm.additem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.earworm.data.repository.FavouriteItemRepository
import com.marcdonald.earworm.utils.FileUtils

class AddItemViewModelFactory(private val favouriteItemRepository: FavouriteItemRepository, private val fileUtils: FileUtils)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return AddItemViewModel(favouriteItemRepository, fileUtils) as T
  }
}