package app.marcdev.earworm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import app.marcdev.earworm.mainscreen.MainFragment
import app.marcdev.earworm.utils.DARK_THEME
import app.marcdev.earworm.utils.LIGHT_THEME
import app.marcdev.earworm.utils.getTheme
import app.marcdev.earworm.utils.setFragment
import timber.log.Timber

class MainActivity : AppCompatActivity() {

  private lateinit var mainFrame: CoordinatorLayout
  private var activityTheme: Int = -1

  override fun onCreate(savedInstanceState: Bundle?) {
    /* Theme changes must be done before super.onCreate otherwise it will be overridden with the value
      in the manifest */
    activityTheme = if(getTheme(applicationContext) == DARK_THEME) {
      setTheme(R.style.Earworm_DarkTheme)
      DARK_THEME
    } else {
      setTheme(R.style.Earworm_LightTheme)
      LIGHT_THEME
    }

    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    bindViews()

    setDefaultFragment()
  }

  private fun bindViews() {
    this.mainFrame = findViewById(R.id.frame_main)
  }

  private fun setDefaultFragment() {
    val fragment = MainFragment()

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
      recreate()
    }
  }
}
