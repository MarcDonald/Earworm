package app.marcdev.earworm.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import app.marcdev.earworm.internal.DATABASE_NAME

@Database(entities = [FavouriteItem::class], version = 5)
abstract class ProductionAppDatabase : RoomDatabase(), AppDatabase {
  abstract override fun dao(): DAO

  companion object {
    @Volatile
    private var INSTANCE: ProductionAppDatabase? = null
    private val LOCK = Any()

    operator fun invoke(context: Context) = INSTANCE
                                            ?: synchronized(LOCK) {
                                              INSTANCE
                                              ?: buildDatabase(context).also { INSTANCE = it }
                                            }

    private fun buildDatabase(context: Context) =
      Room.databaseBuilder(
        context.applicationContext,
        ProductionAppDatabase::class.java,
        DATABASE_NAME
      )
        .setJournalMode(JournalMode.TRUNCATE)
        .addMigrations(MIGRATION_4_TO_5())
        .build()
  }

  class MIGRATION_4_TO_5 : Migration(4, 5) {
    override fun migrate(database: SupportSQLiteDatabase) {
      database.execSQL("ALTER TABLE favourite_items RENAME TO favourite_items_old")
      database.execSQL("CREATE TABLE FavouriteItems(" +
                       "id INTEGER PRIMARY KEY NOT NULL," +
                       "songName TEXT NOT NULL," +
                       "albumName TEXT NOT NULL," +
                       "artistName TEXT NOT NULL," +
                       "genre TEXT NOT NULL," +
                       "day INTEGER NOT NULL," +
                       "month INTEGER NOT NULL," +
                       "year INTEGER NOT NULL," +
                       "type INTEGER NOT NULL," +
                       "imageName TEXT NOT NULL)")
      database.execSQL("INSERT INTO FavouriteItems SELECT * FROM favourite_items_old")
      database.execSQL("DROP TABLE favourite_items_old")
    }
  }
}