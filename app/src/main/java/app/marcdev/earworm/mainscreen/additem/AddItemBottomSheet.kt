package app.marcdev.earworm.mainscreen.additem

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import app.marcdev.earworm.EarwormUtils
import app.marcdev.earworm.R
import app.marcdev.earworm.uicomponents.RoundedBottomDialogFragment
import com.google.android.material.button.MaterialButton
import timber.log.Timber

class AddItemBottomSheet : RoundedBottomDialogFragment(), AddItemView {

  private lateinit var saveButton: MaterialButton
  private lateinit var primaryInput: EditText
  private lateinit var secondaryInput: EditText
  private lateinit var dateButton: ImageButton
  private lateinit var songButton: ImageButton
  private lateinit var albumButton: ImageButton
  private lateinit var artistButton: ImageButton
  private lateinit var presenter: AddItemPresenter
  private var type: Int = 0
  private var recyclerUpdateView: RecyclerUpdateView? = null

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.dialog_add_item_song, container, false)
    presenter = AddItemPresenterImpl(this, activity!!.applicationContext)
    bindViews(view)
    setDefaultType()
    return view
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
    presenter.addItem(primaryInput.text.toString(), secondaryInput.text.toString(), type)
  }

  private val dateOnClickListener = View.OnClickListener {
    Timber.d("Log: DateClick: Clicked")
    // TODO
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

  private fun setDefaultType() {
    Timber.d("Log: setDefaultType: Started")
    type = EarwormUtils.SONG
    changeColorOfImageButton(songButton, true)
    changeColorOfImageButton(albumButton, false)
    changeColorOfImageButton(artistButton, false)
  }

  private fun activateButton(button: ImageButton) {
    Timber.d("Log: activateButton: Started")
    var doUpdate = true

    if(type == EarwormUtils.SONG && button == songButton
       || type == EarwormUtils.ALBUM && button == albumButton
       || type == EarwormUtils.ARTIST && button == artistButton
    ) {
      Timber.d("Log: activateButton: No need to update")
      doUpdate = false
    }

    if(doUpdate) {
      Timber.d("Log: activateButton: Activating button $button")
      when(type) {
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
          type = EarwormUtils.SONG
          primaryInput.hint = resources.getString(R.string.song_name)
          secondaryInput.hint = resources.getString(R.string.artist)
        }

        albumButton -> {
          changeColorOfImageButton(albumButton, true)
          type = EarwormUtils.ALBUM
          primaryInput.hint = resources.getString(R.string.album)
          secondaryInput.hint = resources.getString(R.string.artist)
        }

        artistButton -> {
          changeColorOfImageButton(artistButton, true)
          type = EarwormUtils.ARTIST
          primaryInput.hint = resources.getString(R.string.artist)
          secondaryInput.hint = resources.getString(R.string.genre)
        }
      }
      primaryInput.setText("")
      secondaryInput.setText("")
      primaryInput.requestFocus()
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