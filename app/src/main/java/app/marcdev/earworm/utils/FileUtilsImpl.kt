package app.marcdev.earworm.utils

import android.content.Context
import timber.log.Timber
import java.io.File

class FileUtilsImpl(private val context: Context): FileUtils {

  override fun deleteImage(imageName: String) {
    val filePath = getArtworkDirectory(context) + imageName
    val file = File(filePath)
    if(file.exists()) {
      file.delete()
    } else {
      Timber.w("Log: deleteImage: File doesn't exist")
    }
  }
}