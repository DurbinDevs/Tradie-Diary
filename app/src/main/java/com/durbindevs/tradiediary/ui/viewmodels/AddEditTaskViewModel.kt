package com.durbindevs.tradiediary.ui.viewmodels


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.durbindevs.tradiediary.ADD_JOB_RESULT_OK
import com.durbindevs.tradiediary.EDIT_JOB_RESULT_OK
import com.durbindevs.tradiediary.models.Jobs
import com.durbindevs.tradiediary.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
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

    var jobStartKm = state.get<String>("jobStartKm") ?: job?.startKm ?: "0"
        set(value) {
            field = value
            state.set("jobStartKm", value) // save value to saved state handle
        }

    var jobFinishKm = state.get<String>("jobFinishKm") ?: job?.finishKm ?: "0"
        set(value) {
            field = value
            state.set("jobFinishKm", value) // save value to saved state handle
        }

    var jobCompleted = state.get<Boolean>("jobCompleted") ?: job?.isCompleted ?: false
        set(value) {
            field = value
            state.set("jobCompleted", value)
        }
    val jobTotalKm = totalKmCalculation(jobStartKm.toInt(), jobFinishKm.toInt())

    var jobCompleteTime = state.get<Long>("jobCompletedTime") ?: job?.dateFinished ?: 0

    private var jobDateCreated = state.get<Long>("jobCreatedTime") ?: job?.dateCreated ?: 0

    private var totalTime = completeTime(jobDateCreated, jobCompleteTime)
    val test = String.format(
        "%02d:%02d",
        TimeUnit.MILLISECONDS.toHours(totalTime),
        TimeUnit.MILLISECONDS.toMinutes(totalTime) % TimeUnit.HOURS.toMinutes(1),
       // TimeUnit.MILLISECONDS.toSeconds(totalTime) % TimeUnit.MINUTES.toSeconds(1)
    )

    fun onSaveJobClick() {
        if (jobTitle.isBlank()) {
            showInvalidInputMessage("Title cannot be empty")
            return
        }
        if (job != null) {
            val updateJob = job.copy(
                title = jobTitle,
                location = jobLocation,
                description = jobDescription,
                startKm = jobStartKm,
                finishKm = jobFinishKm,
                isCompleted = jobCompleted,
                totalKm = jobTotalKm,
                dateFinished = jobCompleteTime,
                totalTime = test
            )
            updateJob(updateJob)
        } else {
            val newJob = Jobs(
                title = jobTitle,
                location = jobLocation,
                description = jobDescription,
                startKm = jobStartKm,
                finishKm = jobFinishKm,
                isCompleted = jobCompleted,
                totalKm = jobTotalKm,
                dateFinished = jobCompleteTime
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
        // Log.d("TAG", "total${jobTotalKm} ")
        repository.db.getJobsDao().update(jobs)
        jobEventChannel.send(AddEditJobEvent.NavigateBackWithResult(EDIT_JOB_RESULT_OK))
    }

    private fun totalKmCalculation(startKm: Int?, finishKm: Int?): String {
        var total = finishKm!! - startKm!!
        if (finishKm < startKm) {
            total = 0
        }
        return total.toString()
    }

    private fun completeTime(start: Long, finish: Long): Long {
        return finish - start

    }


    sealed class AddEditJobEvent {
        data class ShowInvalidInputText(val msg: String) : AddEditJobEvent()
        data class NavigateBackWithResult(val result: Int) : AddEditJobEvent()
    }

}