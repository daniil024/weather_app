package com.example.yandexweatherapp

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.yandexweatherapp.databinding.FragmentPermissionBinding

class PermissionFragment : Fragment() {

    private var _binding: FragmentPermissionBinding? = null
    private val binding get() = _binding!!

    private val viewModel:WeatherViewModel by activityViewModels()

    private val navHostFragment by lazy {
        Navigation.findNavController(activity as MainActivity, R.id.navHostFragment)
    }

    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
//            viewModel.setLocationPermissionGrant(it.values.first())
            navHostFragment.navigate(R.id.action_permissionFragment_to_weatherFragment)
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPermissionBinding.inflate(inflater, container, false)
        setupUX()
        return binding.root
    }

    private fun setupUX() {
        binding.nextButton.setOnClickListener {
            permissionResult.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}