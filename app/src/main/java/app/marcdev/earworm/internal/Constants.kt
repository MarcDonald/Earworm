package app.marcdev.earworm.internal

import app.marcdev.earworm.utils.ItemFilter

val DEFAULT_FILTER = ItemFilter(1, 0, 1900, 31, 11, 2099, true, true, true, "")

// <editor-fold desc="Item Types">
const val SONG = 0
const val ALBUM = 1
const val ARTIST = 2
const val GENRE = 3
const val HEADER = 4
// </editor-fold>

// <editor-fold desc="Theme IDs">
const val LIGHT_THEME = 0
const val DARK_THEME = 1
// </editor-fold>

// <editor-fold desc="Preference Keys">
const val PREF_THEME = "pref_theme"
const val PREF_SHOW_TIPS = "pref_show_tips"
const val PREF_BUILD_NUMBER = "pref_build_number"
const val PREF_LICENSES = "pref_licenses"
const val PREF_GITHUB = "pref_github"
const val PREF_CLEAR_INPUTS = "pref_clear_inputs_on_type_change"
// </editor-fold>