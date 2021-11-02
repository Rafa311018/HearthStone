package com.example.hearthstoneapp.data.network.model

data class MapsResponse(
    val results: List<PlaceResponse>,
    val error_message: String,
    val status: String
)

data class PlaceResponse(
    val geometry: Geometry,
    val name: String,
    val vicinity: String,
    val rating: Float
) {

    data class Geometry(
        val location: GeometryLocation
    )

    data class GeometryLocation(
        val lat: Double,
        val lng: Double
    )
}

//fun PlaceResponse.toPlace(): Place = Place(
//    name = name,
//    latLng = LatLng(geometry.location.lat, geometry.location.lng),
//    address = vicinity,
//    rating = rating
//)