package com.marcdonald.earworm.mainscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.earworm.data.repository.FavouriteItemRepository
import com.marcdonald.earworm.utils.FileUtils

class MainFragmentViewModelFactory(private val favouriteItemRepository: FavouriteItemRepository, private val fileUtils: FileUtils)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return MainFragmentViewModel(favouriteItemRepository, fileUtils) as T
  }
}