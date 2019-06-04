package app.marcdev.earworm.additem

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.marcdev.earworm.data.database.FavouriteItem
import app.marcdev.earworm.data.repository.FavouriteItemRepository
import app.marcdev.earworm.internal.ALBUM
import app.marcdev.earworm.internal.ARTIST
import app.marcdev.earworm.internal.SONG
import app.marcdev.earworm.utils.FileUtils
import app.marcdev.earworm.utils.formatDateForDisplay
import kotlinx.coroutines.launch
import java.io.File
import java.util.*

class AddItemViewModel(private val repository: FavouriteItemRepository,
                       private val fileUtils: FileUtils)
  : ViewModel() {

  private var itemId = -1
  private var _date = Calendar.getInstance()
  val date: Calendar
    get() = _date

  private val _displayEmptyError = MutableLiveData<Boolean>()
  val displayEmpty: LiveData<Boolean>
    get() = _displayEmptyError

  private val _displayError = MutableLiveData<Boolean>()
  val displayError: LiveData<Boolean>
    get() = _displayError

  private val _displayAdded = MutableLiveData<Boolean>()
  val displayAdded: LiveData<Boolean>
    get() = _displayAdded

  private val _selectedType = MutableLiveData<Int>()
  val selectedType: LiveData<Int>
    get() = _selectedType

  private val _dateDisplay = MutableLiveData<String>()
  val dateDisplay: LiveData<String>
    get() = _dateDisplay

  private val _primaryInputDisplay = MutableLiveData<String>()
  val primaryInputDisplay: LiveData<String>
    get() = _primaryInputDisplay

  private val _secondaryInputDisplay = MutableLiveData<String>()
  val secondaryInputDisplay: LiveData<String>
    get() = _secondaryInputDisplay

  private var oldImageName = ""
  private var currentImageName = ""
  private var newImagePath = ""
  private val _displayImage = MutableLiveData<String>()
  val displayImage: LiveData<String>
    get() = _displayImage

  private val _dismiss = MutableLiveData<Boolean>()
  val dismiss: LiveData<Boolean>
    get() = _dismiss

  fun passArguments(itemIdArg: Int) {
    itemId = itemIdArg
    if(itemId != -1) {
      convertToEditMode()
    } else {
      setDefaults()
    }
  }

  private fun convertToEditMode() {
    viewModelScope.launch {
      val item = repository.getItem(itemId)
      _selectedType.value = item.type
      setDate(item.day, item.month, item.year)
      setInputs(item)
      if(item.imageName.isNotBlank()) {
        currentImageName = item.imageName
        _displayImage.value = fileUtils.artworkDirectory + item.imageName
      }
    }
  }

  private fun setDefaults() {
    _selectedType.value = SONG
    getDateDisplay()
  }

  fun save(primaryInput: String, secondaryInput: String) {
    if(primaryInput.isBlank() || secondaryInput.isBlank()) {
      _displayEmptyError.value = true
    } else {
      val day = _date.get(Calendar.DAY_OF_MONTH)
      val month = _date.get(Calendar.MONTH)
      val year = _date.get(Calendar.YEAR)

      val imageNameToSave = if(newImagePath.isBlank())
        currentImageName
      else {
        saveNewImage()
        File(newImagePath).name
      }

      if(oldImageName.isNotBlank())
        deleteOldImageIfNecessary()

      selectedType.value?.let { type ->
        val item: FavouriteItem = when(type) {
          SONG -> FavouriteItem(primaryInput, "", secondaryInput, "", day, month, year, type, imageNameToSave)
          ALBUM -> FavouriteItem("", primaryInput, secondaryInput, "", day, month, year, type, imageNameToSave)
          ARTIST -> FavouriteItem("", "", primaryInput, secondaryInput, day, month, year, type, imageNameToSave)
          else -> FavouriteItem("", "", "", "", 0, 0, 0, type, imageNameToSave)
        }

        if(itemId != -1) {
          item.id = itemId
        }

        viewModelScope.launch {
          repository.addItem(item)
          _displayAdded.value = true
          _dismiss.value = true
        }
      }
    }
  }

  fun setDate(day: Int, month: Int, year: Int) {
    _date.set(Calendar.DAY_OF_MONTH, day)
    _date.set(Calendar.MONTH, month)
    _date.set(Calendar.YEAR, year)
    getDateDisplay()
  }

  fun setNewImage(filePath: String) {
    newImagePath = filePath
    _displayImage.value = filePath
  }

  private fun saveNewImage() {
    fileUtils.saveImage(File(newImagePath))
  }

  private fun deleteOldImageIfNecessary() {
    viewModelScope.launch {
      if(oldImageName.isNotBlank()) {
        val uses = repository.countUsesOfImage(oldImageName)
        if(uses <= 1) {
          fileUtils.deleteImage(oldImageName)
        }
      }
    }
  }

  fun removeImage() {
    if(currentImageName.isNotBlank()) {
      oldImageName = currentImageName
      currentImageName = ""
      _displayImage.value = ""
    }
  }

  fun setType(type: Int) {
    if(_selectedType.value != type)
      _selectedType.value = type
  }

  private fun setInputs(item: FavouriteItem) {
    when(item.type) {
      SONG -> {
        _primaryInputDisplay.value = item.songName
        _secondaryInputDisplay.value = item.artistName
      }
      ALBUM -> {
        _primaryInputDisplay.value = item.albumName
        _secondaryInputDisplay.value = item.artistName
      }
      ARTIST -> {
        _primaryInputDisplay.value = item.artistName
        _secondaryInputDisplay.value = item.genre
      }
    }
  }

  private fun getDateDisplay() {
    val day = _date.get(Calendar.DAY_OF_MONTH)
    val month = _date.get(Calendar.MONTH)
    val year = _date.get(Calendar.YEAR)

    val today = Calendar.getInstance()
    val displayValue = if(day == today.get(Calendar.DAY_OF_MONTH) && month == today.get(Calendar.MONTH) && year == today.get(Calendar.YEAR)) {
      ""
    } else {
      formatDateForDisplay(_date)
    }
    _dateDisplay.value = displayValue
  }
}