package app.marcdev.earworm.settingsscreen

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.marcdev.earworm.BuildConfig
import app.marcdev.earworm.R
import app.marcdev.earworm.internal.*
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat() {

  private lateinit var prefs: SharedPreferences

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.preferences, rootKey)
    this.prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity().applicationContext)

    val darkTheme = findPreference(PREF_DARK_THEME)
    darkTheme.onPreferenceChangeListener = themeChangeListener

    val clearInputsPref = findPreference(PREF_CLEAR_INPUTS)
    clearInputsPref.onPreferenceChangeListener = clearInputsChangeListener
    matchSummaryToSelection(clearInputsPref, PreferenceManager.getDefaultSharedPreferences(clearInputsPref.context).getString(clearInputsPref.key, "")!!)

    val tipsPref = findPreference(PREF_SHOW_TIPS)
    tipsPref.onPreferenceClickListener = resetTipsListener

    val versionPref = findPreference(PREF_BUILD_NUMBER)
    versionPref.summary = BuildConfig.VERSION_NAME
    versionPref.onPreferenceClickListener = versionClickListener

    val licensesPref = findPreference(PREF_LICENSES)
    licensesPref.onPreferenceClickListener = licensesOnClickListener

    val githubPref = findPreference(PREF_GITHUB)
    githubPref.onPreferenceClickListener = githubOnClickListener
  }

  private val themeChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
    requireActivity().recreate()
    true
  }

  private val clearInputsChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
    matchSummaryToSelection(preference, newValue.toString())
    true
  }

  private val resetTipsListener = Preference.OnPreferenceClickListener {
    prefs.edit().putBoolean(PREF_SHOW_TIPS, true).apply()
    Toast.makeText(requireContext(), resources.getString(R.string.reset_tips_confirmation), Toast.LENGTH_LONG).show()
    return@OnPreferenceClickListener true
  }

  private val versionClickListener = Preference.OnPreferenceClickListener {
    val versionCodeString = resources.getString(R.string.build_code)
    Toast.makeText(requireContext(), "$versionCodeString: ${BuildConfig.VERSION_CODE}", Toast.LENGTH_SHORT).show()
    true
  }

  private val licensesOnClickListener = Preference.OnPreferenceClickListener {
    val intent = Intent(requireContext(), LicensesActivity::class.java)
    startActivity(intent)
    true
  }

  private val githubOnClickListener = Preference.OnPreferenceClickListener {
    val uriUrl = Uri.parse("https://github.com/MarcDonald/Earworm")
    val launchBrowser = Intent(Intent.ACTION_VIEW)
    launchBrowser.data = uriUrl
    startActivity(launchBrowser)
    true
  }

  private fun matchSummaryToSelection(preference: Preference, value: String) {
    if(preference is ListPreference) {
      val index = preference.findIndexOfValue(value)

      preference.setSummary(
        if(index >= 0) {
          preference.entries[index]
        } else {
          Timber.w("Log: BindPreferenceSummaryToValue: Index < 0")
          null
        })

    } else {
      preference.summary = value
    }
  }
}