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

package com.marcdonald.earworm.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.marcdonald.earworm.internal.DATABASE_NAME
import timber.log.Timber

@Database(entities = [FavouriteItem::class], version = 5)
abstract class ProductionAppDatabase : RoomDatabase(), AppDatabase {

	abstract override fun dao(): DAO

	override fun closeDB() {
		if(INSTANCE != null) {
			INSTANCE?.close()
		} else {
			Timber.e("Log: closeDB: INSTANCE is null")
		}
	}

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