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
import app.marcdev.earworm.utils.changeColorOfDrawable
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat() {

  private lateinit var prefs: SharedPreferences

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    Timber.v("Log: onCreatePreferences: Started")
    setPreferencesFromResource(R.xml.preferences, rootKey)
    this.prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity().applicationContext)

    val themePref = findPreference(PREF_THEME)
    themePref.onPreferenceChangeListener = themeChangeListener
    matchSummaryToSelection(themePref, PreferenceManager.getDefaultSharedPreferences(themePref.context).getString(themePref.key, "")!!)
    changeColorOfDrawable(requireContext(), themePref.icon, false)

    val clearInputsPref = findPreference(PREF_CLEAR_INPUTS)
    clearInputsPref.onPreferenceChangeListener = clearInputsChangeListener
    matchSummaryToSelection(clearInputsPref, PreferenceManager.getDefaultSharedPreferences(clearInputsPref.context).getString(clearInputsPref.key, "")!!)
    changeColorOfDrawable(requireContext(), clearInputsPref.icon, false)

    val tipsPref = findPreference(PREF_SHOW_TIPS)
    tipsPref.onPreferenceClickListener = resetTipsListener
    changeColorOfDrawable(requireContext(), tipsPref.icon, false)

    val versionPref = findPreference(PREF_BUILD_NUMBER)
    versionPref.summary = BuildConfig.VERSION_NAME
    versionPref.onPreferenceClickListener = versionClickListener
    changeColorOfDrawable(requireContext(), versionPref.icon, false)

    val licensesPref = findPreference(PREF_LICENSES)
    licensesPref.onPreferenceClickListener = licensesOnClickListener
    changeColorOfDrawable(requireContext(), licensesPref.icon, false)

    val githubPref = findPreference(PREF_GITHUB)
    githubPref.onPreferenceClickListener = githubOnClickListener
    changeColorOfDrawable(requireContext(), githubPref.icon, false)
  }

  private val themeChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
    Timber.d("Log: themeChangeListener: Theme changed to $newValue")
    requireActivity().recreate()
    matchSummaryToSelection(preference, newValue.toString())
    true
  }

  private val clearInputsChangeListener = Preference.OnPreferenceChangeListener { preference, newValue ->
    Timber.d("Log: clearInputsChangeListener: Value changed to $newValue")
    matchSummaryToSelection(preference, newValue.toString())
    true
  }

  private val resetTipsListener = Preference.OnPreferenceClickListener {
    Timber.d("Log: ResetTipsClick: Clicked")
    prefs.edit().putBoolean(PREF_SHOW_TIPS, true).apply()
    Toast.makeText(requireContext(), resources.getString(R.string.reset_tips_confirmation), Toast.LENGTH_LONG).show()
    return@OnPreferenceClickListener true
  }

  private val versionClickListener = Preference.OnPreferenceClickListener {
    Timber.d("Log: versionClick: Started")
    val versionCodeString = resources.getString(R.string.build_code)
    Toast.makeText(requireContext(), "$versionCodeString: ${BuildConfig.VERSION_CODE}", Toast.LENGTH_SHORT).show()
    true
  }

  private val licensesOnClickListener = Preference.OnPreferenceClickListener {
    Timber.d("Log: licensesClick: Started")
    val intent = Intent(requireContext(), LicensesActivity::class.java)
    startActivity(intent)
    true
  }

  private val githubOnClickListener = Preference.OnPreferenceClickListener {
    Timber.d("Log: githubClick: Started")
    val uriUrl = Uri.parse("https://github.com/MarcDonald/Earworm")
    val launchBrowser = Intent(Intent.ACTION_VIEW)
    launchBrowser.data = uriUrl
    startActivity(launchBrowser)
    true
  }

  private fun matchSummaryToSelection(preference: Preference, value: String) {
    Timber.d("Log: themeOnChangeListener: Started")
    Timber.d("Log: themeOnChangeListener: Value = $value")

    if(preference is ListPreference) {
      val index = preference.findIndexOfValue(value)

      preference.setSummary(
        if(index >= 0) {
          Timber.d("Log: BindPreferenceSummaryToValue: Setting summary to ${preference.entries[index]}")
          preference.entries[index]
        } else {
          Timber.w("Log: BindPreferenceSummaryToValue: Index < 0")
          null
        })

    } else {
      Timber.d("Log: BindPreferenceSummaryToValue: Setting summary to $value")
      preference.summary = value
    }
  }
}