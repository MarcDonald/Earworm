package com.marcdonald.earworm.additem

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.marcdonald.earworm.R
import com.marcdonald.earworm.internal.ALBUM
import com.marcdonald.earworm.internal.ARTIST
import com.marcdonald.earworm.internal.PREF_CLEAR_INPUTS
import com.marcdonald.earworm.internal.SONG
import com.marcdonald.earworm.internal.base.EarwormBottomSheetDialogFragment
import com.marcdonald.earworm.uicomponents.AddItemDatePickerDialog
import com.marcdonald.earworm.uicomponents.BinaryOptionDialog
import com.marcdonald.earworm.utils.ThemeUtils
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance

class AddItemBottomSheet : EarwormBottomSheetDialogFragment(), KodeinAware {
  override val kodein: Kodein by closestKodein()

  // <editor-fold desc="View Model">
  private val viewModelFactory: AddItemViewModelFactory by instance()
  private lateinit var viewModel: AddItemViewModel
  // </editor-fold>

  // <editor-fold desc="UI Components">
  private lateinit var saveButton: MaterialButton
  private lateinit var primaryInput: EditText
  private lateinit var secondaryInput: EditText
  private lateinit var songButton: ImageView
  private lateinit var albumButton: ImageView
  private lateinit var artistButton: ImageView
  private lateinit var datePickerDialog: AddItemDatePickerDialog
  private lateinit var iconImageView: ImageView
  private lateinit var dateChip: Chip
  private lateinit var confirmImageDeleteDialog: BinaryOptionDialog
  // </editor-fold>

  // <editor-fold desc="Other">
  private val themeUtils: ThemeUtils by instance()
  // </editor-fold>

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    viewModel = ViewModelProviders.of(this, viewModelFactory).get(AddItemViewModel::class.java)
  }

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.dialog_add_item, container, false)
    bindViews(view)
    setupObservers()

    var itemId = -1
    if(arguments != null) {
      itemId = requireArguments().getInt("item_id", -1)
    }
    viewModel.passArguments(itemId)

    return view
  }

  private fun bindViews(view: View) {
    saveButton = view.findViewById(R.id.btn_add_item_save)
    saveButton.setOnClickListener {
      viewModel.save(primaryInput.text.toString(), secondaryInput.text.toString())
    }

    primaryInput = view.findViewById(R.id.edt_item_primary_input)
    secondaryInput = view.findViewById(R.id.edt_item_secondary_input)

    songButton = view.findViewById(R.id.btn_add_item_song_choice)
    songButton.setOnClickListener { viewModel.setType(SONG) }

    albumButton = view.findViewById(R.id.btn_add_item_album_choice)
    albumButton.setOnClickListener { viewModel.setType(ALBUM) }

    artistButton = view.findViewById(R.id.btn_add_item_artist_choice)
    artistButton.setOnClickListener { viewModel.setType(ARTIST) }

    datePickerDialog = AddItemDatePickerDialog(viewModel.date) { day, month, year ->
      viewModel.setDate(day, month, year)
      datePickerDialog.dismiss()
    }

    dateChip = view.findViewById(R.id.chip_add_item_date_display)
    dateChip.setOnClickListener {
      datePickerDialog.show(requireFragmentManager(), "Add Item Date Picker Dialog")
    }

    iconImageView = view.findViewById(R.id.img_add_icon)
    iconImageView.setOnClickListener(iconOnClickListener)
    iconImageView.setOnLongClickListener { confirmImageDeleteDialog.show(requireFragmentManager(), "Confirm Image Delete Dialog"); true }

    initImageDeleteDialog()
  }

  private val iconOnClickListener = View.OnClickListener {
    if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      askForStoragePermissions()
    } else {
      FilePickerBuilder.instance.setMaxCount(1)
        .setActivityTheme(R.style.Earworm_DarkTheme)
        .setActivityTitle(resources.getString(R.string.choose_an_image))
        .pickPhoto(this)
    }
  }

  private fun initImageDeleteDialog() {
    val dialogBuilder = BinaryOptionDialog.Builder()
    dialogBuilder
      .setTitle(resources.getString(R.string.confirm_image_deletion_title))
      .setMessage(resources.getString(R.string.confirm_image_deletion_message))
      .setPositiveButton(resources.getString(R.string.cancel), {}, true)
      .setNegativeButton(resources.getString(R.string.delete), {
        viewModel.removeImage()
      }, true)
    confirmImageDeleteDialog = dialogBuilder.build()
  }

  private fun askForStoragePermissions() {
    ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)

    if(requestCode == FilePickerConst.REQUEST_CODE_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
      val photoPathArray = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA)
      val photoPath = photoPathArray[0]
      viewModel.setNewImage(photoPath)
    }
  }

  private fun setupObservers() {
    viewModel.displayEmpty.observe(this, Observer { value ->
      value?.let { show ->
        if(show) {
          if(primaryInput.text.isBlank())
            primaryInput.error = resources.getString(R.string.empty)
          if(secondaryInput.text.isBlank())
            secondaryInput.error = resources.getString(R.string.empty)
        }
      }
    })

    viewModel.selectedType.observe(this, Observer { value ->
      value?.let { type ->
        setTypeSelected(type)
      }
    })

    viewModel.dateDisplay.observe(this, Observer { value ->
      value?.let { dateToDisplay ->
        if(dateToDisplay.isBlank())
          dateChip.text = resources.getString(R.string.today)
        else
          dateChip.text = dateToDisplay
      }
    })

    viewModel.primaryInputDisplay.observe(this, Observer { value ->
      value?.let { input ->
        primaryInput.setText(input)
        primaryInput.setSelection(input.length)
      }
    })

    viewModel.secondaryInputDisplay.observe(this, Observer { value ->
      value?.let { input ->
        secondaryInput.setText(input)
      }
    })

    viewModel.displayImage.observe(this, Observer { value ->
      value?.let { filePath ->
        displayImage(filePath)
      }
    })

    viewModel.dismiss.observe(this, Observer { value ->
      value?.let { dismiss ->
        if(dismiss)
          dismiss()
      }
    })
  }

  private fun setTypeSelected(type: Int) {
    when(type) {
      SONG -> {
        changeColorOfImageViewDrawable(songButton, true)
        changeColorOfImageViewDrawable(albumButton, false)
        changeColorOfImageViewDrawable(artistButton, false)
        primaryInput.hint = resources.getString(R.string.song_name)
        secondaryInput.hint = resources.getString(R.string.artist)
      }
      ALBUM -> {
        changeColorOfImageViewDrawable(songButton, false)
        changeColorOfImageViewDrawable(albumButton, true)
        changeColorOfImageViewDrawable(artistButton, false)
        primaryInput.hint = resources.getString(R.string.album)
        secondaryInput.hint = resources.getString(R.string.artist)
      }
      ARTIST -> {
        changeColorOfImageViewDrawable(songButton, false)
        changeColorOfImageViewDrawable(albumButton, false)
        changeColorOfImageViewDrawable(artistButton, true)
        primaryInput.hint = resources.getString(R.string.artist)
        secondaryInput.hint = resources.getString(R.string.genre)
      }
    }

    if(PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean(PREF_CLEAR_INPUTS, true)) {
      primaryInput.setText("")
      secondaryInput.setText("")
      primaryInput.requestFocus()
    }
  }

  private fun displayImage(imagePath: String) {
    if(imagePath.isBlank()) {
      Glide.with(this)
        .load(resources.getDrawable(R.drawable.ic_add_a_photo_24px, requireActivity().theme))
        .apply(RequestOptions().centerCrop())
        .apply(RequestOptions().error(resources.getDrawable(R.drawable.ic_error_24px, requireActivity().theme)))
        .into(iconImageView)
    } else {
      Glide.with(this)
        .load(imagePath)
        .apply(RequestOptions().centerCrop())
        .apply(RequestOptions().error(resources.getDrawable(R.drawable.ic_error_24px, requireActivity().theme)))
        .into(iconImageView)
    }
  }

  private fun changeColorOfImageViewDrawable(button: ImageView, isActivated: Boolean) {
    when {
      isActivated -> button.setColorFilter(themeUtils.getAccentColor())
      else -> button.clearColorFilter()
    }
  }
}