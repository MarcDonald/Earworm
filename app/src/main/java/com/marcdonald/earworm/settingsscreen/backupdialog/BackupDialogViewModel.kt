package com.marcdonald.earworm.settingsscreen.backupdialog

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.earworm.internal.DATABASE_NAME
import com.marcdonald.earworm.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class BackupDialogViewModel(private val fileUtils: FileUtils) : ViewModel() {
  private var _localBackupUri: Uri? = null
  val localBackupURI: Uri?
    get() = _localBackupUri

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

  init {
    _displaySuccess.value = false
    _displayShareButton.value = false
    _displayDismiss.value = false
  }

  fun backup() {
    viewModelScope.launch(Dispatchers.IO) {
      _displayLoading.postValue(true)
      performBackup()
      _displayLoading.postValue(false)
      _displayDismiss.postValue(true)
      _displaySuccess.postValue(true)
      _displayShareButton.postValue(true)
    }
  }

  private fun performBackup() {
    File(fileUtils.localBackupDirectory).mkdirs()
    val filePath = fileUtils.localBackupDirectory + "Backup.earworm"
    val file = File(filePath)
    if(file.exists())
      file.delete()
    file.createNewFile()

    val zipOutputStream = ZipOutputStream(BufferedOutputStream(FileOutputStream(file)))
    backupDatabase(zipOutputStream)
    backupArtwork(zipOutputStream)
    zipOutputStream.close()

    _localBackupUri = fileUtils.getUriForFilePath(filePath)
  }

  private fun backupDatabase(zipOutputStream: ZipOutputStream) {
    val databaseFileInputStream = FileInputStream(fileUtils.databaseDirectory)
    val databaseBufferedInputStream = BufferedInputStream(databaseFileInputStream)
    val databaseEntry = ZipEntry(DATABASE_NAME)
    zipOutputStream.putNextEntry(databaseEntry)
    databaseBufferedInputStream.copyTo(zipOutputStream, 1024)
    zipOutputStream.closeEntry()
    databaseBufferedInputStream.close()
  }

  private fun backupArtwork(zipOutputStream: ZipOutputStream) {
    for(fileName in File(fileUtils.artworkDirectory).list()) {
      val fileInputStream = FileInputStream(fileUtils.artworkDirectory + fileName)
      val origin = BufferedInputStream(fileInputStream)
      val entry = ZipEntry(fileName)
      zipOutputStream.putNextEntry(entry)
      origin.copyTo(zipOutputStream, 1024)
      zipOutputStream.closeEntry()
      origin.close()
    }
  }
}