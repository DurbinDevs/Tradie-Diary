package com.durbindevs.tradiediary.ui.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.durbindevs.tradiediary.models.Jobs
import com.durbindevs.tradiediary.repository.Repository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository): ViewModel() {

    val readData: LiveData<List<Jobs>> = repository.db.getJobsDao().getJobs()

    fun saveJob(job: Jobs) =
        viewModelScope.launch {
            repository.saveJob(job)
    }

    fun deleteJob(job: Jobs) =
        viewModelScope.launch {
            repository.deleteJob(job)
        }

    fun getJobs() = repository.getJobs()

}