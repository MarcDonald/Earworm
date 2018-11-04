package app.marcdev.earworm.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [FavouriteItem::class], version = 3)
abstract class AppDatabase : RoomDatabase() {

  abstract fun dao(): DAO

  companion object {
    @Volatile
    private var INSTANCE: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
      val tempInstance = INSTANCE

      if(tempInstance != null) {
        return tempInstance
      }

      synchronized(this) {

        val instance = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "AppDatabase.db")
          .addMigrations(MIGRATION_2_3).build()

        INSTANCE = instance
        return instance
      }
    }

    private val MIGRATION_2_3: Migration = object : Migration(2, 3) {
      override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE favourite_items " +
                         "ADD COLUMN imageUri TEXT NOT NULL DEFAULT '';")
      }
    }
  }
}