package com.marwaeltayeb.photoweather.ui.main

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.marwaeltayeb.photoweather.R
import com.marwaeltayeb.photoweather.databinding.ActivityMainBinding
import com.marwaeltayeb.photoweather.ui.history.HistoryActivity
import com.marwaeltayeb.photoweather.ui.main.location.LocationCallback
import com.marwaeltayeb.photoweather.ui.main.location.LocationService
import com.marwaeltayeb.photoweather.ui.main.location.LocationStorage.Companion.getLoc
import com.marwaeltayeb.photoweather.utils.Const.Companion.API_KEY
import com.marwaeltayeb.photoweather.utils.Const.Companion.CAMERA_PERMISSION_CODE
import com.marwaeltayeb.photoweather.utils.Const.Companion.CAMERA_REQUEST
import com.marwaeltayeb.photoweather.utils.Const.Companion.PERMISSIONS_REQUEST_LOCATION
import com.marwaeltayeb.photoweather.utils.Const.Companion.UNIT_NAME
import com.marwaeltayeb.photoweather.utils.ImageUtils.drawTextOnBitmap
import com.marwaeltayeb.photoweather.utils.ImageUtils.shareImageUri
import com.marwaeltayeb.photoweather.utils.NetworkUtils.isNetworkAvailable


private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), LocationCallback {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel

    private lateinit var uri: Uri

    private var locationManager: LocationManager? = null
    private var locationService: LocationService? = null

    private var lat: Double = 0.0
    private var lon: Double = 0.0

    private lateinit var photo: Bitmap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
        locationService = LocationService(this, this)


        val location = getLoc(this)
        if (location.getLat() != null && location.getLon() != null) {
            lat = getLoc(this).getLat()!!.toDouble()
            lon = getLoc(this).getLon()!!.toDouble()
        } else {
            checkLocationPermission()
        }

        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        mainViewModel.getWeatherStates().observe(this, { currentResponse ->
            Log.d("Yes", currentResponse.toString())

            val cityName = currentResponse.cityName
            val temperature = currentResponse.main.temp.toInt()
            val weatherState = currentResponse.weather.get(0).weatherState

            photo.let {
                val weatherInfo = "$cityName\n$temperature c \n$weatherState"
                val newBitmap: Bitmap = drawTextOnBitmap(this, photo, weatherInfo)
                binding.loadingIndicator.visibility = View.GONE
                binding.imgPhotoWeather.setImageBitmap(newBitmap)

                mainViewModel.saveImage(this, cacheDir, newBitmap)
            }
        })

        mainViewModel.getPhotoUri().observe(this, { photoUri ->
            uri = photoUri
        })

        binding.btnTakePhoto.setOnClickListener {

            if (!isNetworkAvailable(this)) {
                Toast.makeText(applicationContext, "Check Internet Connection", Toast.LENGTH_LONG)
                    .show()
                return@setOnClickListener
            }

            if (lat != 0.0 && lon != 0.0) {
                launchCamera()
            } else {
                checkLocationPermission()
            }
        }
    }

    private fun launchCamera() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_CODE)
            } else {
                val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                startActivityForResult(cameraIntent, CAMERA_REQUEST)
            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            photo = data?.extras!!["data"] as Bitmap

            Log.d(TAG, "onActivityResult $lat$lon")

            mainViewModel.requestWeatherStates(lat, lon, UNIT_NAME, API_KEY)
            binding.loadingIndicator.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.hsitory_action -> {
                val intent = Intent(this, HistoryActivity::class.java)
                startActivity(intent)
                return true
            }

            R.id.share_action -> {
                if (::uri.isInitialized) {
                    shareImageUri(this, uri)
                } else {
                    Toast.makeText(
                        applicationContext,
                        "You should take a photo first",
                        Toast.LENGTH_LONG
                    ).show()
                }
                return true
            }

            R.id.location_action -> {
                if (lat == 0.0 && lon == 0.0) {
                    checkLocationPermission()
                    Log.d("location_action", "Yes")
                } else {
                    Toast.makeText(
                        this,
                        "Location Coordinates has been received. Enjoy our service now!",
                        Toast.LENGTH_LONG
                    ).show()
                    Log.d("location_action", "No")
                }
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }


    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // show an explanation
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                promptUserToAccept()
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_LOCATION
                )
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        if (requestCode == PERMISSIONS_REQUEST_LOCATION) {

            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted.
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    // Request location updates
                    locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000L, 500f, locationService)

                    locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000L, 500f, locationService)

                    locationManager?.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 10000L, 500f, locationService)
                }
            }
        }
    }

    private fun promptUserToAccept() {
        AlertDialog.Builder(this)
            .setMessage("Location permission is required to get access to weather data")
            .setPositiveButton("Ok") { dialogInterface: DialogInterface?, i: Int ->
                //Prompt the user once explanation has been shown
                ActivityCompat.requestPermissions(
                    this@MainActivity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSIONS_REQUEST_LOCATION
                )
            }
            .create()
            .show()
    }

    override fun onLocationResult() {
        Log.d(TAG, "onLocationResult $lat$lon")
        Toast.makeText(this, "Location Coordinates has been received.", Toast.LENGTH_LONG).show()
        lat = getLoc(this).getLat()!!.toDouble()
        lon = getLoc(this).getLon()!!.toDouble()
    }
}