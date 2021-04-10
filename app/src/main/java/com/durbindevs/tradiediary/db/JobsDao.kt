package com.durbindevs.tradiediary.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.durbindevs.tradiediary.SortOrder
import com.durbindevs.tradiediary.models.Jobs
import kotlinx.coroutines.flow.Flow


@Dao
interface JobsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveJob(job: Jobs): Long

    fun getJobs(query: String, sortOrder: SortOrder, hideCompleted: Boolean): Flow<List<Jobs>> =
        when(sortOrder) {
            SortOrder.BY_NAME -> getJobsBYName(query, hideCompleted)
            SortOrder.BY_DATE -> getJobsBYDate(query, hideCompleted)
        }

    @Query("SELECT * FROM jobs WHERE (isCompleted != :hideCompleted OR isCompleted = 0) AND location LIKE '%' || :searchQuery || '%' ORDER BY location")
    fun getJobsBYName(searchQuery: String, hideCompleted: Boolean): Flow<List<Jobs>>

    @Query("SELECT * FROM jobs WHERE (isCompleted != :hideCompleted OR isCompleted = 0) AND  location LIKE '%' || :searchQuery || '%' ORDER BY dateCreated")
    fun getJobsBYDate(searchQuery: String, hideCompleted: Boolean): Flow<List<Jobs>>

    @Delete
    suspend fun deleteJob(job: Jobs)
}