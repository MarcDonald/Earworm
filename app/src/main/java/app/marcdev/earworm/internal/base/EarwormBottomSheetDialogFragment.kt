package app.marcdev.earworm.internal.base

import android.app.Dialog
import android.os.Bundle
import android.preference.PreferenceManager
import app.marcdev.earworm.R
import app.marcdev.earworm.internal.PREF_DARK_THEME
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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