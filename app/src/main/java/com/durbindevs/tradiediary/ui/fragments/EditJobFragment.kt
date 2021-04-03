package com.durbindevs.tradiediary.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.durbindevs.tradiediary.databinding.EditJobFragmentBinding

class EditJobFragment: Fragment() {

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
}