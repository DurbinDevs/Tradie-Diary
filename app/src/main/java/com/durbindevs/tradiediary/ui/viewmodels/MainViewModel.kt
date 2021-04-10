package com.durbindevs.tradiediary.ui.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.durbindevs.tradiediary.PreferencesManager
import com.durbindevs.tradiediary.SortOrder
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
    private val repository: Repository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    val searchQuery = MutableStateFlow("")
    val preferencesFlow = preferencesManager.preferencesFlow

    private val taskFlow = combine(
        searchQuery,
     preferencesFlow
    ){
        query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        repository.getJobs(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
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

    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedClick(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

  //  fun getJobs(searchQuery: String) = repository.getJobs(searchQuery)

}

