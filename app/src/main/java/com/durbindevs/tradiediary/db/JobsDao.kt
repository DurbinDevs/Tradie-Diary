package com.durbindevs.tradiediary.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.durbindevs.tradiediary.models.Jobs


@Dao
interface JobsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveJob(job: Jobs): Long

    @Query("SELECT * FROM jobs")
    fun getJobs(): LiveData<List<Jobs>>

    @Delete
    suspend fun deleteJob(job: Jobs)
}