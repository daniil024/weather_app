package com.example.yandexweatherapp

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.navigation.Navigation

class MainActivity : AppCompatActivity() {

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
                searchView.setQuery("",false)
                searchView.onActionViewCollapsed()
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {

                return true
            }

        })



//        val item = menu.findItem(R.id.cities_spinner)
//        val spinner = item.actionView as Spinner
//
//        val adapter = ArrayAdapter.createFromResource(
//            this,
//            R.array.cities, android.R.layout.simple_spinner_item
//        )
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//
//        spinner.adapter = adapter



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
}