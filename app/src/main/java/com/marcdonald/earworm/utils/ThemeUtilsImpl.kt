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

package com.marcdonald.earworm.utils

import android.content.Context
import android.preference.PreferenceManager
import com.marcdonald.earworm.R
import com.marcdonald.earworm.internal.PREF_DARK_THEME

class ThemeUtilsImpl(private val context: Context) : ThemeUtils {
	override fun isLightMode(): Boolean {
		return !(PreferenceManager.getDefaultSharedPreferences(context.applicationContext).getBoolean(PREF_DARK_THEME, true))
	}

	override fun getAccentColor(): Int {
		return if(isLightMode())
			context.resources.getColor(R.color.lightThemeColorAccent, null)
		else
			context.resources.getColor(R.color.darkThemeColorAccent, null)
	}
}