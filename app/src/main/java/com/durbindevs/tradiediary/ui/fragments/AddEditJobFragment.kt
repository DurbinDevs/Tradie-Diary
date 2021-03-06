package com.durbindevs.tradiediary.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.durbindevs.tradiediary.JobActivity
import com.durbindevs.tradiediary.databinding.AddEditJobFragmentBinding
import com.durbindevs.tradiediary.models.Jobs
import com.durbindevs.tradiediary.ui.viewmodels.AddEditTaskViewModel
import com.durbindevs.tradiediary.ui.viewmodels.MainViewModel
import com.durbindevs.tradiediary.util.exhaustive
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import androidx.fragment.app.setFragmentResult
import java.time.Instant
import kotlin.time.days

@AndroidEntryPoint
class AddEditJobFragment : Fragment() {

    private val addEditViewModel: AddEditTaskViewModel by viewModels()
    private val viewModel: MainViewModel by viewModels()
  //  private lateinit var viewModel: MainViewModel
    private var _binding: AddEditJobFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddEditJobFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            etTitle.setText(addEditViewModel.jobTitle)
            etLocation.setText(addEditViewModel.jobLocation)
            etJobDescription.setText(addEditViewModel.jobDescription)
            etStartKm.setText(addEditViewModel.jobStartKm.toString())
            etFinishKm.setText(addEditViewModel.jobFinishKm.toString())
            cbIsCompleted.isChecked = addEditViewModel.jobCompleted
            etTotalKm.setText(addEditViewModel.jobTotalKm)
            etCompletedOn.text = DateUtils.getRelativeDateTimeString(
                context,
                addEditViewModel.jobCompleteTime!!,
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.FORMAT_NUMERIC_DATE.toLong(),
                0
            )
            etJobTime.text = addEditViewModel.test.toString()

       //     if (addEditViewModel.totalTime >= )

            if (addEditViewModel.jobCompleted){
                etCompletedOn.isVisible = true
                CompletedOn.isVisible = true
                etJobTime.isVisible = true
                JobTime.isVisible = true
            }

            etTitle.addTextChangedListener {
                addEditViewModel.jobTitle = it.toString()
            }
            etLocation.addTextChangedListener {
                addEditViewModel.jobLocation = it.toString()
            }
            etJobDescription.addTextChangedListener {
                addEditViewModel.jobDescription = it.toString()
            }
            etStartKm.addTextChangedListener {
                addEditViewModel.jobStartKm = it.toString()
            }
            etFinishKm.addTextChangedListener {
                addEditViewModel.jobFinishKm = it.toString()
            }
            cbIsCompleted.setOnCheckedChangeListener{ _, isChecked ->
                addEditViewModel.jobCompleted = isChecked
                addEditViewModel.jobCompleteTime = System.currentTimeMillis()
            }

        }

        binding.btStartJob.setOnClickListener {
            addEditViewModel.onSaveJobClick()
//            easy way to navigate below but not eay to test
//            findNavController().navigate(AddEditJobFragmentDirections.actionAddJobFragmentToJobListFragment())
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            addEditViewModel.jobEvents.collect { event ->
                when(event) {
                    is AddEditTaskViewModel.AddEditJobEvent.NavigateBackWithResult -> {
                        binding.etTitle.clearFocus()
                        setFragmentResult(
                            "add_edit_request",
                            bundleOf("add_edit_result" to event.result)
                        )
                        findNavController().popBackStack()

                    }
                    is AddEditTaskViewModel.AddEditJobEvent.ShowInvalidInputText -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                }.exhaustive
            }
        }
    }


}