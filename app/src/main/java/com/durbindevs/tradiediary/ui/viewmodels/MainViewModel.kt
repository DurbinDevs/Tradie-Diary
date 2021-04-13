package com.durbindevs.tradiediary.ui.viewmodels

import android.util.Log
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.durbindevs.tradiediary.ADD_JOB_RESULT_OK
import com.durbindevs.tradiediary.EDIT_JOB_RESULT_OK
import com.durbindevs.tradiediary.PreferencesManager
import com.durbindevs.tradiediary.SortOrder
import com.durbindevs.tradiediary.db.JobsDao
import com.durbindevs.tradiediary.models.Jobs
import com.durbindevs.tradiediary.repository.Repository
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    // private val jobsDao: Repository,
    private val repository: Repository,
    private val preferencesManager: PreferencesManager,
    private val state: SavedStateHandle
) : ViewModel() {

    val searchQuery = state.getLiveData(
        "searchQuery",
        ""
    )                                         // MutableStateFlow("")
    val preferencesFlow = preferencesManager.preferencesFlow

    private val taskEventChannel = Channel<TaskEvent>()
    val taskEvents = taskEventChannel.receiveAsFlow()

    private val taskFlow = combine(
        searchQuery.asFlow(),
        preferencesFlow
    ) { query, filterPreferences ->
        Pair(query, filterPreferences)
    }.flatMapLatest { (query, filterPreferences) ->
        repository.getJobs(query, filterPreferences.sortOrder, filterPreferences.hideCompleted)
    }

    val tasks = taskFlow.asLiveData()
    //val readData: LiveData<List<Jobs>> = repository.db.getJobsDao().getJobs()


    fun onSortOrderSelected(sortOrder: SortOrder) = viewModelScope.launch {
        preferencesManager.updateSortOrder(sortOrder)
    }

    fun onHideCompletedClick(hideCompleted: Boolean) = viewModelScope.launch {
        preferencesManager.updateHideCompleted(hideCompleted)
    }

    fun onJobSelected(job: Jobs) = viewModelScope.launch {
        taskEventChannel.send(TaskEvent.NavigateToEditTaskScreen(job))
    }


    fun onJobCompleteClick(job: Jobs, isComplete: Boolean) {
        viewModelScope.launch {
            repository.update(job.copy(isCompleted = isComplete))
        }
    }

    fun onJobSwiped(job: Jobs) = viewModelScope.launch {
        repository.deleteJob(job)
        taskEventChannel.send(TaskEvent.ShowUndoDeleteMessage(job))
    }

    fun onUndoDeleteClick(job: Jobs) = viewModelScope.launch {
        repository.saveJob(job)
    }

    fun onAddNewJobClick() = viewModelScope.launch {
        taskEventChannel.send(TaskEvent.NavigateToAddJobScreen)
    }

    fun onAddEditResult(result: Int) {
        when(result) {
         ADD_JOB_RESULT_OK -> showJobSavedMessage("Job Saved")
         EDIT_JOB_RESULT_OK -> showJobSavedMessage("Job Updated")
        }
    }
    private fun showJobSavedMessage(text: String) = viewModelScope.launch {
        taskEventChannel.send(TaskEvent.ShowTaskSavedConfirmation(text))
    }


    sealed class TaskEvent {
        object NavigateToAddJobScreen : TaskEvent()
        data class NavigateToEditTaskScreen(val job: Jobs) : TaskEvent()
        data class ShowUndoDeleteMessage(val job: Jobs) : TaskEvent()
        data class ShowTaskSavedConfirmation(val msg: String) : TaskEvent()
    }

}

