package app.marcdev.earworm.mainscreen

import androidx.lifecycle.*
import app.marcdev.earworm.data.database.FavouriteItem
import app.marcdev.earworm.data.repository.FavouriteItemRepository
import app.marcdev.earworm.utils.*
import kotlinx.coroutines.launch

class MainFragmentViewModel(private val repository: FavouriteItemRepository,
                            private val fileUtils: FileUtils)
  : ViewModel() {

  val displayData = Transformations.map(repository.allItems) { list ->
    val sortedItems = sortByDateDescending(list.toMutableList())
    val listWithHeaders = addListHeaders(sortedItems)
    _displayLoading.value = false
    _displayNoEntries.value = listWithHeaders.isEmpty()
    return@map listWithHeaders
  }

  private val _displayLoading = MutableLiveData<Boolean>()
  val displayLoading: LiveData<Boolean>
    get() = _displayLoading

  private val _displayNoEntries = MutableLiveData<Boolean>()
  val displayNoEntries: LiveData<Boolean>
    get() = _displayNoEntries

  private val _displayNoFilteredResults = MutableLiveData<Boolean>()
  val displayNoFilteredResults: LiveData<Boolean>
    get() = _displayNoFilteredResults

  private val _colorFilterIcon = MutableLiveData<Boolean>()
  val colorFilterIcon: LiveData<Boolean>
    get() = _colorFilterIcon

  private val _displaySearchIcon = MutableLiveData<Boolean>()
  val displaySearchIcon: LiveData<Boolean>
    get() = _displaySearchIcon

  init {
    _displayLoading.value = true
    _displayNoEntries.value = false
    _displayNoFilteredResults.value = false
  }

  fun deleteItem(item: FavouriteItem) {
    viewModelScope.launch {
      repository.deleteItem(item.id)
      deleteImageIfNecessary(item.imageName)
    }
  }

  private suspend fun deleteImageIfNecessary(imageName: String) {
    if(imageName.isNotBlank()) {
      val uses = repository.countUsesOfImage(imageName)
      if(uses == 0) {
        fileUtils.deleteImage(imageName)
      }
    }
  }
}