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
import android.net.Uri
import androidx.core.content.FileProvider
import com.marcdonald.earworm.internal.DATABASE_NAME
import com.marcdonald.earworm.internal.PACKAGE
import timber.log.Timber
import java.io.File

class FileUtilsImpl(private val context: Context) : FileUtils {

	override fun deleteImage(imageName: String) {
		val filePath = artworkDirectory + imageName
		val file = File(filePath)
		if(file.exists()) {
			file.delete()
		} else {
			Timber.w("Log: deleteImage: File doesn't exist")
		}
	}

	override fun saveImage(file: File) {
		val toPath = artworkDirectory + file.name
		val toFile = File(toPath)
		if(toFile.compareTo(file) != 0) {
			try {
				file.copyTo(toFile, true)
			} catch(e: NoSuchFileException) {
				Timber.e("Log: saveImage: $e")
			}
		} else {
			Timber.d("Log: saveImage: No need to save as file already exists in storage")
		}
	}

	override fun getUriForFilePath(filePath: String): Uri {
		val file = File(filePath)
		return FileProvider.getUriForFile(context, "$PACKAGE.FileProvider", file)
	}

	override val artworkDirectory: String
		get() = context.filesDir.path + "/artwork/"

	override val localBackupDirectory: String
		get() = context.filesDir.path + "/backup/"

	override val databaseDirectory: String
		get() = context.getDatabasePath(DATABASE_NAME).path
}