package app.marcdev.earworm.settingsscreen

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceManager
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.marcdev.earworm.R
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat() {

  private lateinit var prefs: SharedPreferences

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    Timber.v("Log: onCreatePreferences: Started")
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
    Timber.d("Log: ResetTipsClick: Clicked")
    prefs.edit().putBoolean("pref_show_tips", true).apply()
    return@OnPreferenceClickListener true
  }

  private val bindPreferenceSummaryToValueListener = Preference.OnPreferenceChangeListener { preference, value ->
    Timber.d("Log: BindPreferenceSummaryToValue: Started")
    val stringValue = value.toString()
    Timber.d("Log: BindPreferenceSummaryToValue: Value = $stringValue")

    if(preference is ListPreference) {
      val index = preference.findIndexOfValue(stringValue)

      preference.setSummary(
        if(index >= 0) {
          Timber.d("Log: BindPreferenceSummaryToValue: Setting summary to ${preference.entries[index]}")
          preference.entries[index]
        } else {
          Timber.w("Log: BindPreferenceSummaryToValue: Index < 0")
          null
        })

    } else {
      Timber.d("Log: BindPreferenceSummaryToValue: Setting summary to $stringValue")
      preference.summary = stringValue
    }
    true
  }
}