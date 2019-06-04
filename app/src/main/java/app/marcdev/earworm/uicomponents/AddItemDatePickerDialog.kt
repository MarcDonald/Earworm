package app.marcdev.earworm.uicomponents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import app.marcdev.earworm.R
import app.marcdev.earworm.internal.base.EarwormDialogFragment
import com.google.android.material.button.MaterialButton
import java.util.*

class AddItemDatePickerDialog(private val calendarArg: Calendar?,
                              private val okClick: (Int, Int, Int) -> Unit)
  : EarwormDialogFragment() {

  // <editor-fold desc="UI Components">
  private lateinit var datePicker: DatePicker
  private lateinit var cancelButton: MaterialButton
  private lateinit var okButton: MaterialButton
  // </editor-fold>

  private val day: Int
    get() = datePicker.dayOfMonth
  private val month: Int
    get() = datePicker.month
  private val year: Int
    get() = datePicker.year

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.dialog_datepicker, container, false)
    bindViews(view)
    setDate()
    return view
  }

  private fun bindViews(view: View) {
    datePicker = view.findViewById(R.id.datepicker)
    cancelButton = view.findViewById(R.id.btn_datepicker_cancel)
    cancelButton.setOnClickListener { dismiss() }
    okButton = view.findViewById(R.id.btn_datepicker_ok)
    okButton.setOnClickListener {
      okClick(day, month, year)
    }
  }

  private fun setDate() {
    calendarArg?.let { calendar ->
      datePicker.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
    }
  }
}