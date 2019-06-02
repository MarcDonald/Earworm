package app.marcdev.earworm.mainscreen

import androidx.lifecycle.*
import app.marcdev.earworm.data.database.FavouriteItem
import app.marcdev.earworm.data.repository.FavouriteItemRepository
import app.marcdev.earworm.utils.*
import kotlinx.coroutines.launch

class MainFragmentViewModel(private val repository: FavouriteItemRepository,
                            private val fileUtils: FileUtils)
  : ViewModel() {

  private val activeFilter = MutableLiveData<ItemFilter>()
  private val allItems = repository.allItems

  private val _displayData = MediatorLiveData<List<FavouriteItem>>()
  val displayData: LiveData<List<FavouriteItem>>
    get() = _displayData

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
    _displayNoEntries.value = false
    _displayNoFilteredResults.value = false
    _displayData.addSource(activeFilter) { filter ->
      workOutDisplayData(allItems.value, filter)
    }
    _displayData.addSource(allItems) { items ->
      workOutDisplayData(items, activeFilter.value)
    }
  }

  private fun workOutDisplayData(allItems: List<FavouriteItem>?, filter: ItemFilter?) {
    _displayLoading.value = true
    displaySearchIconIfNeeded(filter)
    var finalList: List<FavouriteItem>? = null

    allItems?.let { fullList ->
      _displayNoEntries.value = fullList.isEmpty()

      val filteredList = if(filter != null) {
        filterResults(fullList, filter)
      } else {
        fullList
      }
      val sortedList = sortByDateDescending(filteredList)
      finalList = addListHeaders(sortedList)
    }

    _displayData.value = finalList?.toList()
    _displayLoading.value = false
  }

  private fun filterResults(allItems: List<FavouriteItem>, filter: ItemFilter): List<FavouriteItem> {
    val finalList = applyFilter(allItems, filter)
    _displayNoFilteredResults.value = finalList.isEmpty()
    return finalList
  }

  private fun displaySearchIconIfNeeded(filter: ItemFilter?) {
    if(filter == null) {
      _displaySearchIcon.value = true
    } else {
      _displaySearchIcon.value = filter.searchTerm.isBlank()
    }
  }

  fun deleteItem(item: FavouriteItem) {
    viewModelScope.launch {
      repository.deleteItem(item.id)
      deleteImageIfNecessary(item.imageName)
    }
  }

  fun search(searchTermArg: String) {
    val newFilter: ItemFilter? = if(activeFilter.value == null)
      DEFAULT_FILTER.copy()
    else
      activeFilter.value

    newFilter?.searchTerm = searchTermArg
    activeFilter.value = newFilter
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