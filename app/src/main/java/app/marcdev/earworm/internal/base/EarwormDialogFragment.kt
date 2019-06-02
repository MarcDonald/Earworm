package app.marcdev.earworm.internal.base

import androidx.fragment.app.DialogFragment
import app.marcdev.earworm.R

abstract class EarwormDialogFragment: DialogFragment() {
  override fun onStart() {
    super.onStart()
    requireDialog().window?.setBackgroundDrawableResource(R.drawable.rounded_dialog_background)
  }
}