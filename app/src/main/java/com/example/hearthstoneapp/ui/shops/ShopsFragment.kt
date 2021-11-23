package com.example.hearthstoneapp.ui.shops

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.hearthstoneapp.BuildConfig.GOOGLE_MAPS_API_KEY
import com.example.hearthstoneapp.R
import com.example.hearthstoneapp.data.network.model.PlaceResponse
import com.example.hearthstoneapp.databinding.BottomsheetLayoutBinding
import com.example.hearthstoneapp.databinding.FragmentShopsBinding
import com.example.hearthstoneapp.ui.MainActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.blurry.Blurry
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode

@AndroidEntryPoint
class ShopsFragment : Fragment(), OnMapReadyCallback {

    val viewModel: ShopsViewModel by viewModels()
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

            if (isGranted) {
                Timber.d("Permission granted by the user")
                enableMyLocation()
                binding.mapsFragmentBlurBackground.visibility = View.INVISIBLE
            } else {
                Timber.d("Permission denied by the user")
                Toast.makeText(this.context,"Location permissions are required to search nearby stores.", Toast.LENGTH_LONG).show()
                view?.findNavController()?.navigate(
                    ShopsFragmentDirections.actionNavigationShopsToNavigationMainScreen()
                )
            }
        }
    private var lastMarker: Marker? = null

    private lateinit var binding: FragmentShopsBinding
    private lateinit var mMap: GoogleMap
    private lateinit var gotIcon: Bitmap
    private lateinit var gotSelectIcon: Bitmap
    private lateinit var placesClient: PlacesClient
    private lateinit var mapView: MapView
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var currentLatLng: LatLng


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopsBinding.inflate(inflater)
        binding.lifecycleOwner = this
        val root = binding.root

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

        fusedLocationClient =
            LocationServices.getFusedLocationProviderClient(
                requireActivity()
            )

        Places.initialize(requireActivity(), GOOGLE_MAPS_API_KEY)

        placesClient = Places.createClient(requireContext())

        mapView = root.findViewById(R.id.map) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        viewModel.searchPlaces.observe(viewLifecycleOwner, {
            //bs = bookStore
            it?.results?.forEach { bs ->
                addMarker(bs)
            }
        })

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            Timber.d("Getting permissions")
        } else {
            Timber.d("Permissions already granted.")
        }
    }

    private fun addMarker(bs: PlaceResponse) {
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
        if (!isPermissionGranted()) {
            binding.mapsFragmentBlurBackground.visibility = View.VISIBLE
            Blurry.with(requireContext())
                .radius(3)
                .sampling(8)
                .async()
                .capture(binding.mapsFragmentBlurBackground)
                .into(binding.mapsFragmentBlurBackground)
        }

        mMap = googleMap
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener { marker ->
            onMarkerClick(marker)
        }

        enableMyLocation()
    }

    private fun onMarkerClick(marker: Marker): Boolean {
        val clickCount = marker.tag as? Boolean

        val distanceLocation = Location("")

        distanceLocation.longitude = marker.position.longitude
        distanceLocation.latitude = marker.position.latitude

        val distance = BigDecimal(0.62 * (lastLocation.distanceTo(distanceLocation) / 1000))
            .setScale(2, RoundingMode.HALF_EVEN)
        Timber.d("Distance is: $distance")


        if (clickCount == false) {
            Timber.d("This marker coordinates: " + marker.snippet)

            lastMarker?.tag = false
            lastMarker?.setIcon(BitmapDescriptorFactory.fromBitmap(gotIcon))
            lastMarker = marker
            marker.setIcon(BitmapDescriptorFactory.fromBitmap(gotSelectIcon))
            marker.tag = true
        }

        val bindingSheet = DataBindingUtil.inflate<BottomsheetLayoutBinding>(
            layoutInflater,
            R.layout.bottomsheet_layout,
            null,
            false
        )

        bindingSheet.bsMapsName.text = marker.title
        bindingSheet.bsMapsAddress.text = marker.snippet
        bindingSheet.bsMapsDistance.text = getString(R.string.maps_dialog_distance, distance)
        bindingSheet.bsMapsDirection.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${marker.snippet}")
            )
            startActivity(intent)
        }

        val dialog = BottomSheetDialog(this.requireContext())
        dialog.setContentView(bindingSheet.root)
        dialog.show()

        return true
    }

    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

    private fun isPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this.requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
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

            fusedLocationClient.lastLocation.
            addOnSuccessListener(this.requireActivity()) { location ->

                if (location != null) {
                    lastLocation = location
                    currentLatLng = LatLng(location.latitude, location.longitude)
                    val locationString = "${location.latitude},${location.longitude}"
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
                    viewModel.searchPlaces(locationString)
                    Timber.d("Made the map")

                }
            }
        } else {
            Timber.d("You have no permissions")
        }
    }

    private fun getAddress(lat: LatLng): String? {
        val geocoder = Geocoder(this.requireContext())
        val list = geocoder.getFromLocation(lat.latitude, lat.longitude, 1)
        return list[0].getAddressLine(0)
    }


    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }
}
