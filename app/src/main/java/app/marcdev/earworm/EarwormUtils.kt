package app.marcdev.earworm

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import timber.log.Timber

const val SONG = 0
const val ALBUM = 1
const val ARTIST = 2
const val GENRE = 3

/**
 * Replaces a fragment in a frame with another fragment
 * @param fragment The fragment to display
 * @param fragmentManager The Fragment Manager
 * @param frameId The ID of the frame to display the new fragment in
 */
fun setFragment(fragment: Fragment, fragmentManager: FragmentManager, frameId: Int) {
  Timber.d("Log: setFragment: Replacing frame $frameId with fragment $fragment")
  val fragmentTransaction = fragmentManager.beginTransaction()
  fragmentTransaction.replace(frameId, fragment)
  fragmentTransaction.commit()
}

/**
 * Converts date to a format suitable for display
 * @param day The day
 * @param month The month (indexed at 0 the same as the Java calendar, so January is 0)
 * @param year The year
 */
fun formatDateForDisplay(day: Int, month: Int, year: Int): String {
  Timber.d("Log: formatDateForDisplay: Started with day = $day, month = $month, year = $year")
  // Add 1 to month to make it non-zero indexed (January will now be 1 rather than 0)
  return "$day/${month + 1}/$year"
}