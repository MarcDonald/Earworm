package app.marcdev.earworm.utils

import java.io.File

interface FileUtils {
  fun deleteImage(imageName: String)

  fun saveImage(file: File)

  val artworkDirectory: String

  val localBackupDirectory: String
}