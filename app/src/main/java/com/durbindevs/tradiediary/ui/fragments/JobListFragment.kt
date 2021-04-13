package com.durbindevs.tradiediary.ui.fragments


import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.durbindevs.tradiediary.JobActivity
import com.durbindevs.tradiediary.R
import com.durbindevs.tradiediary.SortOrder
import com.durbindevs.tradiediary.adapter.JobAdapter
import com.durbindevs.tradiediary.databinding.JobListFragmentBinding
import com.durbindevs.tradiediary.databinding.JobRowBinding
import com.durbindevs.tradiediary.models.Jobs
import com.durbindevs.tradiediary.ui.viewmodels.MainViewModel
import com.durbindevs.tradiediary.util.exhaustive

import com.durbindevs.tradiediary.util.onQueryTextChanged
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JobListFragment: Fragment(), JobAdapter.OnItemClickListener {

   private val viewModel: MainViewModel by viewModels()
    private val jobAdapter by lazy { JobAdapter(requireContext(), this) }
    private var _binding : JobListFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var searchView: SearchView


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
//       viewModel = (activity as JobActivity).viewModel
        setupRecycler()

        binding.btAddJob.setOnClickListener {
            viewModel.onAddNewJobClick()
           // findNavController().navigate(JobListFragmentDirections.actionJobListFragmentToAddJobFragment())
        }

   viewModel.tasks.observe(viewLifecycleOwner, Observer { jobs ->
            jobAdapter.differ.submitList(jobs)
   })
        // collect flow from channel for snackBar
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.taskEvents.collect { event ->
                when (event) {
                    is MainViewModel.TaskEvent.ShowUndoDeleteMessage -> {
                        Snackbar.make(requireView(), "Job Deleted", Snackbar.LENGTH_LONG)
                            .setAction("UNDO") {
                                viewModel.onUndoDeleteClick(event.job)
                            }.show()
                    }
                    is MainViewModel.TaskEvent.NavigateToAddJobScreen -> {
                        val action = JobListFragmentDirections.actionJobListFragmentToEditJobFragment(null, "Add Job")
                        findNavController().navigate(action)

                    }
                    is MainViewModel.TaskEvent.NavigateToEditTaskScreen -> {
                        val action = JobListFragmentDirections.actionJobListFragmentToEditJobFragment(event.job, "Edit Job")
                        findNavController().navigate(action)
                    }
                    is MainViewModel.TaskEvent.ShowTaskSavedConfirmation -> {
                        Snackbar.make(requireView(), event.msg, Snackbar.LENGTH_SHORT).show()
                    }
                }.exhaustive
            }
        }

        setHasOptionsMenu(true)

        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val job = jobAdapter.differ.currentList[viewHolder.adapterPosition]
                viewModel.onJobSwiped(job)
            }
        }).attachToRecyclerView(binding.rvJobList)
    }

    override fun onItemClick(job: Jobs) {
        viewModel.onJobSelected(job)
    }

    override fun onCompleteCircleClick(job: Jobs, isComplete: Boolean) {
        viewModel.onJobCompleteClick(job, isComplete)
    }
//    override fun onItemClick(position: Int) {
//        Toast.makeText(requireContext(), "Clicked", Toast.LENGTH_SHORT).show()
//        findNavController().navigate(JobListFragmentDirections.actionJobListFragmentToEditJobFragment())
//    }


    private fun setupRecycler() = binding.rvJobList.apply {
        adapter = jobAdapter
        layoutManager = LinearLayoutManager(requireContext())
        addItemDecoration(DividerItemDecoration(requireContext(),
        DividerItemDecoration.VERTICAL))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_fragment, menu)

        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView

        // keeps search tab open when configuration changes
        val pendingQuery = viewModel.searchQuery.value
        if (pendingQuery != null && pendingQuery.isNotEmpty()) {
            searchItem.expandActionView()
            searchView.setQuery(pendingQuery, false)
        }

        searchView.onQueryTextChanged {
            viewModel.searchQuery.value = it
        }

        viewLifecycleOwner.lifecycleScope.launch {
            menu.findItem(R.id.action_hide_completed).isChecked =
                viewModel.preferencesFlow.first().hideCompleted
        }

        setFragmentResultListener("add_edit_request") {_, bundle ->
            val result = bundle.getInt("add_edit_request")
            viewModel.onAddEditResult(result)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_sort_by_name -> {
                viewModel.onSortOrderSelected(SortOrder.BY_NAME)
                true
            }
            R.id.action_sort_by_date_created -> {
                viewModel.onSortOrderSelected(SortOrder.BY_DATE)
                true
            }
            R.id.action_hide_completed -> {
                item.isChecked = !item.isChecked
                viewModel.onHideCompletedClick(item.isChecked)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchView.setOnQueryTextListener(null)
    }
}