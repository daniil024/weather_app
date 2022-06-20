package com.example.yandexweatherapp

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.yandexweatherapp.adapter.OnWeatherRecyclerItemClicked
import com.example.yandexweatherapp.adapter.WeatherAdapter
import com.example.yandexweatherapp.adapter.WeatherCardDecoration
import com.example.yandexweatherapp.databinding.WeatherFragmentBinding
import com.example.yandexweatherapp.models.*
import com.example.yandexweatherapp.room.WeatherDatabase
import com.example.yandexweatherapp.room.entities.OneApiCallWeatherEntity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
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
        setupSpinner()
        setupObserver()
        setupRecycler()
    }

    override fun onResume() {
        super.onResume()
        if(checkInternetConnection()) {
            getCurrentLocation()
        } else {

        }
    }

    override fun onStop() {
        super.onStop()
        scope.cancel()
    }

    private fun setupSpinner() {
        binding.weatherPeriod.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 1) {
                    if (viewModel.oneCallApiCallWeatherDTO.value != null) {
                        bindData(viewModel.oneCallApiCallWeatherDTO.value!!, DailyHourlyEnum.DAILY)
                    }
                } else {
                    if (viewModel.oneCallApiCallWeatherDTO.value != null) {
                        bindData(viewModel.oneCallApiCallWeatherDTO.value!!, DailyHourlyEnum.HOURLY)
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

        }
    }

    private fun setupObserver() {
        viewModel.oneCallApiCallWeatherDTO.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                bindData(data, DailyHourlyEnum.HOURLY)
                scope.launch (Dispatchers.IO) {
                    val lat = data.lat
                    val lon = data.lon
                    val dbInstance = WeatherDatabase.getWeatherDatabase(requireContext())
                    val dataToStore = OneApiCallWeatherEntity(lat = lat, lon = lon,
                    timezone = data.timezone, timezone_offset = data.timezone_offset,
                    current = data.current, hourly = data.hourly, daily = data.daily)
                    dbInstance?.oneApiCallWeatherDao()?.insertWeatherAtPlace(dataToStore)

                    Log.i("my_log:", dbInstance?.oneApiCallWeatherDao()?.getWeatherAtPlace(lat, lon).toString())
                }
            }
        }

        viewModel.coordinates.observe(viewLifecycleOwner) { data ->
            if (!checkInternetConnection()) {
                Toast.makeText(requireContext(), "Turn on internet!", Toast.LENGTH_LONG).show()
            } else if (data != null && checkInternetConnection()) {
                scope.launch(Dispatchers.IO) {
                    viewModel.getWeather(data.first, data.second, "ru")
                }
            }
        }
    }

    private fun bindData(data: OneApiCallWeatherDTO, dailyHourlyEnum: DailyHourlyEnum) {
        binding.weatherTemp.text = data.current.temp.toInt().toString()
        binding.weatherTimezone.text = data.timezone
        binding.weatherParamsWindDynamic.text =
            data.current.wind_speed.roundToInt().toString()
        binding.weatherMain.text = data.current.weather[0].description
        binding.weatherParamsHumidityDynamic.text = data.current.humidity.toString()
        binding.weatherParamsPressure.text = data.current.pressure.toString()

        if (dailyHourlyEnum.type == "Сегодня") {
            weatherAdapter.replaceData(data.hourly)
        } else {
            weatherAdapter.replaceData(data.daily)
        }

        Glide.with(requireContext())
            .load("http://openweathermap.org/img/wn/${data.current.weather[0].icon}@2x.png")
            .into(binding.weatherIcon)
    }

    private fun setupRecycler() {
        weatherAdapter = WeatherAdapter(requireContext(), clickListener)
        if(resources.configuration.orientation==Configuration.ORIENTATION_PORTRAIT) {
            binding.weatherList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }else {
            binding.weatherList.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
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
            } else {
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

    private fun checkInternetConnection(): Boolean {
        val hasInternet: Boolean

        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            hasInternet = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            hasInternet = try {
                if (connectivityManager.activeNetworkInfo == null) {
                    false
                } else {
                    connectivityManager.activeNetworkInfo?.isConnected!!
                }
            } catch (e: Exception) {
                false
            }
        }
        return hasInternet
    }

    private fun getCurrentLocation() {
        if (checkPermission()) {
            if (checkPermission()) {
                if (isLocationEnabled()) {
                    fusedLocationProviderClient.lastLocation.addOnCompleteListener(
                        requireActivity()
                    ) { task ->
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
                    Toast.makeText(requireContext(), "Turn on location!", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                    startActivity(intent)
                }
            }
        } else {
            requestPermission()
        }
    }

    private fun requestPermission() {
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
}