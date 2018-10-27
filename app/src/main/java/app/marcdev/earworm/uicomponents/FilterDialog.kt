package app.marcdev.earworm.uicomponents

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.Toast
import app.marcdev.earworm.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import timber.log.Timber

class FilterDialog(context: Context) : Dialog(context) {

  private lateinit var displaySongCheckbox: CheckBox
  private lateinit var displayAlbumCheckbox: CheckBox
  private lateinit var displayArtistCheckbox: CheckBox
  private lateinit var startDateDisplay: Chip
  private lateinit var endDateDisplay: Chip
  private var displaySong: Boolean = true
  private var displayAlbum: Boolean = true
  private var displayArtist: Boolean = true

  init {
    Timber.d("Log: FilterDialog Init: Started")
    setContentView(R.layout.dialog_filter)
    window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    bindViews()
    initCheckboxes()
  }

  private fun bindViews() {
    Timber.d("Log: bindViews: Started")

    this.startDateDisplay = findViewById(R.id.chip_filter_start)
    startDateDisplay.setOnClickListener {
      Timber.d("Log: startDateClickListener: Started")
      // TODO display date picker dialog
      Toast.makeText(context, "Start", Toast.LENGTH_SHORT).show()
    }

    this.endDateDisplay = findViewById(R.id.chip_filter_end)
    endDateDisplay.setOnClickListener {
      Timber.d("Log: endDateClickListener: Started")
      // TODO display date picker dialog
      Toast.makeText(context, "End", Toast.LENGTH_SHORT).show()
    }

    this.displaySongCheckbox = findViewById(R.id.chk_filter_song)
    displaySongCheckbox.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
      displaySong = isChecked
    }

    this.displayAlbumCheckbox = findViewById(R.id.chk_filter_album)
    displayAlbumCheckbox.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
      displayAlbum = isChecked
    }

    this.displayArtistCheckbox = findViewById(R.id.chk_filter_artist)
    displayArtistCheckbox.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
      displayArtist = isChecked
    }

    val submitButton: MaterialButton = findViewById(R.id.btn_filter_ok)
    submitButton.setOnClickListener {
      Timber.d("Log: submitButtonOnClickListener: Started")
      // TODO
      Toast.makeText(context, "Filter", Toast.LENGTH_SHORT).show()
      dismiss()
    }
  }

  private fun initCheckboxes() {
    Timber.d("Log: initCheckboxes: Started")
    displaySongCheckbox.isChecked = displaySong
    displayAlbumCheckbox.isChecked = displayAlbum
    displayArtistCheckbox.isChecked = displayArtist
  }
}