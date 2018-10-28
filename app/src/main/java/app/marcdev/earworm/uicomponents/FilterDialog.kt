package app.marcdev.earworm.uicomponents

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.DatePicker
import app.marcdev.earworm.DEFAULT_FILTER
import app.marcdev.earworm.ItemFilter
import app.marcdev.earworm.R
import app.marcdev.earworm.formatDateForDisplay
import app.marcdev.earworm.mainscreen.MainFragmentPresenter
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import timber.log.Timber
import java.util.*

class FilterDialog(context: Context, private val presenter: MainFragmentPresenter) : Dialog(context) {

  private lateinit var displaySongCheckbox: CheckBox
  private lateinit var displayAlbumCheckbox: CheckBox
  private lateinit var displayArtistCheckbox: CheckBox
  private lateinit var startDateDisplay: Chip
  private lateinit var endDateDisplay: Chip
  private lateinit var startDatePickerDialog: Dialog
  private lateinit var endDatePickerDialog: Dialog
  var activeFilter: ItemFilter = DEFAULT_FILTER.copy()

  init {
    Timber.d("Log: FilterDialog Init: Started")
    setContentView(R.layout.dialog_filter)
    window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    bindViews()
    initCheckboxes()
  }

  private fun bindViews() {
    Timber.d("Log: bindViews: Started")

    initStartDatePickerDialog()
    initEndDatePickerDialog()

    this.startDateDisplay = findViewById(R.id.chip_filter_start)
    startDateDisplay.setOnClickListener {
      Timber.d("Log: startDateClickListener: Started")
      startDatePickerDialog.show()
    }

    this.endDateDisplay = findViewById(R.id.chip_filter_end)
    endDateDisplay.setOnClickListener {
      Timber.d("Log: endDateClickListener: Started")
      endDatePickerDialog.show()
    }

    this.displaySongCheckbox = findViewById(R.id.chk_filter_song)
    displaySongCheckbox.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
      activeFilter.includeSongs = isChecked
    }

    this.displayAlbumCheckbox = findViewById(R.id.chk_filter_album)
    displayAlbumCheckbox.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
      activeFilter.includeAlbums = isChecked
    }

    this.displayArtistCheckbox = findViewById(R.id.chk_filter_artist)
    displayArtistCheckbox.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
      activeFilter.includeArtists = isChecked
    }

    val submitButton: MaterialButton = findViewById(R.id.btn_filter_ok)
    submitButton.setOnClickListener {
      Timber.d("Log: submitButtonOnClickListener: Started")
      presenter.getAllItems(activeFilter)
      dismiss()
    }
  }

  private fun initStartDatePickerDialog() {
    Timber.d("Log: initStartDatePickerDialog: Started")

    this.startDatePickerDialog = Dialog(context)
    startDatePickerDialog.setContentView(R.layout.dialog_datepicker_filter)
    startDatePickerDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val datePicker: DatePicker = startDatePickerDialog.findViewById(R.id.datepicker_filter)

    val cancelButton: MaterialButton = startDatePickerDialog.findViewById(R.id.btn_datepicker_filter_cancel)
    cancelButton.setOnClickListener {
      Timber.d("Log: cancelButtonOnClickListener: Started")
      startDatePickerDialog.dismiss()
    }

    val okButton: MaterialButton = startDatePickerDialog.findViewById(R.id.btn_datepicker_filter_ok)
    okButton.setOnClickListener {
      Timber.d("Log: okButtonOnClickListener: Started")
      activeFilter.startDay = datePicker.dayOfMonth
      activeFilter.startMonth = datePicker.month
      activeFilter.startYear = datePicker.year
      startDateDisplay.text = formatDateForDisplay(datePicker.dayOfMonth, datePicker.month, datePicker.year)
      startDatePickerDialog.dismiss()
    }

    val startButton: MaterialButton = startDatePickerDialog.findViewById(R.id.btn_datepicker_filter_start_end)
    startButton.setOnClickListener {
      Timber.d("Log: startButtonOnClickListener: Started")
      activeFilter.startDay = DEFAULT_FILTER.startDay
      activeFilter.startMonth = DEFAULT_FILTER.startMonth
      activeFilter.startYear = DEFAULT_FILTER.startYear
      startDateDisplay.text = context.resources.getString(R.string.start)
      val todayCalendar = Calendar.getInstance()
      datePicker.updateDate(todayCalendar.get(Calendar.YEAR), todayCalendar.get(Calendar.MONTH), todayCalendar.get(Calendar.DAY_OF_MONTH))
      startDatePickerDialog.dismiss()
    }
  }

  private fun initEndDatePickerDialog() {
    Timber.d("Log: initEndDatePickerDialog: Started")

    this.endDatePickerDialog = Dialog(context)
    endDatePickerDialog.setContentView(R.layout.dialog_datepicker_filter)
    endDatePickerDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val datePicker: DatePicker = endDatePickerDialog.findViewById(R.id.datepicker_filter)

    val cancelButton: MaterialButton = endDatePickerDialog.findViewById(R.id.btn_datepicker_filter_cancel)
    cancelButton.setOnClickListener {
      Timber.d("Log: cancelButtonOnClickListener: Started")
      endDatePickerDialog.dismiss()
    }

    val okButton: MaterialButton = endDatePickerDialog.findViewById(R.id.btn_datepicker_filter_ok)
    okButton.setOnClickListener {
      Timber.d("Log: okButtonOnClickListener: Started")
      activeFilter.endDay = datePicker.dayOfMonth
      activeFilter.endMonth = datePicker.month
      activeFilter.endYear = datePicker.year
      endDateDisplay.text = formatDateForDisplay(datePicker.dayOfMonth, datePicker.month, datePicker.year)
      endDatePickerDialog.dismiss()
    }

    val endButton: MaterialButton = endDatePickerDialog.findViewById(R.id.btn_datepicker_filter_start_end)
    endButton.text = context.resources.getString(R.string.end)
    endButton.setOnClickListener {
      Timber.d("Log: startButtonOnClickListener: Started")
      activeFilter.endDay = DEFAULT_FILTER.endDay
      activeFilter.endMonth = DEFAULT_FILTER.endMonth
      activeFilter.endYear = DEFAULT_FILTER.endYear
      endDateDisplay.text = context.resources.getString(R.string.end)
      val todayCalendar = Calendar.getInstance()
      datePicker.updateDate(todayCalendar.get(Calendar.YEAR), todayCalendar.get(Calendar.MONTH), todayCalendar.get(Calendar.DAY_OF_MONTH))
      endDatePickerDialog.dismiss()
    }
  }

  private fun initCheckboxes() {
    Timber.d("Log: initCheckboxes: Started")
    displaySongCheckbox.isChecked = activeFilter.includeSongs
    displayAlbumCheckbox.isChecked = activeFilter.includeAlbums
    displayArtistCheckbox.isChecked = activeFilter.includeArtists
  }
}