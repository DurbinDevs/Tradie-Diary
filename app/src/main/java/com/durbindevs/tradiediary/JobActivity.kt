package com.durbindevs.tradiediary

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import com.durbindevs.tradiediary.databinding.ActivityJobBinding
import com.durbindevs.tradiediary.db.JobsDatabase
import com.durbindevs.tradiediary.repository.Repository
import com.durbindevs.tradiediary.ui.viewmodels.MainViewModel
import com.durbindevs.tradiediary.ui.viewmodels.MainViewModelProvider
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JobActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJobBinding
    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val repository = Repository(JobsDatabase(this))
        val viewModelFactory = MainViewModelProvider(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)

        setupActionBarWithNavController(findNavController(R.id.navHostFragment))
    }
}