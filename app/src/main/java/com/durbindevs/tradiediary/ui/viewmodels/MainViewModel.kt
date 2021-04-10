package com.durbindevs.tradiediary.ui.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.durbindevs.tradiediary.db.JobsDao
import com.durbindevs.tradiediary.models.Jobs
import com.durbindevs.tradiediary.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
   // private val jobsDao: Repository,
    private val repository: Repository
) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    val sortOrder = MutableStateFlow(SortOrder.BY_NAME)
    val hideCompleted = MutableStateFlow(false)

    private val taskFlow = combine(
        searchQuery,
        sortOrder,
        hideCompleted
    ){
        query, sortOder, hideCompleted ->
        Triple(query, sortOder, hideCompleted)
    }.flatMapLatest { (query, sortOrder, hideCompleted) ->
        repository.getJobs(query, sortOrder, hideCompleted)
    }

    val tasks = taskFlow.asLiveData()
    //val readData: LiveData<List<Jobs>> = repository.db.getJobsDao().getJobs()

    fun saveJob(job: Jobs) =
        viewModelScope.launch {
            repository.saveJob(job)
        }

    fun deleteJob(job: Jobs) =
        viewModelScope.launch {
            repository.deleteJob(job)
        }

  //  fun getJobs(searchQuery: String) = repository.getJobs(searchQuery)

}

enum class SortOrder {  BY_NAME, BY_DATE}