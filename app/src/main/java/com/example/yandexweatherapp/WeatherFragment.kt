package com.example.yandexweatherapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.yandexweatherapp.adapter.OnWeatherRecyclerItemClicked
import com.example.yandexweatherapp.adapter.WeatherAdapter
import com.example.yandexweatherapp.adapter.WeatherCardDecoration
import com.example.yandexweatherapp.databinding.WeatherFragmentBinding
import com.example.yandexweatherapp.models.DailyDTO
import com.example.yandexweatherapp.models.DailyHourly
import com.example.yandexweatherapp.models.DailyHourlyAdapter
import com.example.yandexweatherapp.models.HourlyDTO
import com.example.yandexweatherapp.utils.Mapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import kotlin.math.roundToInt

class WeatherFragment : Fragment() {

    private lateinit var layout: View
    private var _binding: WeatherFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: WeatherViewModel by activityViewModels()

    private lateinit var weatherAdapter: WeatherAdapter

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        println("CoroutineExceptionHandler got $exception in $coroutineContext")
    }

    private val scope = CoroutineScope(Dispatchers.IO + exceptionHandler)

    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = WeatherFragmentBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.weatherViewModel = viewModel
        layout = binding.mainLayout
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireContext())
    }

    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        //accessPermissions(layout)
    }

    override fun onResume() {
        super.onResume()
        setupRecycler()
        setupObserver()

        getCurrentLocation()
    }

    override fun onStop() {
        super.onStop()
        scope.cancel()
    }

    private fun setupObserver() {
        viewModel.oneCallApiCallWeatherDTO.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                scope.launch(Dispatchers.Main) {
                    binding.weatherTemp.text = data.current.temp.toInt().toString()
                    binding.weatherTimezone.text = data.timezone
                    binding.weatherParamsWindDynamic.text =
                        data.current.wind_speed.roundToInt().toString()
                    binding.weatherMain.text = data.current.weather[0].description
                    binding.weatherParamsHumidityDynamic.text = data.current.humidity.toString()
                    binding.weatherParamsPressure.text = data.current.pressure.toString()
                    weatherAdapter.setWeather(data.daily)
                    weatherAdapter.notifyDataSetChanged()

                    Glide.with(requireContext())
                        .load("http://openweathermap.org/img/wn/${data.current.weather[0].icon}@2x.png")
                        .into(binding.weatherIcon)
                }
            }
        }

        viewModel.coordinates.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                scope.launch(Dispatchers.IO) {
                    viewModel.getWeather(data.first, data.second, "ru")
                }
            }
        }
    }

    private fun setupRecycler() {
        weatherAdapter = WeatherAdapter(requireContext(), clickListener)
        binding.weatherList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.weatherList.adapter = weatherAdapter
        binding.weatherList.addItemDecoration(WeatherCardDecoration(8))
    }

    private val clickListener = object : OnWeatherRecyclerItemClicked {
        override fun onClick(hourlyDailyWeather: DailyHourlyAdapter) {
            if (hourlyDailyWeather is HourlyDTO) {
                binding.weatherTemp.text = hourlyDailyWeather.temp.toInt().toString()
                binding.weatherParamsWindDynamic.text =
                    hourlyDailyWeather.wind_speed.roundToInt().toString()
                binding.weatherMain.text = hourlyDailyWeather.weather[0].description
                binding.weatherParamsHumidityDynamic.text = hourlyDailyWeather.humidity.toString()
                binding.weatherParamsPressure.text = hourlyDailyWeather.pressure.toString()

                Glide.with(requireContext())
                    .load("http://openweathermap.org/img/wn/${hourlyDailyWeather.weather[0].icon}@2x.png")
                    .into(binding.weatherIcon)
            }else{
                hourlyDailyWeather as DailyDTO

                binding.weatherTemp.text = hourlyDailyWeather.temp.day.toInt().toString()
                binding.weatherParamsWindDynamic.text =
                    hourlyDailyWeather.wind_speed.roundToInt().toString()
                binding.weatherMain.text = hourlyDailyWeather.weather[0].description
                binding.weatherParamsHumidityDynamic.text = hourlyDailyWeather.humidity.toString()
                binding.weatherParamsPressure.text = hourlyDailyWeather.pressure.toString()

                Glide.with(requireContext())
                    .load("http://openweathermap.org/img/wn/${hourlyDailyWeather.weather[0].icon}@2x.png")
                    .into(binding.weatherIcon)
            }
        }
    }

    private fun accessPermissions(view: View) {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
//                layout.showSnackbar(
//                    view,
//                    getString(R.string.permission_granted),
//                    Snackbar.LENGTH_SHORT,
//                    null
//                ) {}
            }

            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) && ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) -> {
                layout.showSnackbar(
                    view,
                    getString(R.string.permission_required),
                    Snackbar.LENGTH_SHORT,
                    getString(R.string.ok)
                ) {
                    permissionResult.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        )
                    )
                }
            }

            else -> {
                permissionResult.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    )
                )
            }
        }
//
//
//
//        if (ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(
//                requireContext(),
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//            permissionResult.launch(
//                arrayOf(
//                    Manifest.permission.ACCESS_FINE_LOCATION,
//                    Manifest.permission.ACCESS_COARSE_LOCATION
//                )
//            )
//        }
    }


    private fun getCurrentLocation() {
        if (checkPermission()) {
            if (isLocationEnabled()) {
                fusedLocationProviderClient.lastLocation.addOnCompleteListener(requireActivity()) { task ->
                    val location: Location? = task.result
                    if (location == null) {
                        Toast.makeText(
                            requireContext(),
                            "null received from location",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        viewModel.setCoordinates(location.latitude, location.longitude)
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Turn on location!", Toast.LENGTH_SHORT).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
//        ActivityCompat.requestPermissions(
//            requireActivity(), arrayOf(
//                Manifest.permission.ACCESS_COARSE_LOCATION,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ), PERMISSION_REQUEST_ACCESS_LOCATION
//        )
        permissionResult.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    companion object {
        private const val PERMISSION_REQUEST_ACCESS_LOCATION = 101
    }
}

fun View.showSnackbar(
    view: View,
    msg: String,
    length: Int,
    actionMessage: CharSequence?,
    action: (View) -> Unit
) {
    val snackbar = Snackbar.make(view, msg, length)
    if (actionMessage != null) {
        snackbar.setAction(actionMessage) {
            action(this)
        }.show()
    } else {
        snackbar.show()
    }
}