package com.yewapp.data.network.api.challenges

//Not used

data class ChallengesActive(
    val daysLeft: Int,
    val title: String,
    val subTitle: String,
    val description: String,
    val challengeType: Int,
    val maxPoint: Int,
    val earnedPoints: Int,
    val draft: String,
    val calorieValue: String,
    val distanceValue: Int,
    val timeValue: Int,
    val elevationGainValue: Int
)
