package app.marcdev.earworm.mainscreen.additem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import app.marcdev.earworm.EarwormUtils
import app.marcdev.earworm.R
import app.marcdev.earworm.uicomponents.RoundedBottomDialogFragment
import com.google.android.material.button.MaterialButton
import timber.log.Timber

class AddItemBottomSheet : RoundedBottomDialogFragment() {

  private lateinit var saveButton: MaterialButton
  private lateinit var primaryInput: EditText
  private lateinit var secondaryInput: EditText
  private lateinit var dateButton: ImageButton
  private lateinit var songButton: ImageButton
  private lateinit var albumButton: ImageButton
  private lateinit var artistButton: ImageButton
  private var choice: Int = 0

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.dialog_add_item_song, container, false)
    bindViews(view)
    setDefaultChoice()
    return view
  }

  private fun bindViews(view: View) {
    this.saveButton = view.findViewById(R.id.btn_add_item_save)
    saveButton.setOnClickListener(saveOnClickListener)

    this.primaryInput = view.findViewById(R.id.edt_item_primary_input)
    this.secondaryInput = view.findViewById(R.id.edt_item_secondary_input)

    this.dateButton = view.findViewById(R.id.btn_add_item_date)
    dateButton.setOnClickListener(dateOnClickListener)

    this.songButton = view.findViewById(R.id.btn_add_item_song_choice)
    songButton.setOnClickListener(songOnClickListener)

    this.albumButton = view.findViewById(R.id.btn_add_item_album_choice)
    albumButton.setOnClickListener(albumOnClickListener)

    this.artistButton = view.findViewById(R.id.btn_add_item_artist_choice)
    artistButton.setOnClickListener(artistOnClickListener)
  }

  private val saveOnClickListener = View.OnClickListener {
    Timber.d("Log: SaveClick: Clicked")
  }

  private val dateOnClickListener = View.OnClickListener {
    Timber.d("Log: DateClick: Clicked")
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

  private fun setDefaultChoice() {
    Timber.d("Log: setDefaultChoice: Started")
    choice = EarwormUtils.SONG
    changeColorOfImageButton(songButton, true)
    changeColorOfImageButton(albumButton, false)
    changeColorOfImageButton(artistButton, false)
  }

  private fun activateButton(button: ImageButton) {
    Timber.d("Log: activateButton: Started")
    var doUpdate = true

    if(choice == EarwormUtils.SONG && button == songButton
       || choice == EarwormUtils.ALBUM && button == albumButton
       || choice == EarwormUtils.ARTIST && button == artistButton
    ) {
      Timber.d("Log: activateButton: No need to update")
      doUpdate = false
    }

    if(doUpdate) {
      Timber.d("Log: activateButton: Activating button $button")
      when(choice) {
        EarwormUtils.SONG -> {
          changeColorOfImageButton(songButton, false)
        }
        EarwormUtils.ALBUM -> {
          changeColorOfImageButton(albumButton, false)
        }
        EarwormUtils.ARTIST -> {
          changeColorOfImageButton(artistButton, false)
        }
      }

      when(button) {
        songButton -> {
          changeColorOfImageButton(songButton, true)
          choice = EarwormUtils.SONG
          primaryInput.hint = resources.getString(R.string.song_name)
          secondaryInput.hint = resources.getString(R.string.artist)
        }

        albumButton -> {
          changeColorOfImageButton(albumButton, true)
          choice = EarwormUtils.ALBUM
          primaryInput.hint = resources.getString(R.string.album)
          secondaryInput.hint = resources.getString(R.string.artist)
        }

        artistButton -> {
          changeColorOfImageButton(artistButton, true)
          choice = EarwormUtils.ARTIST
          primaryInput.hint = resources.getString(R.string.artist)
          secondaryInput.hint = resources.getString(R.string.genre)
        }
      }
      primaryInput.setText("")
      secondaryInput.setText("")
    }
  }

  private fun changeColorOfImageButton(button: ImageButton, isActivated: Boolean) {
    Timber.v("Log: changeColorOfImageButton: Started")

    if(isActivated) {
      DrawableCompat.setTint(button.drawable, ContextCompat.getColor(activity!!.applicationContext, R.color.colorAccent))
    } else {
      DrawableCompat.setTint(button.drawable, ContextCompat.getColor(activity!!.applicationContext, R.color.black))
    }
  }
}