package app.marcdev.earworm.settingsscreen.restoredialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.earworm.data.database.AppDatabase
import app.marcdev.earworm.utils.FileUtils

class RestoreDialogViewModelFactory(private val fileUtils: FileUtils, private val database: AppDatabase)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return RestoreDialogViewModel(fileUtils, database) as T
  }
}