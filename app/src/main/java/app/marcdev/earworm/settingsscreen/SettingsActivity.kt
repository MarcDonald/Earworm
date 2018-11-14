package app.marcdev.earworm.settingsscreen

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import app.marcdev.earworm.R
import app.marcdev.earworm.utils.DARK_THEME
import app.marcdev.earworm.utils.changeColorOfImageButtonDrawable
import app.marcdev.earworm.utils.getTheme
import app.marcdev.earworm.utils.setFragment
import timber.log.Timber

class SettingsActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    Timber.v("Log: onCreate: Started")

    if(getTheme(applicationContext) == DARK_THEME) {
      Timber.v("Log: onCreate: Is dark mode")
      setTheme(R.style.Earworm_DarkTheme)
    } else {
      Timber.v("Log: onCreate: Is not dark mode")
      setTheme(R.style.Earworm_LightTheme)
    }

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    bindViews()

    setFragment(SettingsFragment(), supportFragmentManager, R.id.scroll_settings)
  }

  private fun bindViews() {
    Timber.v("Log: bindViews: Started")
    val backButton = findViewById<ImageButton>(R.id.img_backFromSettings)
    backButton.setOnClickListener(backOnClickListener)

    if(getTheme(applicationContext) == DARK_THEME) {
      Timber.v("Log: bindViews: Converting to dark mode")
      changeColorOfImageButtonDrawable(applicationContext, backButton, false)
    }
  }

  private val backOnClickListener = View.OnClickListener {
    Timber.d("Log: BackClick: Started")
    this.finish()
  }
}