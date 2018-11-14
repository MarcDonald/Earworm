package app.marcdev.earworm.uicomponents

import android.app.Dialog
import android.os.Bundle
import app.marcdev.earworm.R
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

open class RoundedBottomDialogFragment : BottomSheetDialogFragment() {

  override fun getTheme(): Int {
    return R.style.Earworm_BottomSheetDialogTheme
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    return BottomSheetDialog(requireContext(), theme)
  }
}