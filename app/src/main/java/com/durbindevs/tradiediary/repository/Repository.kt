package com.durbindevs.tradiediary.repository

import com.durbindevs.tradiediary.db.JobsDatabase
import com.durbindevs.tradiediary.models.Jobs

class Repository(val db: JobsDatabase) {

    suspend fun saveJob(job: Jobs) =
        db.getJobsDao().saveJob(job)

    suspend fun deleteJob(job: Jobs) =
        db.getJobsDao().deleteJob(job)

    fun getJobs() = db.getJobsDao().getJobs()
}