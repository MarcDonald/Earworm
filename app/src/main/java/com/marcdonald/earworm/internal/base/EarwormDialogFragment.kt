package com.marcdonald.earworm.internal.base

import androidx.fragment.app.DialogFragment
import com.marcdonald.earworm.R

abstract class EarwormDialogFragment : DialogFragment() {
  override fun onStart() {
    super.onStart()
    requireDialog().window?.setBackgroundDrawableResource(R.drawable.rounded_dialog_background)
  }
}