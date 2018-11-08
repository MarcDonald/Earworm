package app.marcdev.earworm.settingsscreen

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import app.marcdev.earworm.R
import app.marcdev.earworm.utils.setFragment
import timber.log.Timber

class SettingsActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Timber.d("Log: onCreate: Started")
    setContentView(R.layout.activity_settings)
    bindViews()

    setFragment(SettingsFragment(), supportFragmentManager, R.id.scroll_settings)
  }

  private fun bindViews() {
    val backButton = findViewById<ImageButton>(R.id.img_backFromSettings)
    backButton.setOnClickListener(backOnClickListener)
  }

  private val backOnClickListener = View.OnClickListener {
    Timber.d("Log: BackClick: Started")
    this.finish()
  }
}