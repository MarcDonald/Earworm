package app.marcdev.earworm.settingsscreen

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import app.marcdev.earworm.R
import app.marcdev.earworm.internal.DARK_THEME
import app.marcdev.earworm.internal.base.EarwormActivity
import app.marcdev.earworm.utils.changeColorOfImageViewDrawable
import app.marcdev.earworm.utils.getTheme

class SettingsActivity : EarwormActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    if(getTheme(applicationContext) == DARK_THEME) {
      setTheme(R.style.Earworm_DarkTheme)
    } else {
      setTheme(R.style.Earworm_LightTheme)
    }

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_settings)
    bindViews()

    setFragment(SettingsFragment(), supportFragmentManager, R.id.scroll_settings)
  }

  private fun bindViews() {
    val backButton = findViewById<ImageView>(R.id.img_backFromSettings)
    backButton.setOnClickListener(backOnClickListener)

    if(getTheme(applicationContext) == DARK_THEME) {
      changeColorOfImageViewDrawable(applicationContext, backButton, false)
    }
  }

  private val backOnClickListener = View.OnClickListener {
    this.finish()
  }
}