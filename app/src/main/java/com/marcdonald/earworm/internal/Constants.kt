/*
 * Copyright (c) 2019 Marc Donald
 *
 * The MIT License (MIT)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in
 * the Software without restriction, including without limitation the rights to
 * use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of
 * the Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER
 * IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.marcdonald.earworm.internal

import com.marcdonald.earworm.utils.ItemFilter

val DEFAULT_FILTER = ItemFilter(1, 0, 1900, 31, 11, 2099, true, true, true, "")

const val PACKAGE = "com.marcdonald.earworm"
const val DATABASE_NAME = "AppDatabase.db"

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
const val PREF_DARK_THEME = "pref_dark_theme"
const val PREF_SHOW_TIPS = "pref_show_tips"
const val PREF_BUILD_NUMBER = "pref_build_number"
const val PREF_LICENSES = "pref_licenses"
const val PREF_PRIVACY = "pref_privacy"
const val PREF_GITHUB = "pref_github"
const val PREF_AUTHOR = "pref_author"
const val PREF_CLEAR_INPUTS = "pref_clear_inputs_on_type_change_bool"
const val PREF_BACKUP = "pref_backup"
const val PREF_RESTORE = "pref_restore"
const val PREF_APP_UPDATE = "pref_app_update"
// </editor-fold>

// <editor-fold desc="Request Codes">
const val CHOOSE_RESTORE_FILE_REQUEST_CODE = 2
// </editor-fold>

// <editor-fold desc="Argument Keys">
const val RESTORE_FILE_PATH_KEY = "restore_file_path"
// </editor-fold>