package app.marcdev.earworm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import timber.log.Timber

class EarwormUtils {

  companion object {
    const val SONG = 0
    const val ALBUM = 1
    const val ARTIST = 2
    const val GENRE = 3
  }

  fun setFragment(fragment: Fragment, fragmentManager: FragmentManager, frameId: Int) {
    Timber.d("Log: setFragment: Replacing frame $frameId with fragment $fragment")
    val fragmentTransaction = fragmentManager.beginTransaction()
    fragmentTransaction.replace(frameId, fragment)
    fragmentTransaction.commit()
  }
}