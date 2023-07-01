package com.yewapp.data.network.api.createchallenge

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.yewapp.data.network.api.addmodelequipment.EquipmentData

data class CreateChallengeRequest(
    val id: String = "",//null when create challenge otherwise pass challenge id in edit challenge
    val isDraft: Boolean?,
    val sportTypeId: Int?,
    val subSportTypeId: String?,
    @SerializedName("name")
    @Expose
    val challengeName: String?,//challenge Name
    val sportGradeLevel: List<String>?,//EX:["Beginner"] multiple selection *****
    val equipments: List<EquipmentData>?,
    val visibility: String?,
    val description: String?,//Challenge Description
    val minimumCalories: String?,
    val minimumMiles: String?,//get time when create or select route
    val minimumElevationGain: String?,//get time when create or select route
    val minimumAvgWatt: String?,
    val minimumTime: String?,//get time when create or select route
    val maxUserLimit: Int?,
    val countryId: Int?,
    val stateId: Int?,
    val cityIds: List<Int>?,
    val zipcodes: List<Int>?,
//    val ageGroup: String?,
    val ageGroup: List<String?>,
    val inviteUserId: List<String?>,

//    inviteUserId
    val location: String?,
    val locationLatitude: String?,
    val locationLongitude: String?,
    val overview: String?,
    val additionalInformation: String?,
    val rule: String?,
    val qualificationCriteria: String?,
    val prizeType: String?,//FSD From Leaderboard or Randomly
    val prize: String?,
    val winnerPrizeAwarded: Int,
    val winnerPickedMethod: String?,
    val winningCriteria: String?,//According. to FSD Challenges can be only goal based.
    val startDate: String?,
    val endDate: String?,
    val routeId: String?,
    val minimumPoint: String?,
    val icon: String?,
    val featureImage: String?,
    @SerializedName("activeStatus")
    @Expose
    val challengeStatus: String?,
    val makeModel: List<Int>?,//[ 5, 10 ]
    val step: String?,//new Added screen number
    val challengeArea: String,// radius or extended
    val locationRadius: String,  //5,10 to 30
    val guidelines: String  //5,10 to 30
)

//For Sports Level/Type EX: Beginner, Pro and Advanced
data class StaticMultipleSelection(
    val name: String?,
    var status: Boolean
)