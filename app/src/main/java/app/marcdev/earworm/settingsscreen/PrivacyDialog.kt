package app.marcdev.earworm.settingsscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.marcdev.earworm.R
import app.marcdev.earworm.internal.base.EarwormDialogFragment
import com.google.android.material.button.MaterialButton

class PrivacyDialog : EarwormDialogFragment() {
  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val view = inflater.inflate(R.layout.dialog_privacy, container, false)
    bindViews(view)
    return view
  }

  private fun bindViews(view: View) {
    view.findViewById<MaterialButton>(R.id.btn_privacy_dialog_dismiss).setOnClickListener {
      dismiss()
    }
  }
}