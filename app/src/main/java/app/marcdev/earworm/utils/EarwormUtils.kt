package app.marcdev.earworm.utils

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.preference.PreferenceManager
import android.widget.ImageView
import app.marcdev.earworm.R
import app.marcdev.earworm.internal.DARK_THEME
import app.marcdev.earworm.internal.LIGHT_THEME
import app.marcdev.earworm.internal.PREF_THEME

// <editor-fold desc="TODO These may be removed in an upcoming theme overhaul">
/**
 * Changes the color of a drawable in an ImageView to indicate whether it is activated or not.
 * Deactivated will change the color to either black or 70% white depending on the theme
 * @param context Context
 * @param button The button to change the color of
 * @param isActivated Whether or not the button should be put into the activated state
 */
fun changeColorOfImageViewDrawable(context: Context, button: ImageView, isActivated: Boolean) {
  when {
    isActivated -> button.setColorFilter(context.getColor(R.color.lightThemeColorAccent))
    (getTheme(context) == DARK_THEME && !isActivated) -> button.setColorFilter(context.getColor(R.color.white60))
    else -> button.setColorFilter(context.getColor(R.color.black))
  }
}

/**
 * Changes the color of a drawable to indicate whether it is activated or not. Deactivated will
 * change the color to either black or 70% white depending on the theme
 * @param context Context
 * @param drawable The drawable to change the color of
 * @param isActivated Whether or not the button should be put into the activated state
 */
fun changeColorOfDrawable(context: Context, drawable: Drawable, isActivated: Boolean) {
  when {
    isActivated -> drawable.setColorFilter(context.getColor(R.color.lightThemeColorAccent), PorterDuff.Mode.SRC_IN)
    (getTheme(context) == DARK_THEME && !isActivated) -> drawable.setColorFilter((context.getColor(R.color.white60)), PorterDuff.Mode.SRC_IN)
    else -> drawable.setColorFilter((context.getColor(R.color.black)), PorterDuff.Mode.SRC_IN)
  }
}

/**
 * Checks the shared preferences to see if the user has selected dark mode
 * @param context Context
 */
fun getTheme(context: Context): Int {
  val prefs = PreferenceManager.getDefaultSharedPreferences(context)

  return when(prefs.getString(PREF_THEME, context.resources.getString(R.string.light))) {
    context.resources.getString(R.string.light) -> LIGHT_THEME
    context.resources.getString(R.string.dark) -> DARK_THEME
    else -> -1
  }
}
// </editor-fold>