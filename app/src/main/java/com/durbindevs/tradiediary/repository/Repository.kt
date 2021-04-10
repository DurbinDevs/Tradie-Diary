package com.durbindevs.tradiediary.repository

import com.durbindevs.tradiediary.SortOrder
import com.durbindevs.tradiediary.db.JobsDatabase
import com.durbindevs.tradiediary.models.Jobs
import dagger.Provides
import javax.inject.Inject


class Repository @Inject constructor(val db: JobsDatabase) {

    suspend fun saveJob(job: Jobs) =
        db.getJobsDao().saveJob(job)

    suspend fun deleteJob(job: Jobs) =
        db.getJobsDao().deleteJob(job)

    fun getJobs(searchQuery: String, sortOrder: SortOrder, hideCompleted: Boolean) =
        db.getJobsDao().getJobs(searchQuery,sortOrder,hideCompleted)
}