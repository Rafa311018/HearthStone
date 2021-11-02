package com.example.hearthstoneapp.ui.shops

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.hearthstone.data.network.repo.HearthStoneRepo
import com.example.hearthstoneapp.MainActivity
import com.example.hearthstoneapp.R
import com.example.hearthstoneapp.data.network.model.MapsResponse
import com.example.hearthstoneapp.data.network.model.PlaceResponse
import com.example.hearthstoneapp.ui.shops.adapter.BitmapHelper
import com.example.hearthstoneapp.util.createViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import android.widget.TextView

import android.widget.LinearLayout

import com.google.android.material.bottomsheet.BottomSheetBehavior

import android.widget.ImageView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.hearthstoneapp.databinding.FragmentShopsBinding
import com.google.android.gms.maps.model.Marker


class ShopsFragment : Fragment(), OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapClickListener {

    private lateinit var binding: FragmentShopsBinding

    private lateinit var latLng: LatLng

    private lateinit var mMap: GoogleMap

    private var moveCamara: Boolean = true

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewModel: ShopsViewModel by lazy {
            createViewModel {
                ShopsViewModel(
                    HearthStoneRepo.provideHearthStoneRepo(),
                    requireActivity().application
                )
            }
        }
        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        fetchLocation()
        viewModel.latLng.observe(viewLifecycleOwner, { latLngO ->
            viewModel.searchPlaces(latLngO)
            latLng = latLngO
            checkForPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, "location", 101)
        })
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shops, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
//        val supportFragmentManager = (activity as FragmentActivity).supportFragmentManager
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        viewModel.searchPlaces.observe(viewLifecycleOwner, { places ->
            val mapsResponse: MapsResponse = places as MapsResponse
            if (mapsResponse.error_message != "" && mapsResponse.status != "" && mapsResponse.status == "OK") {
                for (i in mapsResponse.results.indices) {
                    addMarkers(mapsResponse.results[i], i)
                }
            }
        })

        return binding.root
    }

    private fun addMarkers(placeResponse: PlaceResponse, position: Int) {
        val markerOptions = MarkerOptions()
            .position(
                LatLng(
                    placeResponse.geometry.location.lat,
                    placeResponse.geometry.location.lng
                )
            )
            .snippet(placeResponse.vicinity)
            .title(placeResponse.name)
            .icon(hearthstoneIcon)

        mMap.addMarker(markerOptions)?.tag = position
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMyLocationButtonClickListener(this)
        mMap.setOnMyLocationClickListener(this)
        mMap.setOnMarkerClickListener(this)
        mMap.setOnMapClickListener(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED -> {
                    fetchLocation()
                    mMap.setMyLocationEnabled(true)
                }
            }
        }
    }

    private fun fetchLocation() {
        val viewModel: ShopsViewModel by lazy {
            createViewModel {
                ShopsViewModel(
                    HearthStoneRepo.provideHearthStoneRepo(),
                    requireActivity().application
                )
            }
        }
        val task = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
        }

        task.addOnSuccessListener {
            if (it != null) {
                viewModel._latLng.value = LatLng(it.latitude, it.longitude)
                if (moveCamara) {
                    mMap.moveCamera(
                        CameraUpdateFactory.newLatLng(
                            LatLng(
                                it.latitude,
                                it.longitude
                            )
                        )
                    )
                    mMap.animateCamera(CameraUpdateFactory.zoomTo(15f), 2000, null)
                    moveCamara = false
                }
            }
        }
    }

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(
                    this.requireContext(),
                    permission
                ) == PackageManager.PERMISSION_GRANTED -> {
                }
                shouldShowRequestPermissionRationale(permission) -> showDialog(
                    permission,
                    name,
                    requestCode
                )
                else -> ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(permission),
                    requestCode
                )
            }
        }
    }

    private fun showDialog(permission: String, name: String, requestCode: Int) {
        val builder = AlertDialog.Builder(this.context)
        builder.apply {
            setMessage("Permission to access your $name is required to show you the nearest shops")
            setTitle("Permission required")
            setPositiveButton("Ok") { dialog, which ->
                ActivityCompat.requestPermissions(
                    requireActivity(),
                    arrayOf(permission),
                    requestCode
                )
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    private val hearthstoneIcon: BitmapDescriptor by lazy {
        BitmapHelper.vectorToBitmap(requireContext(), R.drawable.unnamed_copy_4)
    }

    private val hearthstoneIconSelected: BitmapDescriptor by lazy {
        BitmapHelper.vectorToBitmap(requireContext(), R.drawable.logo_copy)
    }

    override fun onMyLocationClick(location: Location) {
        Log.d("Yoshi", "Current location:\n$location")
    }

    override fun onMyLocationButtonClick(): Boolean {
        Log.d("Yoshi", "MyLocation button clicked")
        return false
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        p0.remove()
        val markerOptions = MarkerOptions()
            .position(
                LatLng(
                    p0.position.latitude,
                    p0.position.longitude
                )
            )
            .icon(hearthstoneIconSelected)
        mMap.addMarker(markerOptions)?.tag
        binding.bottomSheet.name.text = p0.title
        binding.bottomSheet.address.text = p0.snippet
        binding.bottomSheet.type.text = "Book Store"
        binding.FL.visibility = View.VISIBLE
        p0.hideInfoWindow()
        return false
    }

    override fun onMapClick(p0: LatLng) {
        binding.FL.visibility = View.GONE
        mMap.clear()
        fetchLocation()
    }
}