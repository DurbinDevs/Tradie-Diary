package com.durbindevs.tradiediary.ui.viewmodels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.durbindevs.tradiediary.ADD_JOB_RESULT_OK
import com.durbindevs.tradiediary.EDIT_JOB_RESULT_OK
import com.durbindevs.tradiediary.models.Jobs
import com.durbindevs.tradiediary.repository.Repository
import dagger.assisted.Assisted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTaskViewModel @Inject constructor(
    private val repository: Repository,
    private val state: SavedStateHandle
) : ViewModel() {

    private val jobEventChannel = Channel<AddEditJobEvent>()
    val jobEvents = jobEventChannel.receiveAsFlow()

    val job = state.get<Jobs>("job") // job is name of argument from nav graph

    // send saved state data else send data from current job else send empty string
    var jobTitle = state.get<String>("jobTitle") ?: job?.title ?: ""
        set(value) {
            field = value
            state.set("jobTitle", value) // save value to saved state handle
        }

    var jobLocation = state.get<String>("jobLocation") ?: job?.location ?: ""
        set(value) {
            field = value
            state.set("jobLocation", value) // save value to saved state handle
        }

    var jobDescription = state.get<String>("jobDescription") ?: job?.description ?: ""
        set(value) {
            field = value
            state.set("jobDescription", value) // save value to saved state handle
        }

    var jobStartKm = state.get<Int>("jobStartKm") ?: job?.startKm ?: ""
        set(value) {
            field = value
            state.set("jobStartKm", value) // save value to saved state handle
        }

    var jobFinishKm = state.get<Int>("jobFinishKm") ?: job?.finishKm ?: ""
        set(value) {
            field = value
            state.set("jobFinishKm", value) // save value to saved state handle
        }

    fun onSaveJobClick() {
        if (jobTitle.isBlank()) {
            showInvalidInputMessage("Field cannot be empty")
            return
        }
        if (job != null) {
            val updateJob = job.copy(
                title = jobTitle,
                location = jobLocation,
                description = jobDescription,
                startKm = jobStartKm.toString().toInt(),
                finishKm = jobFinishKm.toString().toInt()
                )
            updateJob(updateJob)
        }else{
            val newJob = Jobs(
                title = jobTitle,
                location = jobLocation,
                description = jobDescription,
                startKm = jobStartKm.toString().toInt(),
                finishKm = jobFinishKm.toString().toInt()
            )
            createJob(newJob)
        }
    }

    private fun showInvalidInputMessage(text: String) = viewModelScope.launch {
        jobEventChannel.send(AddEditJobEvent.ShowInvalidInputText(text))
    }



    private fun createJob(jobs: Jobs) = viewModelScope.launch {
        repository.db.getJobsDao().saveJob(jobs)
        jobEventChannel.send(AddEditJobEvent.NavigateBackWithResult(ADD_JOB_RESULT_OK))
    }

    private fun updateJob(jobs: Jobs) = viewModelScope.launch {
        repository.db.getJobsDao().update(jobs)
        jobEventChannel.send(AddEditJobEvent.NavigateBackWithResult(EDIT_JOB_RESULT_OK))
    }


    sealed class AddEditJobEvent {
        data class ShowInvalidInputText(val msg: String) : AddEditJobEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditJobEvent()
    }

}