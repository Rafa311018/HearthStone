package com.example.hearthstoneapp.ui.shops

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.example.hearthstoneapp.MainActivity
import com.example.hearthstoneapp.R
import com.example.hearthstoneapp.data.network.model.Place
import com.example.hearthstoneapp.ui.shops.adapter.MarkerInfoWindowAdapter
import com.example.hearthstoneapp.ui.shops.adapter.PlaceRenderer
import com.example.hearthstoneapp.ui.shops.places.PlacesReader
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.maps.android.clustering.ClusterManager


class ShopsFragment : Fragment(), OnMapReadyCallback {

    private val places: List<Place> by lazy {
        PlacesReader(requireContext()).read()
    }
    private lateinit var mMap: GoogleMap


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root: View = inflater.inflate(R.layout.fragment_shops, container, false)

        val supportFragmentManager = (activity as FragmentActivity).supportFragmentManager
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapFragment?.getMapAsync { googleMap ->
        googleMap.setOnMapLoadedCallback {
            val bounds = LatLngBounds.builder()
            places.forEach { bounds.include(it.latLng) }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20))
        }
//        addClusteredMarkers(googleMap)

    }


        return root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(-34.0, 151.0)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
    companion object {
        val TAG = MainActivity::class.java.simpleName
    }

}

//
//private lateinit var binding: FragmentShopsBinding
//
//override fun onCreateView(
//    inflater: LayoutInflater,
//    container: ViewGroup?,
//    savedInstanceState: Bundle?
//): View? {
//    super.onCreateView(inflater, container, savedInstanceState)
//
//    binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shops, container, false)
//
//    val supportFragmentManager = (activity as FragmentActivity).supportFragmentManager
//    val mapFragment = supportFragmentManager.findFragmentById(R.id.map_fragment) as? SupportMapFragment
//    mapFragment?.getMapAsync { googleMap ->
//        // Ensure all places are visible in the map.
//        googleMap.setOnMapLoadedCallback {
//            val bounds = LatLngBounds.builder()
//            places.forEach { bounds.include(it.latLng) }
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20))
//        }
//        addClusteredMarkers(googleMap)
//
//    }
//
//    return binding.root
//}
//
//
//private fun addClusteredMarkers(googleMap: GoogleMap) {
//    // Create the ClusterManager class and set the custom renderer.
//    val clusterManager = ClusterManager<Place>(requireContext(), googleMap)
//    clusterManager.renderer =
//        PlaceRenderer(
//            requireContext(),
//            googleMap,
//            clusterManager
//        )
//
//    // Set custom info window adapter
//    clusterManager.markerCollection.setInfoWindowAdapter(MarkerInfoWindowAdapter(requireContext()))
//
//    // Add the places to the ClusterManager.
//    clusterManager.addItems(places)
//    clusterManager.cluster()
//
//    clusterManager.setOnClusterItemClickListener { item ->
//        addCircle(googleMap, item)
//        return@setOnClusterItemClickListener false
//    }
//
//    googleMap.setOnCameraMoveStartedListener {
//        clusterManager.markerCollection.markers.forEach { it.alpha = 0.3f }
//        clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 0.3f }
//    }
//
//    // Set ClusterManager as the OnCameraIdleListener so that it
//    // can re-cluster when zooming in and out.
//    googleMap.setOnCameraIdleListener {
//        // When the camera stops moving, change the alpha value back to opaque
//        clusterManager.markerCollection.markers.forEach { it.alpha = 1.0f }
//        clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 1.0f }
//
//        // Call clusterManager.onCameraIdle() when the camera stops moving so that re-clustering
//        // can be performed when the camera stops moving
//        clusterManager.onCameraIdle()
//    }
//
//}
//
//private var circle: Circle? = null
//
///**
// * Adds a [Circle] around the provided [item]
// */
//private fun addCircle(googleMap: GoogleMap, item: Place) {
//    circle?.remove()
//    circle = googleMap.addCircle(
//        CircleOptions()
//            .center(item.latLng)
//            .radius(1000.0)
//            .fillColor(ContextCompat.getColor(requireContext(), R.color.white))
//            .strokeColor(ContextCompat.getColor(requireContext(), R.color.black))
//    )
//}
//
//private val hearthStoneIcon: BitmapDescriptor by lazy {
//    BitmapHelper.vectorToBitmap(requireContext(), R.drawable.unnamed_copy_4)
//}
//
//private fun addMarkers(googleMap: GoogleMap) {
//    places.forEach { place ->
//        val marker = googleMap.addMarker(
//            MarkerOptions()
//                .title(place.name)
//                .position(place.latLng)
//                .icon(hearthStoneIcon)
//        )
//        marker.tag = place
//    }
//}
//

//
//override fun onMapReady(googleMap: GoogleMap) {
//    // Add a marker in Sydney, Australia,
//    // and move the map's camera to the same location.
//    val sydney = LatLng(-33.852, 151.211)
//    googleMap.addMarker(
//        MarkerOptions()
//            .position(sydney)
//            .title("Marker in Sydney")
//    )
//    googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
//}
/*-----------------------------------------------------------------*/
//
//    private val viewModel: ShopsViewModel by lazy {
//        createViewModel { ShopsViewModel() }
//    }
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_shops, container, false)
//        binding.lifecycleOwner = this
//        binding.viewModel = viewModel
//
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        checkForPermissions(android.Manifest.permission.ACCESS_FINE_LOCATION, "location", 101)
//    }
//
//    private fun checkForPermissions(permission: String, name: String, requestCode: Int) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            when {
//                ContextCompat.checkSelfPermission(
//                    this.requireContext(),
//                    permission
//                ) == PackageManager.PERMISSION_GRANTED -> {
//                    Toast.makeText(this.context, "$name permission granted", Toast.LENGTH_SHORT)
//                        .show()
//                }
//                shouldShowRequestPermissionRationale(permission) -> showDialog(
//                    permission,
//                    name,
//                    requestCode
//                )
//                else -> ActivityCompat.requestPermissions(
//                    requireActivity(),
//                    arrayOf(permission),
//                    requestCode
//                )
//            }
//        }
//    }
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        fun innerCheck(name: String) {
//            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(this.context, "$name permission refused", Toast.LENGTH_SHORT).show()
//            } else {
//
//            }
//        }
//
//        when (requestCode) {
//            101 -> innerCheck("location")
//        }
//    }
//
//    private fun showDialog(permission: String, name: String, requestCode: Int) {
//        val builder = AlertDialog.Builder(this.context)
//        builder.apply {
//            setMessage("Permission to access your $name is required to show you the nearest shops")
//            setTitle("Permission required")
//            setPositiveButton("Ok") { dialog, which ->
//                ActivityCompat.requestPermissions(requireActivity(), arrayOf(permission), requestCode)
//            }
//        }
//        val dialog = builder.create()
//        dialog.show()
//    }

