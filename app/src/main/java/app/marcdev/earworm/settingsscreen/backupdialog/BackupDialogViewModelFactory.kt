package app.marcdev.earworm.settingsscreen.backupdialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.marcdev.earworm.utils.FileUtils

class BackupDialogViewModelFactory(private val fileUtils: FileUtils)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return BackupDialogViewModel(fileUtils) as T
  }
}