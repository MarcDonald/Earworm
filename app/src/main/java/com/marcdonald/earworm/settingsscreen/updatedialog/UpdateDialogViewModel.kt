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

package com.marcdonald.earworm.settingsscreen.updatedialog

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marcdonald.earworm.BuildConfig
import com.marcdonald.earworm.data.network.NoConnectivityException
import com.marcdonald.earworm.data.network.github.GithubAPIService
import com.marcdonald.earworm.data.network.github.apiresponse.GithubVersionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UpdateDialogViewModel(private val githubAPIService: GithubAPIService) : ViewModel() {
	private val _displayDismiss = MutableLiveData<Boolean>()
	val displayDismiss: LiveData<Boolean>
		get() = _displayDismiss

	private val _displayOpenButton = MutableLiveData<Boolean>()
	val displayOpenButton: LiveData<Boolean>
		get() = _displayOpenButton

	private val _displayLoading = MutableLiveData<Boolean>()
	val displayLoading: LiveData<Boolean>
		get() = _displayLoading

	private val _displayNoConnection = MutableLiveData<Boolean>()
	val displayNoConnection: LiveData<Boolean>
		get() = _displayNoConnection

	private val _displayNoUpdateAvailable = MutableLiveData<Boolean>()
	val displayNoUpdateAvailable: LiveData<Boolean>
		get() = _displayNoUpdateAvailable

	private val _newVersionName = MutableLiveData<String>()
	val newVersionName: LiveData<String>
		get() = _newVersionName

	private val _displayError = MutableLiveData<Boolean>()
	val displayError: LiveData<Boolean>
		get() = _displayError

	init {
		_displayOpenButton.value = false
		_displayNoConnection.value = false
		_displayNoUpdateAvailable.value = false
		_displayDismiss.value = true
	}

	fun check() {
		viewModelScope.launch(Dispatchers.Default) {
			_displayLoading.postValue(true)
			performCheck()
		}
	}

	private suspend fun performCheck() {
		try {
			val newestVersion = githubAPIService.getNewestVersion()
			checkIfVersionIsNewer(newestVersion)
		} catch(e: NoConnectivityException) {
			_displayLoading.postValue(false)
			_displayNoConnection.postValue(true)
		}
	}

	private fun checkIfVersionIsNewer(newestVersion: GithubVersionResponse) {
		val semicolonIndex = newestVersion.tag_name.indexOf(';')
		try {
			val newestVersionCode = newestVersion.tag_name.substring(semicolonIndex + 1)
			_displayLoading.postValue(false)
			if(newestVersionCode.toInt() > BuildConfig.VERSION_CODE) {
				_newVersionName.postValue(newestVersion.name)
				_displayOpenButton.postValue(true)
				_displayNoUpdateAvailable.postValue(false)
			} else {
				_displayNoUpdateAvailable.postValue(true)
			}
		} catch(e: NumberFormatException) {
			_displayError.postValue(true)
		}
	}
}