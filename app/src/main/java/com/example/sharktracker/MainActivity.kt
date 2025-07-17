
package com.example.sharktracker

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import androidx.appcompat.widget.AppCompatImageButton
import android.view.View
import android.widget.ProgressBar
import android.util.Log

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var googleMap: GoogleMap
    private lateinit var viewModel: SharkTrackingViewModel
    private lateinit var sharkAdapter: SharkAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var fabRefresh: AppCompatImageButton
    private lateinit var locationPermissionHelper: LocationPermissionHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        try {
            // Initialize location permission helper
            locationPermissionHelper = LocationPermissionHelper(this)

            // Initialize views
            initViews()

            // Initialize ViewModel
            viewModel = ViewModelProvider(this).get(SharkTrackingViewModel::class.java)

            // Setup RecyclerView
            setupRecyclerView()

            // Setup map
            val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map_fragment) as SupportMapFragment
            mapFragment.getMapAsync(this)

            // Setup FAB
            fabRefresh.setOnClickListener {
                viewModel.refreshData()
                Toast.makeText(this, "Refreshing shark data...", Toast.LENGTH_SHORT).show()
            }

            // Observe ViewModel
            observeViewModel()

            // Show a test message
            Toast.makeText(this, "Shark Tracker started successfully!", Toast.LENGTH_SHORT).show()
            Log.d("MainActivity", "App initialized successfully with maps")

        } catch (e: Exception) {
            Log.e("MainActivity", "Error in onCreate: ${e.message}", e)
            Toast.makeText(this, "Error starting app: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerView)
        progressBar = findViewById(R.id.progressBar)
        fabRefresh = findViewById(R.id.fab_refresh)
    }

    private fun setupRecyclerView() {
        sharkAdapter = SharkAdapter { sharkPing ->
            // When shark is clicked, focus on it on the map
            if (::googleMap.isInitialized) {
                val location = LatLng(sharkPing.latitude, sharkPing.longitude)
                googleMap.animateCamera(
                    CameraUpdateFactory.newLatLngZoom(location, 8f)
                )
            }

            // Show shark info
            Toast.makeText(
                this,
                "${sharkPing.name} - ${sharkPing.species}",
                Toast.LENGTH_SHORT
            ).show()
        }

        recyclerView.apply {
            adapter = sharkAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
    }

    private fun observeViewModel() {
        viewModel.sharkPings.observe(this) { pings ->
            Log.d("MainActivity", "Received ${pings.size} shark pings")
            sharkAdapter.submitList(pings)
            if (::googleMap.isInitialized) {
                updateMapMarkers(pings)
            }
        }

        viewModel.isLoading.observe(this) { isLoading ->
            Log.d("MainActivity", "Loading state: $isLoading")
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(this) { error ->
            error?.let {
                Log.e("MainActivity", "Error: $it")
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.clearError()
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Set initial camera position to show East Coast
        val eastCoast = LatLng(40.0, -74.0)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eastCoast, 6f))

        // Enable map controls
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true

        // Enable location if permission is granted
        if (locationPermissionHelper.hasLocationPermission()) {
            enableMyLocation()
        } else {
            locationPermissionHelper.requestLocationPermission(
                onGranted = { enableMyLocation() },
                onDenied = {
                    Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show()
                }
            )
        }

        // Update markers if we already have data
        viewModel.sharkPings.value?.let { pings ->
            updateMapMarkers(pings)
        }

        Log.d("MainActivity", "Google Maps initialized successfully")
    }

    @SuppressLint("MissingPermission")
    private fun enableMyLocation() {
        try {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
        } catch (e: SecurityException) {
            Toast.makeText(this, "Location permission required", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateMapMarkers(pings: List<SharkPing>) {
        if (!::googleMap.isInitialized) return

        // Clear existing markers
        googleMap.clear()

        Log.d("MainActivity", "Adding ${pings.size} markers to map")

        // Add markers for each shark ping
        pings.forEach { ping ->
            val location = LatLng(ping.latitude, ping.longitude)
            val marker = googleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title(ping.name)
                    .snippet("${ping.species} - Last seen: ${ping.datetime}")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
            )

            // Store shark ping data in marker tag
            marker?.tag = ping
        }

        // Set marker click listener
        googleMap.setOnMarkerClickListener { marker ->
            val sharkPing = marker.tag as? SharkPing
            sharkPing?.let {
                // Find the shark in the RecyclerView and scroll to it
                val position = sharkAdapter.currentList.indexOf(it)
                if (position != -1) {
                    recyclerView.smoothScrollToPosition(position)
                }
                Toast.makeText(this, "${it.name} clicked!", Toast.LENGTH_SHORT).show()
            }
            false // Return false to show default info window
        }
    }
}