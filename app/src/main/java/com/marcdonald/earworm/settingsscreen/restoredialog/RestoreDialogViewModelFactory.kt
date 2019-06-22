package com.marcdonald.earworm.settingsscreen.restoredialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.earworm.data.database.AppDatabase
import com.marcdonald.earworm.utils.FileUtils

class RestoreDialogViewModelFactory(private val fileUtils: FileUtils, private val database: AppDatabase)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return RestoreDialogViewModel(fileUtils, database) as T
  }
}