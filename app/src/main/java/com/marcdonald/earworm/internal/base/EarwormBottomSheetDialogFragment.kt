package com.marcdonald.earworm.internal.base

import android.app.Dialog
import android.os.Bundle
import android.preference.PreferenceManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.marcdonald.earworm.R
import com.marcdonald.earworm.internal.PREF_DARK_THEME

abstract class EarwormBottomSheetDialogFragment : BottomSheetDialogFragment() {

  override fun getTheme(): Int {

    return if(PreferenceManager.getDefaultSharedPreferences(requireContext()).getBoolean(PREF_DARK_THEME, false))
      R.style.Theme_Earworm_BottomSheetDialog_Dark
    else
      R.style.Theme_Earworm_BottomSheetDialog_Light

  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return BottomSheetDialog(requireContext(), theme)
  }
}