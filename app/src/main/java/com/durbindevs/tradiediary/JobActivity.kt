package com.durbindevs.tradiediary

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.durbindevs.tradiediary.databinding.ActivityJobBinding
import com.durbindevs.tradiediary.db.JobsDatabase
import com.durbindevs.tradiediary.repository.Repository
import com.durbindevs.tradiediary.ui.viewmodels.MainViewModel

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityJobBinding
//    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        val repository = Repository(JobsDatabase(this))
//        val viewModelFactory = MainViewModelProvider(repository)
//        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment
        navController = navHostFragment.findNavController()

        setupActionBarWithNavController(navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

const val ADD_JOB_RESULT_OK = Activity.RESULT_FIRST_USER
const val EDIT_JOB_RESULT_OK = Activity.RESULT_FIRST_USER + 1