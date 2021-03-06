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

package com.marcdonald.earworm.settingsscreen.restoredialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.earworm.data.database.AppDatabase
import com.marcdonald.earworm.internal.DATABASE_NAME
import com.marcdonald.earworm.utils.FileUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.util.zip.ZipException
import java.util.zip.ZipFile

class RestoreDialogViewModel(private val fileUtils: FileUtils, private val database: AppDatabase) :
		ViewModel() {

	private var restoreFilePath = ""
	private val _displayMessage = MutableLiveData<Boolean>()
	val displayMessage: LiveData<Boolean>
		get() = _displayMessage

	private val _displayButtons = MutableLiveData<Boolean>()
	val displayButtons: LiveData<Boolean>
		get() = _displayButtons

	private val _displayLoading = MutableLiveData<Boolean>()
	val displayLoading: LiveData<Boolean>
		get() = _displayLoading

	private val _displayError = MutableLiveData<Boolean>()
	val displayError: LiveData<Boolean>
		get() = _displayError

	private val _displayDismiss = MutableLiveData<Boolean>()
	val displayDismiss: LiveData<Boolean>
		get() = _displayDismiss

	private val _canDismiss = MutableLiveData<Boolean>()
	val canDismiss: LiveData<Boolean>
		get() = _canDismiss

	init {
		_displayButtons.value = true
		_displayMessage.value = true
		_canDismiss.value = true
		_displayLoading.value = false
		_displayError.value = false
		_displayDismiss.value = false
	}

	fun passArguments(filePath: String) {
		restoreFilePath = filePath
	}

	fun restore() {
		viewModelScope.launch(Dispatchers.IO) {
			_displayLoading.postValue(true)
			_displayMessage.postValue(false)
			_displayButtons.postValue(false)
			_displayDismiss.postValue(false)
			_canDismiss.postValue(false)
			val restoreSuccess = attemptRestore()
			_displayLoading.postValue(false)
			if(!restoreSuccess) {
				_displayError.postValue(true)
				_displayDismiss.postValue(true)
				_canDismiss.postValue(true)
			}
		}
	}

	private fun attemptRestore(): Boolean {
		return if(isValid()) {
			performRestore()
			true
		} else {
			false
		}
	}

	private fun isValid(): Boolean {
		if(restoreFilePath.isBlank()) {
			Timber.e("Log: isValid: No restore file path")
			return false
		}

		try {
			val file = ZipFile(restoreFilePath)
			for(element in file.entries()) {
				if(element.name == DATABASE_NAME)
					return true
			}
		} catch(e: ZipException) {
			Timber.e("Log: isValid: $e")
		}
		return false
	}

	private fun performRestore() {
		File(fileUtils.artworkDirectory).mkdirs()
		clearCurrentArtwork()
		moveArtworkFromBackupToCurrent()
		clearCurrentDatabase()
		moveDatabase()
	}

	private fun clearCurrentArtwork() {
		val artworkDirectory = File(fileUtils.artworkDirectory)
		for(file in artworkDirectory.listFiles()) {
			file.delete()
		}
	}

	private fun moveArtworkFromBackupToCurrent() {
		val file = ZipFile(restoreFilePath)
		file.entries().asSequence().forEach { entry ->
			if(entry.toString() != DATABASE_NAME) {
				val inputStream = file.getInputStream(entry)
				val outFile = File(fileUtils.artworkDirectory + entry.toString())
				val outputStream = FileOutputStream(outFile)
				inputStream.copyTo(outputStream)
				inputStream.close()
				outputStream.close()
			}
		}
	}

	private fun clearCurrentDatabase() {
		database.closeDB()
		File(fileUtils.databaseDirectory).delete()
	}

	private fun moveDatabase() {
		val file = ZipFile(restoreFilePath)
		file.entries().asSequence().forEach { entry ->
			if(entry.toString() == DATABASE_NAME) {
				val inputStream = file.getInputStream(entry)
				val outFile = File(fileUtils.databaseDirectory)
				val outputStream = FileOutputStream(outFile)
				inputStream.copyTo(outputStream)
				inputStream.close()
				outputStream.close()
			}
		}
		/* This is apparently bad practice but I can't find any other way of completely destroying
		 * the application so that the database can be opened again */
		System.exit(1)
	}
}