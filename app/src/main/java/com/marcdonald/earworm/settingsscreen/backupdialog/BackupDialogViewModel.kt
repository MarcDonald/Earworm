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