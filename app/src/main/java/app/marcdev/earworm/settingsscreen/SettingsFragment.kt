package app.marcdev.earworm.settingsscreen

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import app.marcdev.earworm.R

class SettingsFragment : PreferenceFragmentCompat() {

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.preferences, rootKey)
  }
}