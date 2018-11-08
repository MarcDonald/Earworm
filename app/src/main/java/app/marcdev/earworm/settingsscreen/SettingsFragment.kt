package app.marcdev.earworm.settingsscreen

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceManager
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.marcdev.earworm.R

class SettingsFragment : PreferenceFragmentCompat() {

  private lateinit var prefs: SharedPreferences

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.preferences, rootKey)
    this.prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity().applicationContext)

    bindPreferenceSummaryToValue(findPreference("pref_theme"))
    findPreference("pref_show_tips").onPreferenceClickListener = resetTipsListener
  }

  private fun bindPreferenceSummaryToValue(preference: Preference) {
    preference.onPreferenceChangeListener = bindPreferenceSummaryToValueListener

    bindPreferenceSummaryToValueListener.onPreferenceChange(preference,
      PreferenceManager
        .getDefaultSharedPreferences(preference.context)
        .getString(preference.key, ""))
  }

  private val resetTipsListener = Preference.OnPreferenceClickListener {
    prefs.edit().putBoolean("pref_show_tips", true).apply()
    return@OnPreferenceClickListener true
  }

  private val bindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->
    val stringValue = value.toString()

    if(preference is ListPreference) {
      val index = preference.findIndexOfValue(stringValue)

      preference.setSummary(
        if(index >= 0)
          preference.entries[index]
        else
          null)

    } else {
      preference.summary = stringValue
    }
    true
  }
}