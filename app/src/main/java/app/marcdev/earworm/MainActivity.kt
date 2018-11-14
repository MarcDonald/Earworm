package app.marcdev.earworm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import app.marcdev.earworm.mainscreen.MainFragmentViewImpl
import app.marcdev.earworm.utils.DARK_THEME
import app.marcdev.earworm.utils.LIGHT_THEME
import app.marcdev.earworm.utils.getTheme
import app.marcdev.earworm.utils.setFragment
import timber.log.Timber

class MainActivity : AppCompatActivity() {

  private lateinit var mainFrame: CoordinatorLayout
  private var activityTheme: Int = -1

  override fun onCreate(savedInstanceState: Bundle?) {
    Timber.d("Log: onCreate: Started")

    /* Theme changes must be done before super.onCreate otherwise it will be overridden with the value
      in the manifest */
    if(getTheme(applicationContext) == DARK_THEME) {
      Timber.v("Log: onCreate: Is dark mode")
      setTheme(R.style.Earworm_DarkTheme)
      activityTheme = DARK_THEME
    } else {
      Timber.v("Log: onCreate: Is not dark mode")
      setTheme(R.style.Earworm_LightTheme)
      activityTheme = LIGHT_THEME
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

    if(intent.action == "app.marcdev.earworm.intent.ADD_ITEM") {
      Timber.d("Log: onCreate: Started from Add Item app shortcut")
      val args = Bundle()
      args.putBoolean("add_item", true)
      fragment.arguments = args
    }

    setFragment(fragment, supportFragmentManager, R.id.frame_main)
  }

  override fun onResume() {
    super.onResume()
    if(getTheme(applicationContext) != activityTheme) {
      Timber.d("Log: onResume: Theme was changed, recreating activity")
      recreate()
    }
  }
}
