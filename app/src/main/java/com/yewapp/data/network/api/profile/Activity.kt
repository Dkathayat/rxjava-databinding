package com.yewapp.data.network.api.profile

data class Activity(
    val title: String,
    val type: Int,
    val description: String,
    val timeStamp: String,
    val activityData: ActivityData,
    val location: Coordinates,
    val points: Points
)

class Points(
    val pointGained: String,
    val maxPoints: String
)


data class ActivityData(
    val calorie: String,
    val distance: String,
    val time: String,
    val elevation: String
)

data class Coordinates(
    val latitude: String,
    val longitude: String
)