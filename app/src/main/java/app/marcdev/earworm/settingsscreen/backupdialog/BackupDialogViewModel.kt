package app.marcdev.earworm.settingsscreen.backupdialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.marcdev.earworm.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BackupDialogViewModel(private val fileUtils: FileUtils) : ViewModel() {
  private var _localBackupPath: String = ""
  val localBackupPath: String
    get() = _localBackupPath

  private val _displayDismiss = MutableLiveData<Boolean>()
  val displayDismiss: LiveData<Boolean>
    get() = _displayDismiss

  private val _displayShareButton = MutableLiveData<Boolean>()
  val displayShareButton: LiveData<Boolean>
    get() = _displayShareButton

  private val _displayLoading = MutableLiveData<Boolean>()
  val displayLoading: LiveData<Boolean>
    get() = _displayLoading

  private val _displaySuccess = MutableLiveData<Boolean>()
  val displaySuccess: LiveData<Boolean>
    get() = _displaySuccess

  private val _displayError = MutableLiveData<Boolean>()
  val displayError: LiveData<Boolean>
    get() = _displayError


  init {
    _displaySuccess.value = false
    _displayShareButton.value = false
    _displayDismiss.value = false
    _displayError.value = false
  }

  fun backup() {
    viewModelScope.launch(Dispatchers.IO) {
      _displayLoading.postValue(true)
      val backupSuccess = performBackup()
      _displayLoading.postValue(false)
      _displayDismiss.postValue(true)
      if(backupSuccess) {
        _displaySuccess.postValue(true)
        _displayShareButton.postValue(true)
      } else {
        _displayError.postValue(true)
      }
    }
  }

  private suspend fun performBackup(): Boolean {
    // TODO
    Thread.sleep(3000L)
    return true
  }
}