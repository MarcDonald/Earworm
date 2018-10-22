package app.marcdev.earworm.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FavouriteItem::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

  abstract fun dao(): DAO

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      val tempInstance = INSTANCE

      if (tempInstance != null) {
        return tempInstance
      }

      synchronized(this) {

        val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "AppDatabase.db")
          .fallbackToDestructiveMigration().build()

        INSTANCE = instance
        return instance
      }
    }
  }
}