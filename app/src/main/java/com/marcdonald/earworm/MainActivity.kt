package com.marcdonald.earworm

import android.os.Bundle
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.marcdonald.earworm.internal.base.EarwormActivity
import com.marcdonald.earworm.mainscreen.MainFragment
import timber.log.Timber

class MainActivity : EarwormActivity() {
  private lateinit var mainFrame: CoordinatorLayout

  override fun onCreate(savedInstanceState: Bundle?) {
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
}
