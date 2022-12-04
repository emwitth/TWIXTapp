package com.example.twixtapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.twixtapp.databinding.HomeScreenBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeScreen : Fragment() {

    private var _binding: HomeScreenBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = HomeScreenBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var viewModel = ViewModelProvider(requireActivity())[GameViewModel::class.java]

        binding.buttonFirst.setOnClickListener {
            findNavController().navigate(R.id.action_HomeScreen_to_GameScreen)
        }

        binding.buttonReset.setOnClickListener {
            viewModel.reset()
            findNavController().navigate(R.id.action_HomeScreen_to_GameScreen)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}