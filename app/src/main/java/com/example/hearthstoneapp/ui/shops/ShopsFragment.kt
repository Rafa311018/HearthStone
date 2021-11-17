package com.example.hearthstoneapp.ui.shops

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
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
import com.example.hearthstoneapp.ui.MainActivity
import com.example.hearthstoneapp.R
import com.example.hearthstoneapp.data.network.model.MapsResponse
import com.example.hearthstoneapp.data.network.model.PlaceResponse
import com.example.hearthstoneapp.ui.shops.adapter.BitmapHelper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.example.hearthstoneapp.databinding.FragmentShopsBinding
import com.example.hearthstoneapp.util.secret.API_KEY
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ShopsFragment : Fragment(), OnMapReadyCallback,
    ActivityCompat.OnRequestPermissionsResultCallback, GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnMapClickListener {

    val viewModel: ShopsViewModel by viewModels()

    private var moveCamara: Boolean = true

    private lateinit var binding: FragmentShopsBinding
    private lateinit var latLng: LatLng
    private lateinit var mMap: GoogleMap
    private lateinit var gotIcon: Bitmap
    private lateinit var gotSelectIcon: Bitmap
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient
    private lateinit var mapView: MapView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val hs_shop = BitmapFactory.decodeResource(
            context?.resources, R.drawable.hs_shop_icon
        )

        val hs_shop_selected = BitmapFactory.decodeResource(
            context?.resources, R.drawable.hs_shop_selected_icon
        )

        gotIcon = Bitmap.createScaledBitmap(
            hs_shop, 100, 100, true
        )

        gotSelectIcon = Bitmap.createScaledBitmap(
            hs_shop_selected, 120, 120, true
        )

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(
                requireActivity()
            )

        Places.initialize(requireActivity(), API_KEY)

        placesClient = Places.createClient(requireContext())

        val root = binding.root

        mapView = root.findViewById(R.id.map) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        viewModel.searchPlaces.observe(viewLifecycleOwner, {
            //bs = bookStore
            it?.results?.forEach{ bs ->
                val bsLatLng = LatLng(
                    bs.geometry.location.lat, bs.geometry.location.lng
                )
                val snippet = getAddress(bsLatLng)

                val bsMarker = mMap.addMarker(
                    MarkerOptions()
                        .position(bsLatLng)
                        .title(bs.name)
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.fromBitmap(gotIcon))
                )
                if (bsMarker != null) {
                    Timber.d("New Marker has been made. " +
                    "${bsMarker.title} is located in ${bsMarker.position}")
                    bsMarker.tag = false
                }
            }
        })

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_shops, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return root
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
                    mMap.animateCamera(
                        CameraUpdateFactory.zoomTo(15f),
                        2000,
                        null
                    )
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
        BitmapHelper.vectorToBitmap(requireContext(), R.drawable.hs_shop_icon)
    }

    private val hearthstoneIconSelected: BitmapDescriptor by lazy {
        BitmapHelper.vectorToBitmap(requireContext(), R.drawable.hs_shop_selected_icon)
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

    private fun getAddress(lat: LatLng): String? {
        val geocoder = Geocoder(this.requireContext())
        val list = geocoder.getFromLocation(lat.latitude, lat.longitude,1)
        return list[0].getAddressLine(0)
    }

}
