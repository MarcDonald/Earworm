package app.marcdev.earworm.data.database

interface AppDatabase {
    fun dao(): DAO
  fun closeDB()
}