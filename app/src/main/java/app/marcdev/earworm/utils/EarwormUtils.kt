package app.marcdev.earworm.utils

import android.content.Context
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import app.marcdev.earworm.ItemFilter
import app.marcdev.earworm.R
import timber.log.Timber

const val SONG = 0
const val ALBUM = 1
const val ARTIST = 2
const val GENRE = 3
const val HEADER = 4
val DEFAULT_FILTER = ItemFilter(1, 0, 1900, 31, 11, 2099, true, true, true, "")

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

/**
 * Changes the color of a drawable in an ImageView to indicate whether it is activated or not
 * @param context Context
 * @param button The button to change the color of
 * @param
 */
fun changeColorOfImageButtonDrawable(context: Context, button: ImageButton, isActivated: Boolean) {
  Timber.v("Log: changeColorOfImageButtonDrawable: Started")

  if(isActivated) {
    DrawableCompat.setTint(button.drawable, ContextCompat.getColor(context, R.color.colorAccent))
  } else {
    DrawableCompat.setTint(button.drawable, ContextCompat.getColor(context, R.color.black))
  }
}

/**
 * Gets the full name of a month based on it's 0 indexed number
 * @param month Integer value of the month, indexed at 0
 * @param context Context
 * @return Full name of the month in string format
 */
fun getMonthName(month: Int, context: Context): String {
  val monthArray = context.resources.getStringArray(R.array.months)
  return monthArray[month]
}