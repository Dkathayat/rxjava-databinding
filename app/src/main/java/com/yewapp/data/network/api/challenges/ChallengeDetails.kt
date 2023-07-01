package com.yewapp.data.network.api.challenges

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.yewapp.data.network.api.addmodelequipment.EquipmentData
import com.yewapp.data.network.api.invitemember.InviteMember
import com.yewapp.data.network.api.profile.*
import com.yewapp.data.network.api.routes.Coordinate


data class ChallengeDetailResponse(
    val list: ChallengeDetails
)

data class ChallengeDetails(
    val activeStatus: String,
    val additionalInformation: String,
    val ageGroups: List<String>,
    val challengeArea: String,
    val city: List<City>,
    val combinedPrizeAwarded: Boolean,
    val combinedWinnerChoosingMethod: String,
    val combinedWinnerPrize: String,
    val combinedWinnerPrizeGiveaway: String,
    val combinedWinnerPrizeValue: String,
    val commentStatus: Boolean,
    val country: Country?,
    val createBy: String,
    val createdBy: String,
    val description: String,
    val endDate: String,
    val equipments: List<EquipmentData>,
    val featureImage: String,
    val femaleWinnerChoosingMethod: String,
    val femaleWinnerPrize: String,
    val femaleWinnerPrizeGiveaway: String,
    val femaleWinnerPrizeValue: String,
    val guidelines: String,
    val icon: String,
    val id: Int,
    val inviteUserId: List<String>,
    val isAnyoneCanJoin: Boolean,
    val isDraft: Boolean,
    val isEarnPoint: String,
    val isPointBasedParticipantCriteria: String,
    val location: String?,
    val locationLatitude: String?,
    val locationLongitude: String?,
    val maleWinnerChoosingMethod: String,
    val maleWinnerPrize: String,
    val maleWinnerPrizeGiveaway: String,
    val maleWinnerPrizeValue: String,
    val maxUserLimit: String,
    val menPrizeAwarded: Boolean,
    val minimumAvgWatt: String,
    val minimumCalories: String,
    val minimumElevationGain: String,
    val minimumMiles: String,
    val minimumPoint: String,
    val minimumTime: String,
    val name: String,
    val overview: String,
    val participants: List<Any>,
    val prize: String,
    val prizeType: String,
    val qualificationCriteria: String,
    val radius: String,
    val requiredPointToParticipate: String,
    val routeDetail: RouteDetail?,
    val routeId: String,
    val rule: String,
    val runnerUpPrizeGiveaway: String,
    val sportGradeLevel: List<String>,
    val sportName: String,
    val sportTypeId: String,
    val startDate: String,
    val state: State?,
    val step: String,
    val subSportName: String,
    val subSportTypeId: String,
    val visibility: String,
    val winnerPickedMethod: String,
    val winnerPrizeAwarded: Boolean,
    val winnerType: String,
    val winningCriteria: String,
    val womenPrizeAwarded: Boolean,
    val zipCode: String
)

data class RouteDetail(
    val cordinates: List<Coordinate>?,
    val description: String?,
    val distance: String?,
    val elevationGain: String?,
    val elevationLoss: String?,
    val expectationTime: String?,
    val id: Int?,
    @SerializedName("mapViewUrl")
    @Expose
    val mapStyle: String?,
    val name: String?,
    @SerializedName("rideType")
    @Expose
    val profileType: String?,
    val sportId: String?,
    val staticMap: String?,
    val unit: String?
)

