package app.marcdev.earworm.mainscreen.additem

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import app.marcdev.earworm.R
import app.marcdev.earworm.database.FavouriteItem
import app.marcdev.earworm.uicomponents.RoundedBottomDialogFragment
import app.marcdev.earworm.utils.*
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import timber.log.Timber
import java.util.*

class AddItemBottomSheet : RoundedBottomDialogFragment(), AddItemView {

  private lateinit var saveButton: MaterialButton
  private lateinit var primaryInput: EditText
  private lateinit var secondaryInput: EditText
  private lateinit var songButton: ImageButton
  private lateinit var albumButton: ImageButton
  private lateinit var artistButton: ImageButton
  private lateinit var presenter: AddItemPresenter
  private lateinit var datePickerDialog: Dialog
  private lateinit var datePicker: DatePicker
  private lateinit var datePickerOk: MaterialButton
  private lateinit var datePickerCancel: MaterialButton
  private lateinit var iconImageView: ImageView
  private lateinit var dateChip: Chip
  private val dateChosen = Calendar.getInstance()
  private val REQUEST_CODE = 1
  // If the itemID is anything other than -1 then it is in edit mode
  private var itemId: Int = -1

  private var type: Int = 0
  private var recyclerUpdateView: RecyclerUpdateView? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.dialog_add_item, container, false)
    presenter = AddItemPresenterImpl(this, activity!!.applicationContext)
    bindViews(view)

    if(arguments != null) {
      Timber.d("Log: onCreateView: Arguments not null")
      this.itemId = arguments!!.getInt("item_id")
      presenter.getItem(itemId)
    } else {
      Timber.d("Log: onCreateView: Arguments null")
      setupDefaults()
    }
    return view
  }

  override fun convertToEditMode(item: FavouriteItem) {
    Timber.d("Log: convertToEditMode: Started")

    when(item.type) {
      SONG -> {
        activateButton(songButton)
        primaryInput.setText(item.songName)
        secondaryInput.setText(item.artistName)
      }

      ALBUM -> {
        activateButton(albumButton)
        primaryInput.setText(item.albumName)
        secondaryInput.setText(item.artistName)
      }

      ARTIST -> {
        activateButton(artistButton)
        primaryInput.setText(item.artistName)
        secondaryInput.setText(item.genre)
      }
    }

    updateDateAndDisplay(item.day, item.month, item.year)
  }

  fun bindRecyclerUpdateView(view: RecyclerUpdateView) {
    Timber.d("Log: bindRecyclerUpdateView: Started")
    this.recyclerUpdateView = view
  }

  private fun bindViews(view: View) {
    Timber.v("Log: bindViews: Started")
    this.saveButton = view.findViewById(R.id.btn_add_item_save)
    saveButton.setOnClickListener(saveOnClickListener)

    this.primaryInput = view.findViewById(R.id.edt_item_primary_input)
    this.secondaryInput = view.findViewById(R.id.edt_item_secondary_input)

    this.songButton = view.findViewById(R.id.btn_add_item_song_choice)
    songButton.setOnClickListener(songOnClickListener)

    this.albumButton = view.findViewById(R.id.btn_add_item_album_choice)
    albumButton.setOnClickListener(albumOnClickListener)

    this.artistButton = view.findViewById(R.id.btn_add_item_artist_choice)
    artistButton.setOnClickListener(artistOnClickListener)

    this.datePickerDialog = Dialog(this.requireActivity())
    datePickerDialog.setContentView(R.layout.dialog_datepicker)
    datePickerDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    this.datePicker = datePickerDialog.findViewById(R.id.datepicker)

    this.datePickerOk = datePickerDialog.findViewById(R.id.btn_datepicker_ok)
    datePickerOk.setOnClickListener(dateOnOkClickListener)

    this.datePickerCancel = datePickerDialog.findViewById(R.id.btn_datepicker_cancel)
    datePickerCancel.setOnClickListener(dateOnCancelClickListener)

    this.dateChip = view.findViewById(R.id.chip_add_item_date_display)
    dateChip.setOnClickListener(dateOnClickListener)

    this.iconImageView = view.findViewById(R.id.img_add_icon)
    iconImageView.setImageDrawable(resources.getDrawable(R.drawable.ic_person_24px, null))
    iconImageView.setOnClickListener(iconOnClickListener)
  }

  private val saveOnClickListener = View.OnClickListener {
    Timber.d("Log: SaveClick: Clicked")
    if(itemId == -1) {
      Timber.d("Log: saveOnClickListener: Adding new item")
      presenter.addItem(primaryInput.text.toString(), secondaryInput.text.toString(), type, dateChosen, null)
    } else {
      Timber.d("Log: saveOnClickListener: Updating item with id = $itemId")
      presenter.addItem(primaryInput.text.toString(), secondaryInput.text.toString(), type, dateChosen, itemId)
    }
  }

  private val dateOnClickListener = View.OnClickListener {
    Timber.d("Log: DateClick: Clicked")
    datePicker.updateDate(dateChosen.get(Calendar.YEAR), dateChosen.get(Calendar.MONTH), dateChosen.get(Calendar.DAY_OF_MONTH))
    datePickerDialog.show()
  }

  private val dateOnOkClickListener = View.OnClickListener {
    Timber.d("Log: DateOkClick: Clicked")

    val day = datePicker.dayOfMonth
    val monthRaw = datePicker.month
    val year = datePicker.year

    updateDateAndDisplay(day, monthRaw, year)

    datePickerDialog.dismiss()
  }

  private val dateOnCancelClickListener = View.OnClickListener {
    Timber.d("Log: DateCancelClick: Clicked")
    datePickerDialog.dismiss()
  }

  private val songOnClickListener = View.OnClickListener {
    Timber.d("Log: SongClick: Clicked")
    activateButton(songButton)
  }

  private val albumOnClickListener = View.OnClickListener {
    Timber.d("Log: AlbumClick: Clicked")
    activateButton(albumButton)
  }

  private val artistOnClickListener = View.OnClickListener {
    Timber.d("Log: ArtistClick: Clicked")
    activateButton(artistButton)
  }

  private val iconOnClickListener = View.OnClickListener {
    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
    intent.type = ("image/*")
    intent.addCategory(Intent.CATEGORY_OPENABLE)
    startActivityForResult(intent, REQUEST_CODE)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if(requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      val uri: Uri? = data!!.data
      Timber.i("Log: onActivityResult: URI Path = ${uri!!.path}")
    }
  }


  private fun setupDefaults() {
    Timber.d("Log: setupDefaults: Started")
    type = SONG
    changeColorOfImageButtonDrawable(activity!!.applicationContext, songButton, true)
    changeColorOfImageButtonDrawable(activity!!.applicationContext, albumButton, false)
    changeColorOfImageButtonDrawable(activity!!.applicationContext, artistButton, false)
    dateChip.text = resources.getString(R.string.today)
  }

  private fun activateButton(button: ImageButton) {
    Timber.d("Log: activateButton: Started")
    var doUpdate = true

    if(type == SONG && button == songButton
       || type == ALBUM && button == albumButton
       || type == ARTIST && button == artistButton
    ) {
      Timber.d("Log: activateButton: No need to update")
      doUpdate = false
    }

    if(doUpdate) {
      Timber.d("Log: activateButton: Activating button $button")
      when(type) {
        SONG -> {
          changeColorOfImageButtonDrawable(activity!!.applicationContext, songButton, false)
        }
        ALBUM -> {
          changeColorOfImageButtonDrawable(activity!!.applicationContext, albumButton, false)
        }
        ARTIST -> {
          changeColorOfImageButtonDrawable(activity!!.applicationContext, artistButton, false)
        }
      }

      when(button) {
        songButton -> {
          changeColorOfImageButtonDrawable(activity!!.applicationContext, songButton, true)
          type = SONG
          primaryInput.hint = resources.getString(R.string.song_name)
          secondaryInput.hint = resources.getString(R.string.artist)
        }

        albumButton -> {
          changeColorOfImageButtonDrawable(activity!!.applicationContext, albumButton, true)
          type = ALBUM
          primaryInput.hint = resources.getString(R.string.album)
          secondaryInput.hint = resources.getString(R.string.artist)
        }

        artistButton -> {
          changeColorOfImageButtonDrawable(activity!!.applicationContext, artistButton, true)
          type = ARTIST
          primaryInput.hint = resources.getString(R.string.artist)
          secondaryInput.hint = resources.getString(R.string.genre)
        }
      }
      primaryInput.setText("")
      secondaryInput.setText("")
      primaryInput.requestFocus()
    }
  }

  private fun updateDateAndDisplay(day: Int, month: Int, year: Int) {
    val todayCalendar = Calendar.getInstance()
    val todayDay = todayCalendar.get(Calendar.DAY_OF_MONTH)
    val todayMonthRaw = todayCalendar.get(Calendar.MONTH)
    val todayYear = todayCalendar.get(Calendar.YEAR)

    if(day == todayDay
       && month == todayMonthRaw
       && year == todayYear
    ) {
      // Display "Today" on chip
      datePickerDialog.dismiss()
      dateChip.text = resources.getString(R.string.today)
      setDate(todayDay, todayMonthRaw, todayYear)
    } else {

      val date = formatDateForDisplay(day, month, year)
      dateChip.text = date

      setDate(day, month, year)
      datePickerDialog.dismiss()
    }
  }

  private fun setDate(day: Int, month: Int, year: Int) {
    Timber.d("Log: setDate: Started with day = $day, month = $month, year = $year")
    dateChosen.set(Calendar.DAY_OF_MONTH, day)
    dateChosen.set(Calendar.MONTH, month)
    dateChosen.set(Calendar.YEAR, year)
  }

  override fun saveCallback() {
    Timber.d("Log: saveCallback: Started")
    if(recyclerUpdateView == null) {
      Timber.e("Log: saveCallback: RecyclerUpdateView is null, cannot update recycler")
    } else {
      recyclerUpdateView!!.fillData()
    }
    Toast.makeText(activity, resources.getString(R.string.item_added), Toast.LENGTH_SHORT).show()
    dismiss()
  }

  override fun displayEmptyToast() {
    Timber.d("Log: displayEmptyToast: Started")
    Toast.makeText(activity, resources.getString(R.string.empty), Toast.LENGTH_SHORT).show()
  }
}