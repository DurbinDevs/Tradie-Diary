package com.durbindevs.tradiediary.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.durbindevs.tradiediary.models.Jobs


@TypeConverters(Converters::class)
@Database(
    entities = [Jobs::class],
    version = 1,
    exportSchema = false
)
abstract class JobsDatabase: RoomDatabase() {

    abstract fun getJobsDao(): JobsDao

    companion object{
        @Volatile
        private var instance: JobsDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also { instance = it}
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                JobsDatabase::class.java,
                "Jobs_db"
            ).build()
    }
}