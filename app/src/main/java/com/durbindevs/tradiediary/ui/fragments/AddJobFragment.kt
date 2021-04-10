package com.durbindevs.tradiediary.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.durbindevs.tradiediary.JobActivity
import com.durbindevs.tradiediary.databinding.AddJobFragmentBinding
import com.durbindevs.tradiediary.models.Jobs
import com.durbindevs.tradiediary.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.time.Instant

@AndroidEntryPoint
class AddJobFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()
  //  private lateinit var viewModel: MainViewModel
    private var _binding: AddJobFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddJobFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       // viewModel = (activity as JobActivity).viewModel

        binding.btStartJob.setOnClickListener {
          addJob()
            findNavController().navigate(AddJobFragmentDirections.actionAddJobFragmentToJobListFragment())
        }
    }

    private fun addJob() {
        val location = binding.etLocation.text.toString()
        val title = binding.etTitle.text.toString()
        val description = binding.etJobDescription.text.toString()
        val startKm = binding.etStartKm.text.toString().toInt()
        val finishKm = binding.etFinishKm.text.toString().toInt()
        val isCompleted = binding.cbIsCompleted.isChecked

        val job = Jobs(location,title,description,0,System.currentTimeMillis(),startKm,finishKm,isCompleted)
        viewModel.saveJob(job)
        Toast.makeText(requireContext(), "Saved Successfully", Toast.LENGTH_SHORT).show()
    }



}