package app.marcdev.earworm

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import app.marcdev.earworm.mainscreen.MainFragmentViewImpl
import timber.log.Timber

class MainActivity : AppCompatActivity() {

  private lateinit var mainFrame: CoordinatorLayout

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    Timber.d("Log: onCreate: Started")
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
    EarwormUtils().setFragment(fragment, supportFragmentManager, R.id.frame_main)
  }
}
