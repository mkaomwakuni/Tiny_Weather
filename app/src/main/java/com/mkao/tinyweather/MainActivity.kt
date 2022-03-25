package com.mkao.tinyweather

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.constraintlayout.motion.widget.Debug.getLocation
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.google.android.gms.common.api.internal.TaskApiCall
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.mkao.tinyweather.databinding.ActivityMainBinding
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class MainActivity : AppCompatActivity() {
    //companion object, defines several static variables that will be initialised when the activity is created.
    companion object {
        private const val APP_ID = "716ce0536ca89a214d0a72225e2f4ae4"
        private const val CITY_NAME_URL = "https://api.openweathermap.org/data/2.5/weather?q"
        private const val GEO_LOCATION_COORDINATES =
            "https://api.openweathermap.org/data/2.5/weather?lat="
    }

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        binding.fab.setOnClickListener {
            getLocation()
        }
        binding.root.setOnRefreshListener {
            refreshData()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) getLocation()
        else if (requestCode == 1 && grantResults[0] != PackageManager.PERMISSION_GRANTED)
            Toast.makeText(
                this,
                "Permission required to fetch your Weather Information",
                Toast.LENGTH_LONG
            )
                .show()
    }


    private fun refreshData() {
        TODO("Not yet implemented")
    }

    private fun getLocation() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                1
            )
        } else {
            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val apiCall =
                        GEO_LOCATION_COORDINATES + location.latitude + "&lon" + location.longitude
                    updateWeatherData(apiCall)
                    sharedPreferences.edit().apply() {
                        putString("location", "currentLocation")
                        apply()
                    }

                }
            }
        }
    }

    private fun updateWeatherData(apiCall: String) {

    }
}