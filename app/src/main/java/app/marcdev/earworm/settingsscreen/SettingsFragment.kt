package app.marcdev.earworm.settingsscreen

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.preference.ListPreference
import android.preference.PreferenceManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import app.marcdev.earworm.BuildConfig
import app.marcdev.earworm.R
import app.marcdev.earworm.internal.*
import app.marcdev.earworm.settingsscreen.backupdialog.BackupDialog
import app.marcdev.earworm.settingsscreen.restoredialog.RestoreDialog
import droidninja.filepicker.FilePickerBuilder
import droidninja.filepicker.FilePickerConst
import timber.log.Timber

class SettingsFragment : PreferenceFragmentCompat() {

  private lateinit var prefs: SharedPreferences

  override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
    setPreferencesFromResource(R.xml.preferences, rootKey)
    this.prefs = PreferenceManager.getDefaultSharedPreferences(requireActivity().applicationContext)

    val darkTheme = findPreference(PREF_DARK_THEME)
    darkTheme.onPreferenceChangeListener = themeChangeListener

    val tipsPref = findPreference(PREF_SHOW_TIPS)
    tipsPref.onPreferenceClickListener = resetTipsListener

    val versionPref = findPreference(PREF_BUILD_NUMBER)
    versionPref.summary = BuildConfig.VERSION_NAME
    versionPref.onPreferenceClickListener = versionClickListener

    val licensesPref = findPreference(PREF_LICENSES)
    licensesPref.onPreferenceClickListener = licensesOnClickListener

    val githubPref = findPreference(PREF_GITHUB)
    githubPref.onPreferenceClickListener = Preference.OnPreferenceClickListener { launchURL("https://github.com/MarcDonald/Earworm"); true }

    val authorPref = findPreference(PREF_AUTHOR)
    authorPref.onPreferenceClickListener = Preference.OnPreferenceClickListener { launchURL("https://github.com/MarcDonald"); true }

    val backup = findPreference(PREF_BACKUP)
    backup.onPreferenceClickListener = backupClickListener

    val restore = findPreference(PREF_RESTORE)
    restore.onPreferenceClickListener = restoreClickListener
  }

  private val themeChangeListener = Preference.OnPreferenceChangeListener { _, _ ->
    requireActivity().recreate()
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

  private val backupClickListener = Preference.OnPreferenceClickListener {
    if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
    } else {
      val dialog = BackupDialog()
      dialog.show(requireFragmentManager(), "Backup Dialog")
    }
    true
  }

  private val restoreClickListener = Preference.OnPreferenceClickListener {
    if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
    } else {
      FilePickerBuilder.instance
        .setMaxCount(1)
        .setActivityTheme(R.style.Earworm_DarkTheme)
        .setActivityTitle(resources.getString(R.string.restore_title))
        .enableDocSupport(false)
        .addFileSupport(resources.getString(R.string.backup_file), Array(1) { ".earworm" })
        .pickFile(this, CHOOSE_RESTORE_FILE_REQUEST_CODE)
    }
    true
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    if(requestCode == CHOOSE_RESTORE_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      if(data != null) {
        val filePathArray = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS)
        val filePath = filePathArray[0]
        if(filePath != null)
          displayRestoreDialog(filePath)
      }
    } else
      super.onActivityResult(requestCode, resultCode, data)
  }

  private fun displayRestoreDialog(path: String) {
    val dialog = RestoreDialog()
    val bundle = Bundle()
    bundle.putString(RESTORE_FILE_PATH_KEY, path)
    dialog.arguments = bundle
    dialog.show(requireFragmentManager(), "Restore Dialog")
  }

  private fun launchURL(url: String) {
    val uriUrl = Uri.parse(url)
    val launchBrowser = Intent(Intent.ACTION_VIEW)
    launchBrowser.data = uriUrl
    startActivity(launchBrowser)
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