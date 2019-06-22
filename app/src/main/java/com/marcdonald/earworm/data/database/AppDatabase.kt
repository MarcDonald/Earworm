package com.marcdonald.earworm.data.database

interface AppDatabase {
    fun dao(): DAO
  fun closeDB()
}