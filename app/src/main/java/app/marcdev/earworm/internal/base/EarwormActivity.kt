package app.marcdev.earworm.internal.base

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

abstract class EarwormActivity : AppCompatActivity() {
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