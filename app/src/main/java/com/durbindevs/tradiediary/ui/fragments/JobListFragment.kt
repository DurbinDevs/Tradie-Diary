package com.durbindevs.tradiediary.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.durbindevs.tradiediary.JobActivity
import com.durbindevs.tradiediary.adapter.JobAdapter
import com.durbindevs.tradiediary.databinding.JobListFragmentBinding
import com.durbindevs.tradiediary.ui.viewmodels.MainViewModel

class JobListFragment: Fragment() {

    private val jobadapter by lazy { JobAdapter(requireContext()) }
    private lateinit var viewModel: MainViewModel
    private var _binding : JobListFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = JobListFragmentBinding.inflate(layoutInflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as JobActivity).viewModel
        setupRecycler()

        binding.btAddJob.setOnClickListener {
            findNavController().navigate(JobListFragmentDirections.actionJobListFragmentToAddJobFragment())
        }

   viewModel.readData.observe(viewLifecycleOwner, Observer { jobs ->
            jobadapter.differ.submitList(jobs)
   })
    }



    private fun setupRecycler() = binding.rvJobList.apply {
        adapter = jobadapter
        layoutManager = LinearLayoutManager(requireContext())
        addItemDecoration(DividerItemDecoration(requireContext(),
        DividerItemDecoration.VERTICAL))
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}