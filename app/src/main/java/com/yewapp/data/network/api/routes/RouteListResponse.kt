package com.yewapp.data.network.api.routes

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


data class RouteListResponse(val pageData: PageData?, val list: List<Route>)

@Parcelize
data class Route(
    var id: Int?,
    val name: String?,
    val expectedTime: String?,
    val distance: Double?,
    val description: String?,
    val calories: Int?,
    val elevation: Int?,
    val atheletesTravelled: Int?,
    val sportType: String?,
    val sportTypeIcon: String,
    val mapIcon: String
) : Parcelable//val coordinates: List<Coordinates>?

@Parcelize
data class Coordinates(val latitude: Double?, val longitude: Double?) : Parcelable

data class PageData(
    val total: Int?,
    val per_page: Int?,
    val current_page: Int?,
    val last_page: Int?
)

data class RouteDetailResponse(
    val atheletesTravelled: Int,
    val calories: Int,
    val coordinates: List<Coordinate>,
    val description: String,
    val distance: Double,
    val distanceUnit: String,
    val elevationGain: Double,
    val elevationLoss: Double,
    val expectedTime: String,
    val id: Int,
    val mapIcon: String,
    val sportTypeIcon: String?,
    val isRouteFavorite: Boolean?,
    val name: String,
    val rideType: String
)

@Parcelize
data class Coordinate(
    val latitude: String,
    val longitude: String
):Parcelable
