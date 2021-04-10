package com.durbindevs.tradiediary.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.asFlow
import androidx.lifecycle.map
import com.durbindevs.tradiediary.JobActivity
import com.durbindevs.tradiediary.databinding.EditJobFragmentBinding
import com.durbindevs.tradiediary.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class EditJobFragment: Fragment() {

    private lateinit var viewModel: MainViewModel
    private var _binding: EditJobFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = EditJobFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel = (activity as JobActivity).viewModel

       //  val job =  viewModel.readData.map { it }.asFlow()
       // Log.d("test", "??${job}")




//     val jobs = viewModel.jobData.value
//
//        jobs?.copy(
//            location = binding.locationEdit.toString(),
//            title = binding.titleEdit.toString(),
//            description = binding.descEdit.toString(),
//            startKm = binding.editStartKm.toString().toInt(),
//            finishKm = binding.editFinishKm.toString().toInt(),
//            isCompleted = binding.isCompleted.isChecked
//        )
   }
}