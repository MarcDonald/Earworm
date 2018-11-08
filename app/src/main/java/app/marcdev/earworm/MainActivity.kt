package app.marcdev.earworm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import app.marcdev.earworm.mainscreen.MainFragmentViewImpl
import app.marcdev.earworm.utils.isDarkMode
import app.marcdev.earworm.utils.setFragment
import timber.log.Timber

class MainActivity : AppCompatActivity() {

  private lateinit var mainFrame: CoordinatorLayout
  private var activityInDarkMode: Boolean = false

  override fun onCreate(savedInstanceState: Bundle?) {
    Timber.d("Log: onCreate: Started")

    /* Theme changes must be done before super.onCreate otherwise it will be overriden with the value
      in the manifest */
    if(isDarkMode(applicationContext)) {
      Timber.v("Log: onCreate: Is dark mode")
      setTheme(R.style.Earworm_DarkTheme)
      activityInDarkMode = true
    } else {
      Timber.v("Log: onCreate: Is not dark mode")
      setTheme(R.style.Earworm_LightTheme)
      activityInDarkMode = false
    }

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    bindViews()
    setDefaultFragment()
  }

  private fun bindViews() {
    Timber.v("Log: bindViews: Started")
    this.mainFrame = findViewById(R.id.frame_main)
  }

  private fun setDefaultFragment() {
    Timber.v("Log: setDefaultFragment: Started")
    val fragment = MainFragmentViewImpl()
    setFragment(fragment, supportFragmentManager, R.id.frame_main)
  }

  override fun onResume() {
    super.onResume()
    if(isDarkMode(applicationContext) != activityInDarkMode) {
      Timber.d("Log: onResume: Theme was changed, recreating activity")
      recreate()
    }
  }
}
