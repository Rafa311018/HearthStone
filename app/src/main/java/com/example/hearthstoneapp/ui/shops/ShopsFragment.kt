package com.example.hearthstoneapp.ui.shops

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.hearthstoneapp.ui.MainActivity
import com.example.hearthstoneapp.R
import com.example.hearthstoneapp.data.network.model.PlaceResponse
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.hearthstoneapp.databinding.FragmentShopsBinding
import com.example.hearthstoneapp.util.secret.API_KEY
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.SnapshotReadyCallback
import jp.wasabeef.blurry.Blurry


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
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var currentLatLng: LatLng

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

            if (isGranted) {
                Timber.d("Permission granted by the user")
                enableMyLocation()
                mMap.snapshot(SnapshotReadyCallback { bitmap ->
                    binding.blurView.setVisibility(View.VISIBLE)
                    binding.blurView.setImageBitmap(bitmap)
                    Blurry.with(context)
                        .radius(15)
                        .sampling(2)
                        .onto(mRootLayout)
                    mAddressBarLayout.expand()
                })
            }
            else {
                Timber.d("Permission denied by the user")
                view?.findNavController()?.navigate(
                    ShopsFragmentDirections.actionNavigationShopsToNavigationMainScreen()
                )
            }
        }


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
                addMarkers(bs)
            }
        })

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_shops, container, false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        return root
    }

    private fun addMarkers(bs: PlaceResponse) {
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
            Timber.d(
                "New Marker has been made. " +
                        "${bsMarker.title} is located in ${bsMarker.position}"
            )
            bsMarker.tag = false
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            Timber.d("Getting permissions")
        }
        else{
            Timber.d("Permissions already granted.")
        }
    }


    companion object {
        val TAG = MainActivity::class.java.simpleName
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
    }

    private fun getAddress(lat: LatLng): String? {
        val geocoder = Geocoder(this.requireContext())
        val list = geocoder.getFromLocation(lat.latitude, lat.longitude,1)
        return list[0].getAddressLine(0)
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Timber.d("Case test")
            }

            mMap.isMyLocationEnabled = true

            fusedLocationClient.lastLocation.addOnSuccessListener(this.requireActivity()) { location ->

                if (location != null) {
                    lastLocation = location
                    currentLatLng = LatLng(location.latitude, location.longitude)
                    val locationString = "${location.latitude},${location.longitude}"
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
                    viewModel.searchPlaces(locationString)
                    Timber.d("Made the map")

                }
            }
        }
        else {
            Timber.d("Sad, no permissions")
        }
    }

    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            this.requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}
