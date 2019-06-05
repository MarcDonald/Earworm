package app.marcdev.earworm.internal.base

import android.app.Dialog
import android.os.Bundle
import android.preference.PreferenceManager
import app.marcdev.earworm.R
import app.marcdev.earworm.internal.PREF_THEME
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class EarwormBottomSheetDialogFragment : BottomSheetDialogFragment() {

  override fun getTheme(): Int {

    return if(PreferenceManager.getDefaultSharedPreferences(requireContext()).getString(PREF_THEME, resources.getString(R.string.light)) == resources.getString(R.string.light))
      R.style.Theme_Earworm_BottomSheetDialog_Light
    else
      R.style.Theme_Earworm_BottomSheetDialog_Dark

  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return BottomSheetDialog(requireContext(), theme)
  }
}