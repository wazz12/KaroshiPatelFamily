package com.karoshi.patelfamily.database

import android.content.Context
import android.util.Log
import androidx.room.*
import io.reactivex.Flowable
import java.io.*

@Database(entities = [(Family::class)], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun familyDao(): FamilyDao

    companion object {

        private val TAG = AppDatabase::class.java.simpleName
        private const val DATABASE_NAME = "karoshi_family.sqlite"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance

            copyAttachedDatabase(context)

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java, DATABASE_NAME
                ).build()
                INSTANCE = instance
                return instance
            }
        }

        private fun copyAttachedDatabase(context: Context) {
            Log.e(TAG, "Start of copyAttachedDatabase")

            val dbPath: File = context.getDatabasePath(DATABASE_NAME)
            // If the database already exists, return
            if (dbPath.exists()) {
                Log.e(TAG, "return empty copyAttachedDatabase")
                return
            }

            // Make sure we have a path to the file
            dbPath.parentFile?.mkdirs()

            // Try to copy database file
            try {
                val inputStream: InputStream = context.assets.open(DATABASE_NAME)
                val output: OutputStream = FileOutputStream(dbPath)
                val buffer = ByteArray(8192)
                var length: Int
                while (inputStream.read(buffer, 0, 8192).also { length = it } > 0) {
                    output.write(buffer, 0, length)
                }
                output.flush()
                output.close()
                inputStream.close()
                Log.e(TAG, "End of copyAttachedDatabase")
            } catch (e: IOException) {
                Log.d(TAG, "Failed to open file", e)
                e.printStackTrace()
            }
        }
    }
}


@Entity(tableName = "Family")
data class Family(
    @PrimaryKey
    @ColumnInfo(name = "ID_USER")
    val userId: String,
    @ColumnInfo(name = "ID_FATHER")
    val fatherId: String = "",
    @ColumnInfo(name = "USER_NAME")
    val userName: String,
    @ColumnInfo(name = "USER_SURNAME")
    val userSurname: String = "",
    @ColumnInfo(name = "SOSA")
    val sosa: String = ""
)


@Dao
interface FamilyDao {

    /**
     * Get familyList.
     * @return all the family from the table.
     */
    @Query("SELECT * FROM Family")
    fun getFamilyList(): Flowable<List<Family>>

    /**
     * Get searched familyList.
     * @return searched family from the table.
     */
    @Query("SELECT * FROM Family WHERE USER_NAME LIKE :searchQuery")
    fun getSearchedFamilyList(searchQuery: String): Flowable<List<Family>>

    /**
     * Get fatherName.
     * @return the fatherName from the table.
     */
    @Query("SELECT USER_NAME FROM Family WHERE ID_USER=:fatherId")
    fun getFatherName(fatherId: String): Flowable<String>
}