package com.marcdonald.earworm.settingsscreen.updatedialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.marcdonald.earworm.data.network.github.GithubAPIService

class UpdateDialogViewModelFactory(private val githubAPIService: GithubAPIService)
  : ViewModelProvider.NewInstanceFactory() {

  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T {
    return UpdateDialogViewModel(githubAPIService) as T
  }
}