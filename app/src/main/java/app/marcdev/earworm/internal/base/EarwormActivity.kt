package app.marcdev.earworm.internal.base

import android.os.Bundle
import android.preference.PreferenceManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import app.marcdev.earworm.R
import app.marcdev.earworm.internal.PREF_DARK_THEME

abstract class EarwormActivity : AppCompatActivity() {
  private var isDarkTheme = false

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    updateTheme()
  }

  override fun onResume() {
    super.onResume()
    // Checks if the theme was changed while it was paused and then sees if the current theme of the activity matches
    val isDarkThemeNow = PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean(PREF_DARK_THEME, false)
    if(isDarkTheme != isDarkThemeNow) {
      updateTheme()
      recreate()
    }
  }

  private fun updateTheme() {
    val darkPref = PreferenceManager.getDefaultSharedPreferences(applicationContext).getBoolean(PREF_DARK_THEME, false)
    isDarkTheme = if(darkPref) {
      setTheme(R.style.Earworm_DarkTheme)
      true
    } else {
      setTheme(R.style.Earworm_LightTheme)
      false
    }
  }

  /**
   * Replaces a fragment in a frame with another fragment
   * @param fragment The fragment to display
   * @param fragmentManager The Fragment Manager
   * @param frameId The ID of the frame to display the new fragment in
   */
  protected fun setFragment(fragment: Fragment, fragmentManager: FragmentManager, frameId: Int) {
    val fragmentTransaction = fragmentManager.beginTransaction()
    fragmentTransaction.replace(frameId, fragment)
    fragmentTransaction.commit()
  }
}