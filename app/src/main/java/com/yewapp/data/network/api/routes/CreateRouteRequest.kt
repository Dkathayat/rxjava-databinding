package com.yewapp.data.network.api.routes

data class CreateRouteResponse(
    val routeId: Int
)

data class CreateRouteRequest(
    val routeId: String,
    val coordinates: List<Coordinate>,
    val description: String,
    val distance: Int,
    val distanceUnit: String,
    val elevationGain: Double,
    val elevationLoss: Double,
    val expectedTime: String,
    val mapType: String,
    val name: String,
    val rideType: String,
    val sportId: Int,
    val staticImage: String,
)

