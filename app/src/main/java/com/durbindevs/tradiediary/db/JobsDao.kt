package com.durbindevs.tradiediary.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.durbindevs.tradiediary.models.Jobs
import kotlinx.coroutines.flow.Flow


@Dao
interface JobsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveJob(job: Jobs): Long

    @Query("SELECT * FROM jobs WHERE location LIKE '%' || :searchQuery || '%'")
    fun getJobs(searchQuery: String): Flow<List<Jobs>>

    @Delete
    suspend fun deleteJob(job: Jobs)
}