package com.example.yandexweatherapp

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.Navigation
import com.example.yandexweatherapp.room.WeatherDatabase
import com.example.yandexweatherapp.utils.Cities
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, exception ->
        println("CoroutineExceptionHandler got $exception in $coroutineContext")
    }

    private val scope = CoroutineScope(Dispatchers.IO + exceptionHandler)

    private val viewModel: WeatherViewModel by viewModels()

    private val navHostFragment by lazy {
        Navigation.findNavController(this, R.id.navHostFragment)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.custom_menu, menu)
        val searchMenuItem = menu?.findItem(R.id.action_search)
        val searchView = searchMenuItem?.actionView as SearchView
        searchView.queryHint = getString(R.string.city_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                searchView.clearFocus()
                searchView.setQuery("", false)
                searchView.onActionViewCollapsed()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                return true
            }

        })


        val item = menu.findItem(R.id.cities_spinner)
        val spinner = item.actionView as Spinner

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            Cities.values()
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val choseCity = p0?.selectedItem.toString()

                if (Cities.valueOf(choseCity) == Cities.CHOOSE) return

                if (checkInternetConnection()) {
                    val weather = scope.async {
                        viewModel.getWeather(
                            Cities.valueOf(choseCity).lat,
                            Cities.valueOf(choseCity).lon,
                            "ru"
                        )
                    }

//                        viewModel.putLocationToSharPref(
//                            Cities.valueOf(choseCity).lat,
//                            Cities.valueOf(choseCity).lon
//                        )
                    scope.launch {
                        weather.await()
                        val weatherToSave = viewModel.oneCallApiCallWeatherDTO.value
                        if (weatherToSave != null)
                            viewModel.saveWeather(weatherToSave)
                    }
                } else {
                    scope.launch(Dispatchers.IO) {
                        viewModel.retrieveWeather(
                            Cities.valueOf(choseCity).timezone
                        )
                    }
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> {
                Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.action_share -> {
                Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.share_VK -> {
                Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show()
                true
            }
            R.id.share_instagram -> {
                Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show()
                true
            }
            else -> {
                Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show()
                true
            }
        }
    }

    fun checkInternetConnection(): Boolean {
        val hasInternet: Boolean

        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

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
}