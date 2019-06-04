package app.marcdev.earworm.additem

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import app.marcdev.earworm.R
import app.marcdev.earworm.internal.base.EarwormBottomSheetDialogFragment
import app.marcdev.earworm.utils.*
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import timber.log.Timber

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
  private lateinit var datePickerDialog: Dialog
  private lateinit var datePicker: DatePicker
  private lateinit var datePickerOk: MaterialButton
  private lateinit var datePickerCancel: MaterialButton
  private lateinit var iconImageView: ImageView
  private lateinit var dateChip: Chip
  private lateinit var confirmDeleteDialog: Dialog
//  private lateinit var imageSelectorDialog: BottomSheetImagePicker
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

    datePickerDialog = Dialog(this.requireActivity())
    datePickerDialog.setContentView(R.layout.dialog_datepicker)
    datePickerDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    datePicker = datePickerDialog.findViewById(R.id.datepicker)

    datePickerOk = datePickerDialog.findViewById(R.id.btn_datepicker_ok)
    datePickerOk.setOnClickListener {
      viewModel.setDate(datePicker.dayOfMonth, datePicker.month, datePicker.year)
      datePickerDialog.dismiss()
    }

    datePickerCancel = datePickerDialog.findViewById(R.id.btn_datepicker_cancel)
    datePickerCancel.setOnClickListener { datePickerDialog.dismiss() }

    dateChip = view.findViewById(R.id.chip_add_item_date_display)
    dateChip.setOnClickListener(dateOnClickListener)

    iconImageView = view.findViewById(R.id.img_add_icon)
    iconImageView.setOnClickListener(iconOnClickListener)
    iconImageView.setOnLongClickListener { confirmDeleteDialog.show(); true }
    // Convert to dark mode if needed
    changeColorOfDrawable(requireContext(), iconImageView.drawable, false)

    initEditDialog()
  }

  private val dateOnClickListener = View.OnClickListener {
    datePicker.updateDate(viewModel.year, viewModel.month, viewModel.day)
    datePickerDialog.show()
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

  private fun initEditDialog() {
    // TODO convert to DialogFragment
    this.confirmDeleteDialog = Dialog(requireContext())
    confirmDeleteDialog.setContentView(R.layout.dialog_delete_image)
    confirmDeleteDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    val confirmDeleteButton = confirmDeleteDialog.findViewById<MaterialButton>(R.id.btn_delete_image_confirm)
    confirmDeleteButton.setOnClickListener(confirmDeleteOnClickListener)
    val cancelButton = confirmDeleteDialog.findViewById<MaterialButton>(R.id.btn_delete_image_cancel)
    cancelButton.setOnClickListener(cancelDeleteOnClickListener)
  }

  private val confirmDeleteOnClickListener = View.OnClickListener {
    viewModel.removeImage()
    confirmDeleteDialog.dismiss()
  }

  private val cancelDeleteOnClickListener = View.OnClickListener {
    confirmDeleteDialog.dismiss()
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
    viewModel.displayAdded.observe(this, Observer { value ->
      value?.let { show ->
        if(show)
          Toast.makeText(requireActivity(), resources.getString(R.string.item_added), Toast.LENGTH_SHORT).show()
      }
    })

    viewModel.displayEmpty.observe(this, Observer { value ->
      value?.let { show ->
        if(show)
          Toast.makeText(requireActivity(), resources.getString(R.string.empty), Toast.LENGTH_SHORT).show()
      }
    })

    viewModel.displayError.observe(this, Observer { value ->
      value?.let { show ->
        if(show)
          Toast.makeText(requireActivity(), resources.getString(R.string.error), Toast.LENGTH_SHORT).show()
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
        // TODO display IME
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
        changeColorOfImageViewDrawable(requireActivity(), songButton, true)
        changeColorOfImageViewDrawable(requireActivity(), albumButton, false)
        changeColorOfImageViewDrawable(requireActivity(), artistButton, false)
        primaryInput.hint = resources.getString(R.string.song_name)
        secondaryInput.hint = resources.getString(R.string.artist)
      }
      ALBUM -> {
        changeColorOfImageViewDrawable(requireActivity(), songButton, false)
        changeColorOfImageViewDrawable(requireActivity(), albumButton, true)
        changeColorOfImageViewDrawable(requireActivity(), artistButton, false)
        primaryInput.hint = resources.getString(R.string.album)
        secondaryInput.hint = resources.getString(R.string.artist)
      }
      ARTIST -> {
        changeColorOfImageViewDrawable(requireActivity(), songButton, false)
        changeColorOfImageViewDrawable(requireActivity(), albumButton, false)
        changeColorOfImageViewDrawable(requireActivity(), artistButton, true)
        primaryInput.hint = resources.getString(R.string.artist)
        secondaryInput.hint = resources.getString(R.string.genre)
      }
    }

    // TODO convert to switch preference that uses boolean
    if(PreferenceManager.getDefaultSharedPreferences(requireContext()).getString(PREF_CLEAR_INPUTS, resources.getString(R.string.yes)) == resources.getString(R.string.yes)) {
      primaryInput.setText("")
      secondaryInput.setText("")
      primaryInput.requestFocus()
    }
  }

  private fun displayImage(imagePath: String) {
    Timber.i("Log: displayImage: $imagePath")
    if(imagePath.isBlank()) {
      Glide.with(this)
        .load(resources.getDrawable(R.drawable.ic_add_a_photo_24px, null))
        .apply(RequestOptions().centerCrop())
        .apply(RequestOptions().error(resources.getDrawable(R.drawable.ic_error_24px, null)))
        .into(iconImageView)
    } else {
      Glide.with(this)
        .load(imagePath)
        .apply(RequestOptions().centerCrop())
        .apply(RequestOptions().error(resources.getDrawable(R.drawable.ic_error_24px, null)))
        .into(iconImageView)
    }
  }
}